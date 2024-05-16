package de.ganzer.fx.validation;

import de.ganzer.core.validation.TextFormat;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.core.validation.ValidatorExceptionRef;
import javafx.application.Platform;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

/**
 * A common formatter that encapsulates an instance of the {@link Validator}
 * class to validate text during input.
 * <p>
 * This formatter should be used for validation instead of using the validator
 * itself because it is able to change the style of the control if the input is
 * invalid. In this case the control's tooltip is set to the validators error
 * message.
 * {@code
@FXML
private Button okButton;
@FXML
private TextField numNuggets;
@FXML
private TextField numEnemies;

@FXML
private void initialize() {
    new ValidatorTextFormatter(new NumberValidator(1, 10), numNuggets);
    new ValidatorTextFormatter(new NumberValidator(1, 10), numEnemies);
}

@FXML
private void closeDialog(ActionEvent actionEvent) {
    Node source = (Node)actionEvent.getSource();

    if (source != okButton || applyValues(true)) {
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}

private boolean applyValues(boolean withMessageBox) {
    // Show errors by MessageBox an by error indicator:
    //
    if (withMessageBox)
        return validate(numEnemies) && validate(numNuggets);

    // Show errors by error indicators only:
    //
    boolean isValid = true;

    isValid = ((ValidatorTextFormatter)numEnemies.getTextFormatter()).isValid() && isValid;
    isValid = ((ValidatorTextFormatter)numNuggets.getTextFormatter()).isValid() && isValid;

    return isValid;
}

private boolean validate(TextField field) {
    try {
        ((ValidatorTextFormatter)field.getTextFormatter()).validate();
    } catch (ValidatorException e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("My Application");
        a.setHeaderText(ResourceBundle.getBundle("messages").getString("invalidInput"));
        a.setContentText(e.getMessage());
        a.showAndWait();

        field.requestFocus();
        field.selectAll();

        return false;
    }

    return true;
}
 * }
 */
@SuppressWarnings("unused")
public class ValidatorTextFormatter extends TextFormatter<String> {
    private static final String DEFAULT_ERROR_STYLE = "-fx-border-color: red; -fx-focus-color: red;";
    private final Validator validator;
    private final TextInputControl control;
    private boolean setErrorIndicators = true;
    private String errorStyle = DEFAULT_ERROR_STYLE;
    private boolean validateOnLostFocus = true;
    private String orgStyle;
    private Tooltip orgTooltip;
    private boolean errorIndicatorsVisible;

    /**
     * Creates a new instance from the specified arguments.
     * <p>
     * This needs th control where the created {@link ValidatorTextFormatter}
     * instance is linked to, to recognize focus change events of the control.
     * <p>
     * Additionally, the controls text formatter is automatically set to this
     * instance. So, the code may be shortened as follows:
     * <p>
     * {@code new ValidatorTextFormatter(myValidator, myControl);}
     * <p>
     * Instead of
     * <p>
     * {@code myControl.setTextFormatter(new ValidatorTextFormatter(myValidator, myControl));}
     *
     * @param validator The validator to use for input validation. This must not
     *                  be {@code null}.
     * @param control   The control that's input shall be validated. This must not
     *                  be {@code null}.
     *
     * @throws NullPointerException validator or control is {@code null}.
     */
    public ValidatorTextFormatter(Validator validator, TextInputControl control) {
        super(change -> {
            var current = change.getControlText();
            var input = change.getControlNewText();

            if (input.equals(current))
                return change;

            var text = new StringBuilder(input);

            if (!validator.isValidInput(text, true))
                return null;

            if (!text.toString().equals(input)) {
                control.setText(text.toString());

                Platform.runLater(() -> {
                    var pos = control.getText().length();
                    control.selectRange(pos, pos);
                });

                return null;
            }

            return change;
        });

        if (validator == null)
            throw new NullPointerException("validator");

        if (control == null)
            throw new NullPointerException("control");

        control.setTextFormatter(this);

        control.focusedProperty().addListener((p, o, n) -> {
            if (n)
                control.setText(validator.formatText(control.getText(), TextFormat.EDIT));
            else {
                control.setText(validator.formatText(control.getText(), TextFormat.DISPLAY));
                validate(false);
            }
        });

        this.validator = validator;
        this.control = control;
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
     * Indicates whether validation is automatically performed when the control
     * has lost its focus.
     *
     * @return {@code true} if automatic validation is performed; otherwise,
     * {@code false}.
     */
    public boolean isValidateOnLostFocus() {
        return validateOnLostFocus;
    }

    /**
     * Sets whether an automatic validation shall be performed when the control
     * has lost its focus.
     *
     * @param validateOnLostFocus {@code false} to permit automatic validation.
     *                            The default is {@code true}.
     */
    public void setValidateOnLostFocus(boolean validateOnLostFocus) {
        this.validateOnLostFocus = validateOnLostFocus;
    }

    /**
     * Indicates whether the style of the control is changed on error.
     *
     * @return {@code true} if the style is changed on error; otherwise,
     * {@code false}.
     */
    public boolean isSetErrorIndicators() {
        return setErrorIndicators;
    }

    /**
     * Sets whether the style of the control is changed on error.
     *
     * @param setErrorIndicators {@code true} to change the style on error. The
     *                        default is {@code true}.
     */
    public void setSetErrorIndicators(boolean setErrorIndicators) {
        this.setErrorIndicators = setErrorIndicators;
    }

    /**
     * Gets the current style to indicate an error.
     *
     * @return The current style to use for indicating an error.
     */
    public String getErrorStyle() {
        return errorStyle;
    }

    /**
     * Sets the style to use to indicate an error.
     *
     * @param errorStyle The style to set. If this is {@code null}, the default
     *                   style "-fx-border-color: red; -fx-focus-color: red;"
     *                   is set.
     */
    public void setErrorStyle(String errorStyle) {
        this.errorStyle = errorStyle == null
                ? DEFAULT_ERROR_STYLE
                : errorStyle;
    }

    /**
     * Resets the style of the control to its default.
     */
    public void resetStyle() {
        if (!errorIndicatorsVisible)
            return;

        control.setStyle(orgStyle);
        control.setTooltip(orgTooltip);

        errorIndicatorsVisible = false;
    }

    /**
     * Validates the input.
     *
     * @return {@code true} if input is valid; otherwise, {@code false}
     * is returned.
     */
    public boolean isValid() {
        return validate(false);
    }

    /**
     * Checks whether the current text of the linked control is valid.
     *
     * @throws ValidatorException If the text is invalid.
     */
    public void validate() throws ValidatorException {
        validate(true);
    }

    /**
     * Checks whether the current text of the linked control is valid.
     *
     * @param er A reference to a possible exception. If this method returns
     *           {@code false}, the encapsulated exception is set to an
     *           instance of {@link ValidatorException}. This must not be
     *           {@code null}.
     *
     * @return {@code true} if input is valid; otherwise, {@code false}
     * is returned.
     */
    public boolean validate(ValidatorExceptionRef er) {
        if (validator.validate(control.getText(), er)) {
            resetStyle();
            return true;
        }

        if (setErrorIndicators && !errorIndicatorsVisible) {
            orgStyle = control.getStyle();
            orgTooltip = control.getTooltip();

            errorIndicatorsVisible = true;

            control.setStyle(errorStyle);
            control.setTooltip(new Tooltip(er.getException().getMessage()));
        }

        return false;
    }

    private boolean validate(boolean throwException) {
        ValidatorExceptionRef ref = new ValidatorExceptionRef();

        if (!validate(ref)) {
            if (throwException)
                throw ref.getException();

            return false;
        }

        return true;
    }
}
