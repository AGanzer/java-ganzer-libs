package de.ganzer.fx.validation;

import de.ganzer.core.validation.TextFormat;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.core.validation.ValidatorExceptionRef;
import javafx.application.Platform;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

/**
 * A common formatter that encapsulates an instance of the {@link Validator}
 * class to validate text during input.
 * <p>
 * If you don't want to create fields or variables for the validators, the
 * validation can be done by an instance of this class:
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

    if (source != okButton || applyValues()) {
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}

private boolean applyValues() {
    return validate(numEnemies) &&
            validate(numNuggets);
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
    private final Validator validator;
    private final TextInputControl control;

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
     * @throws NullPointerException validator is {@code null}.
     * @throws NullPointerException control is {@code null}.
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

            if (text.length() - input.length() != 0) {
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

        control.focusedProperty().addListener((p, o, n) ->
                control.setText(validator.formatText(control.getText(), n ? TextFormat.EDIT : TextFormat.DISPLAY)));

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
     * Checks whether the current text of the linked control is valid.
     *
     * @throws ValidatorException If the text is invalid.
     */
    public void validate() throws ValidatorException {
        validator.validate(control.getText());
    }

    /**
     * Checks whether the current text of the linked control is valid.
     *
     * @param er A reference to a possible exception. If this method returns
     *           {@code false}, the encapsulated exception is set to an
     *           instance of {@link ValidatorException}. This must not be
     *           {@code null}.
     * @return {@code true} if input is valid; otherwise, <c>false</c>
     * is returned.
     */
    public boolean validate(ValidatorExceptionRef er) {
        return validator.validate(control.getText(), er);
    }
}
