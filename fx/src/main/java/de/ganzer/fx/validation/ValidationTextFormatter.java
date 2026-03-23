package de.ganzer.fx.validation;

import de.ganzer.core.validation.TextFormat;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.core.validation.ValidatorExceptionRef;
import de.ganzer.fx.internals.FXMessages;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A common formatter that encapsulates an instance of the {@link Validator}
 * class to validate text during input.
 * <p>
 * This formatter should be used for validation instead of using the validator
 * itself because it is able to change the style of the control if the input is
 * invalid. In this case the control's tooltip is set to the validators error
 * message.
 * <pre>{@code
 * public class InputTestController {
 *     private ValidationTextFormatter inputFormatter;
 *     // Other fields here.
 *
 *     @FXML
 *     public void initialize() {
 *         inputFormatter = new ValidationTextFormatter(new NumberValidator(0.0, 100.0), inputField);
 *     }
 *
 *     @FXML
 *     public void closeDialog(ActionEvent actionEvent) {
 *         Node source = (Node)actionEvent.getSource();
 *
 *         if (source != okButton || applyValues()) {
 *             Stage stage = (Stage)source.getScene().getWindow();
 *             stage.close();
 *         }
 *     }
 *
 *     private boolean applyValues() {
 *         if (!inputFormatter.validate(ValidationBehavior.SHOW_MESSAGE_BOX))
 *             return false;
 *
 *         // Apply values here.
 *         return true;
 *     }
 * }
 * }</pre>
 *
 * @since 5.4.0
 */
@SuppressWarnings("unused")
public class ValidationTextFormatter extends TextFormatter<String> {
    private static Consumer<ValidatorException> errorConsumer;
    private static ValidationHintProvider hintProvider = new StyleValidationHint();

    private final TextInputControl control;

    private Validator validator;
    private boolean validateOnFocusLost;
    private boolean liveValidation;
    private boolean hintsVisible;

    /**
     * Creates a new instance from the specified arguments.
     * <p>
     * This sets {@link #isValidateOnFocusLost()} to {@code false} and
     * {@link #isLiveValidation()} to {@code true}.
     * <p>
     * This needs the control where the created {@link ValidationTextFormatter}
     * instance is linked to, to recognize focus change events of the control.
     * <p>
     * Additionally, the controls text formatter is automatically set to this
     * instance. So, the code may be shortened as follows:
     * <p>
     * {@code new ValidationTextFormatter(myValidator, myControl);}
     * <p>
     * Instead of
     * <p>
     * {@code myControl.setTextFormatter(new ValidationTextFormatter(myValidator, myControl));}
     *
     * @param validator The validator to use for input validation.
     * @param control   The control that's input shall be validated.
     *
     * @throws NullPointerException {@code validator} or {@code control} is
     *         {@code null}.
     */
    public ValidationTextFormatter(Validator validator, TextInputControl control) {
        this(validator, control, false, true);
    }

    /**
     * Creates a new instance from the specified arguments.
     * <p>
     * This needs the control where the created {@link ValidationTextFormatter}
     * instance is linked to, to recognize focus change events of the control.
     * <p>
     * Additionally, the controls text formatter is automatically set to this
     * instance. So, the code may be shortened as follows:
     * <p>
     * {@code new ValidationTextFormatter(myValidator, myControl);}
     * <p>
     * Instead of
     * <p>
     * {@code myControl.setTextFormatter(new ValidationTextFormatter(myValidator, myControl));}
     *
     * @param validator The validator to use for input validation.
     * @param control   The control that's input shall be validated.
     * @param validateOnFocusLost If {@code true} the validation is done when
     *        {@code inputField} loses its focus.
     * @param liveValidation If {@code true} the input is validated live. See
     *        {@link #setLiveValidation(boolean)} for further details.
     *
     * @throws NullPointerException {@code validator} or {@code control} is
     *         {@code null}.
     */
    public ValidationTextFormatter(Validator validator, TextInputControl control, boolean validateOnFocusLost, boolean liveValidation) {
        super(new ValidationChangeFilter());
        ((ValidationChangeFilter) getFilter()).setFormatter(this);

        Objects.requireNonNull(validator,"validator must not be null.");
        Objects.requireNonNull(control, "control must not be null.");

        control.setTextFormatter(this);
        control.focusedProperty().addListener((p, o, n) -> {
            if (n)
                control.setText(validator.formatText(getText(), TextFormat.EDIT));
            else {
                control.setText(validator.formatText(getText(), TextFormat.DISPLAY));

                if (validateOnFocusLost)
                    validate(ValidationBehavior.SET_VISUAL_HINTS);
            }
        });

        this.validator = validator;
        this.control = control;
        this.validateOnFocusLost = validateOnFocusLost;
        this.liveValidation = liveValidation;
    }

    /**
     * Gets the currently used hint provider.
     *
     * @return The currently used hint provider.
     */
    public static ValidationHintProvider getHintProvider() {
        return hintProvider;
    }

    /**
     * Sets the hint provider to use.
     *
     * @param hintProvider The provider to set or {@code null} to use the
     *         default provider (an instance of {@link StyleValidationHint}).
     */
    public static void setHintProvider(ValidationHintProvider hintProvider) {
        ValidationTextFormatter.hintProvider = hintProvider == null ? new StyleValidationHint() : hintProvider;
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
        ValidationTextFormatter.errorConsumer = errorConsumer;
    }

    /**
     * Gets a value indicating whether live validation is active.
     * <p>
     * For a detailed explanation see {@link #setLiveValidation(boolean)}.
     *
     * @return {@code true} if live validation is active.
     *
     * @see #setLiveValidation(boolean)
     *
     * @since 5.4.0
     */
    public boolean isLiveValidation() {
        return liveValidation;
    }

    /**
     * Activates or deactivates live validation.
     * <p>
     * If live validation is active, the validation error hints are updated
     * live while the user inputs its text. If this is not active, the hints
     * are updated on lost focus if {@link #isValidateOnFocusLost()} is true
     * or on explicitly invoking {@link #validate(ValidationBehavior)}.
     *
     * @param activate {@code true} to activate live validation.
     *
     * @since 5.4.0
     */
    public void setLiveValidation(boolean activate) {
        this.liveValidation = activate;
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
     * Gets the control that is validated.
     *
     * @return The control to validate.
     */
    public TextInputControl getControl() {
        return control;
    }

    /**
     * Gets the validator that is used for validation.
     *
     * @return The used validator.
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
     * Validates the input.
     *
     * @param behavior The behavior to use for validation.
     *
     * @return {@code true} if the input is valid; otherwise, {@code false}.
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
     * Resets all visual hints to hide them in any is visible.
     */
    public void resetVisualHints() {
        if (!hintsVisible)
            return;

        hintProvider.hideHints(control);
        hintsVisible = false;
    }

    private ValidatorException doValidation() {
        ValidatorExceptionRef ref = new ValidatorExceptionRef();

        if (!validator.validate(getText(), ref))
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

        control.requestFocus();
        control.selectAll();
    }

    private void showError(ValidatorException e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("My Application");
        a.setHeaderText(FXMessages.get("invalidInput"));
        a.setContentText(e.getMessage());
        a.showAndWait();
    }

    private void setVisualHints(ValidatorException e) {
        if (hintsVisible) {
            hintProvider.updateHints(control, e);
        } else {
            hintProvider.showHints(control, e);
            hintsVisible = true;
        }
    }

    private String getText() {
        return control.getText() == null ? "" : control.getText();
    }
}
