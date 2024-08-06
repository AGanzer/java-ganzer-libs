package de.ganzer.swing.validaton;

import de.ganzer.core.validation.TextFormat;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.core.validation.ValidatorExceptionRef;
import de.ganzer.swing.internals.SwingMessages;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ValidationFilter extends DocumentFilter {
    private static Border errorBorder = BorderFactory.createLineBorder(Color.RED, 1);
    private static Consumer<ValidatorException> errorConsumer;

    private final Validator validator;
    private final JTextComponent textField;
    private boolean validateOnLostFocus;
    private String orgTooltip;
    private Border orgBorder;
    private boolean hintsVisible;
    private boolean updating;

    public ValidationFilter(Validator validator, JTextComponent textField) {
        this(validator, textField, true);
    }

    public ValidationFilter(Validator validator, JTextComponent textField, boolean validateOnLostFocus) {
        Objects.requireNonNull(validator, "validator must not be null.");
        Objects.requireNonNull(textField, "textField must not be null.");

        this.validator = validator;
        this.textField = textField;
        this.validateOnLostFocus = validateOnLostFocus;

        ((AbstractDocument)textField.getDocument()).setDocumentFilter(this);

        setListeners();
    }

    public static Border getErrorBorder() {
        return errorBorder;
    }

    public static void setErrorBorder(Border errorBorder) {
        Objects.requireNonNull(errorBorder, "errorBorder must not be null.");
        ValidationFilter.errorBorder = errorBorder;
    }

    public static Consumer<ValidatorException> getErrorConsumer() {
        return errorConsumer;
    }

    public static void setErrorConsumer(Consumer<ValidatorException> errorConsumer) {
        ValidationFilter.errorConsumer = errorConsumer;
    }

    public boolean isValidateOnLostFocus() {
        return validateOnLostFocus;
    }

    public void setValidateOnLostFocus(boolean validateOnLostFocus) {
        this.validateOnLostFocus = validateOnLostFocus;
    }

    public String getText() {
        return textField.getText();
    }

    public boolean validate(ValidationBehavior behavior) {
        var e = doValidation();

        if (e == null) {
            resetVisualHints();
            return true;
        }

        doErrorHandling(e, behavior);
        return false;
    }

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
                if (validateOnLostFocus)
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
