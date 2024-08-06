package de.ganzer.swing.validaton;

import de.ganzer.core.validation.*;
import de.ganzer.swing.internals.SwingMessages;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A document filter that validates the input of a {@link JTextComponent}.
 * <p>
 * Example how to use it in a simple input dialog:
 * <p>
 * {@code
public class InputTestDialog extends JDialog {
    private ValidationFilter inputFilter;
    // Other fields here.

    private InputTestDialog(Frame owner) {
        super(owner, "Input Test", true);
        initTextField();
        initButtons();
    }

    private void initTextField() {
        var inputField = new JTextField(30);
        // Set the validator:
        inputFilter = new ValidationFilter(new NumberValidator(0.0, 100.0), inputField);

        getContentPane().add(inputField);
    }

    private void initButtons() {
        // Init OK and Cancel buttons here.
    }

    // OK is clicked:
    private void onOk(GActionEvent event) {
        if (!inputFilter.validate(ValidationBehavior.SET_VISUAL_HINTS))
            return;

        // Update dialog data here.
    }
}
 * }
 */
@SuppressWarnings("unused")
public class ValidationFilter extends DocumentFilter {
    private static Border errorBorder = BorderFactory.createLineBorder(Color.RED, 1);
    private static Consumer<ValidatorException> errorConsumer;

    private final JTextComponent textField;
    private Validator validator;
    private boolean validateOnFocusLost;
    private String orgTooltip;
    private Border orgBorder;
    private boolean hintsVisible;
    private boolean updating;

    /**
     * Create a new filter from the specified arguments.
     * <p>
     * This sets {@link #isValidateOnFocusLost()} to {@code true}.
     *
     * @param validator The validator to use.
     * @param textField The text field to validate.
     *
     * @throws NullPointerException {@code validator} or {@code textField} is
     *         {@code null}.
     */
    public ValidationFilter(Validator validator, JTextComponent textField) {
        this(validator, textField, true);
    }

    /**
     * Create a new filter from the specified arguments.
     *
     * @param validator The validator to use.
     * @param textField The text field to validate.
     * @param validateOnFocusLost If {@code true} the validation is done when
     *        {@code inputField} loses its focus.
     *
     * @throws NullPointerException {@code validator} or {@code textField} is
     *         {@code null}.
     *
     * @see #setValidateOnFocusLost(boolean)
     */
    public ValidationFilter(Validator validator, JTextComponent textField, boolean validateOnFocusLost) {
        Objects.requireNonNull(validator, "validator must not be null.");
        Objects.requireNonNull(textField, "textField must not be null.");

        this.validator = validator;
        this.textField = textField;
        this.validateOnFocusLost = validateOnFocusLost;

        ((AbstractDocument)textField.getDocument()).setDocumentFilter(this);

        setListeners();
    }

    /**
     * Gets the border to use for marking a text field with invalid input.
     *
     * @return The set border. The default is a red thin border.
     */
    public static Border getErrorBorder() {
        return errorBorder;
    }

    /**
     * Sets the border to use for marking a text field with invalid input.
     *
     * @param errorBorder The border to set.
     *
     * @throws NullPointerException {@code border} is {@code null}.
     */
    public static void setErrorBorder(Border errorBorder) {
        Objects.requireNonNull(errorBorder, "errorBorder must not be null.");
        ValidationFilter.errorBorder = errorBorder;
    }

    /**
     * Gets the consumer to call on validation error.
     *
     * @return The currently set consumer or {@code null} if no consumer is set.
     *
     * @see #setErrorConsumer(Consumer)
     */
    public static Consumer<ValidatorException> getErrorConsumer() {
        return errorConsumer;
    }

    /**
     * Sets the specified error consumer.
     * <p>
     * This is useful if the default message box should not be displayed. If
     * this is set, the default handling will not take place and {@code consumer}
     * is invoked to shaw a message box.
     *
     * @param errorConsumer The consumer to set or {@code null} to display a
     *        default message box.
     */
    public static void setErrorConsumer(Consumer<ValidatorException> errorConsumer) {
        ValidationFilter.errorConsumer = errorConsumer;
    }

    /**
     * Gets a value that indicates whether validation is done when the text
     * field has lost its focus.
     *
     * @return {@code true} if validation is performed when the focus is lost.
     */
    public boolean isValidateOnFocusLost() {
        return validateOnFocusLost;
    }

    /**
     * Sets a value that indicates whether validation is done when the text
     * field has lost its focus.
     * <p>
     * Independently of the set {@link ValidationBehavior} when
     * {@link #validate(ValidationBehavior)} is invoked, the error hints are
     * always set on invalid input if {@code validateOnFocusLost} is
     * {@code true}.
     *
     * @param validateOnFocusLost {@code true} to validate the input when the
     *        text field loses its focus.
     */
    public void setValidateOnFocusLost(boolean validateOnFocusLost) {
        this.validateOnFocusLost = validateOnFocusLost;
    }

    /**
     * Gets the text of the text field.
     *
     * @return The current text of the text field.
     */
    public String getText() {
        return textField.getText();
    }

    /**
     * Gets the validator that was set at construction.
     *
     * @return The set validator.
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Sets the validator to use for input validation.
     *
     * @param validator The validator to use.
     *
     * @throws NullPointerException {@code validator} is {@code null}.
     */
    public void setValidator(Validator validator) {
        Objects.requireNonNull(validator, "validator must not be null.");
        this.validator = validator;
    }

    /**
     * Gets the text field that was set at construction.
     *
     * @return The text field.
     */
    public JTextComponent getTextField() {
        return textField;
    }

    /**
     * Validates the input.
     *
     * @param behavior The behavior to use for validation.
     *
     * @return {@code true} if all input is valid; otherwise, {@code false}.
     *         Remark that there is no return if {@code behavior} is
     *         {@link ValidationBehavior#THROW_EXCEPTION}.
     */
    public boolean validate(ValidationBehavior behavior) {
        var e = doValidation();

        if (e == null) {
            resetVisualHints();
            return true;
        }

        doErrorHandling(e, behavior);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (updating) {
            super.insertString(fb, offset, string, attr);
            updating = false;
        } else {
            var textToInsert = doInputValidation(fb, offset, 0, string);

            if (textToInsert != null)
                fb.insertString(offset, textToInsert, attr);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (updating) {
            super.replace(fb, offset, length, text, attrs);
            updating = false;
        } else {
            var textToInsert = doInputValidation(fb, offset, length, text);

            if (textToInsert != null)
                fb.replace(offset, length, textToInsert, attrs);
        }
    }

    private void setListeners() {
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                var newText = validator.formatText(textField.getText(), TextFormat.EDIT);

                if (!newText.equals(textField.getText())) {
                    updating = true;
                    textField.setText(newText);
                    textField.selectAll();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (validateOnFocusLost)
                    validate(ValidationBehavior.SET_VISUAL_HINTS);

                var newText = validator.formatText(textField.getText(), TextFormat.DISPLAY);

                if (!newText.equals(textField.getText())) {
                    updating = true;
                    textField.setText(newText);
                }
            }
        });
    }

    private String doInputValidation(FilterBypass fb, int offset, int length, String text) throws BadLocationException {
        var textToValidate = getCompleteText(fb, offset, length, text);
        var fullTextLength = textToValidate.length();
        var orgTextLength = textField.getDocument().getLength();
        var appending = isAppending(orgTextLength, length, offset);

        if (validator.isValidInput(textToValidate, appending)) {
            return appending
                    ? textToValidate.substring(orgTextLength)
                    : textToValidate.substring(offset, offset + text.length());
        }

        return null;
    }

    private StringBuilder getCompleteText(FilterBypass fb, int offset, int length, String text) throws BadLocationException {
        var textLength = textField.getDocument().getLength();
        var completeText = new StringBuilder(fb.getDocument().getText(0, textLength));

        completeText.replace(offset, offset + length, text);

        return completeText;
    }

    private boolean isAppending(int length, int removeLength, int offset) {
        return offset >= length - removeLength;
    }

    private ValidatorException doValidation() {
        ValidatorExceptionRef ref = new ValidatorExceptionRef();

        if (!validator.validate(textField.getText(), ref))
            return ref.getException();

        return null;
    }

    private void doErrorHandling(ValidatorException e, ValidationBehavior behavior) {
        switch (behavior) {
            case SHOW_MESSAGE_BOX:
                showErrorMessage(e);
                break;

            case SET_VISUAL_HINTS:
                setVisualHints(e);
                break;

            case THROW_EXCEPTION:
                throw e;
        }
    }

    private void showErrorMessage(ValidatorException e) {
        if (errorConsumer != null)
            errorConsumer.accept(e);
        else
            showError(e);

        textField.grabFocus();
        textField.selectAll();
    }

    private void showError(ValidatorException e) {
        JOptionPane.showMessageDialog(
                textField.getRootPane(),
                e.getMessage(),
                SwingMessages.get("invalidInputHint"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetVisualHints() {
        if (!hintsVisible)
            return;

        textField.setToolTipText(orgTooltip);
        textField.setBorder(orgBorder);

        hintsVisible = false;
        orgTooltip = null;
        orgBorder = null;
    }

    private void setVisualHints(ValidatorException e) {
        if (!hintsVisible) {
            orgTooltip = textField.getToolTipText();
            orgBorder = textField.getBorder();
            hintsVisible = true;
        }

        textField.setToolTipText(e.getLocalizedMessage());
        textField.setBorder(errorBorder);
    }
}
