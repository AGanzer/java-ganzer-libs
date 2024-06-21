package de.ganzer.fx.validation;

import de.ganzer.core.validation.ValidatorException;
import de.ganzer.core.validation.ValidatorExceptionRef;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A utility class that simplifies the validation of multiple inputs.
 * <p>
 * {@code
@FXML
public Button okButton;
@FXML
public TextField numNuggets;
@FXML
public TextField numEnemies;

private final ValidatorTextFormatterList formatterList = new ValidatorTextFormatterList();

@FXML
public void initialize() {
    formatterList.add(new ValidatorTextFormatter(new NumberValidator(1, 10), numNuggets));
    formatterList.add(new ValidatorTextFormatter(new NumberValidator(1, 10), numEnemies));
}

@FXML
public void closeDialog(ActionEvent actionEvent) {
    Node source = (Node)actionEvent.getSource();

    if (source != okButton || applyValues()) {
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}

private boolean applyValues() {
    // Instead of this complicate code:
    //
    //boolean isValid = true;
    //isValid = ((ValidatorTextFormatter)numEnemies.getTextFormatter()).isValid() && isValid;
    //isValid = ((ValidatorTextFormatter)numNuggets.getTextFormatter()).isValid() && isValid;
    //
    // just:
    //
    boolean isValid = formatterList.validate();

    if (isValid) {
        // Apply values here.
    }

    return isValid;
}
 * }
 */
@SuppressWarnings("unused")
public class ValidatorTextFormatterList extends ArrayList<ValidatorTextFormatter> {
    /**
     * The handler that is invoked on a failed validation.
     * <p>
     * This handler can be used to show a dialog instead of only setting a red
     * border:
     * <p>
     * {@code
    private final ValidatorTextFormatterList formatterList = new ValidatorTextFormatterList();

    @FXML
    public void initialize() {
        formatterList.setOnValidationFail((f, e) -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("My Application");
            a.setHeaderText(ResourceBundle.getBundle("messages").getString("invalidInput"));
            a.setContentText(e.getMessage());
            a.showAndWait();

            f.getControl().requestFocus();
            f.getControl().selectAll();

            return false;
        });

        // Do all other initialization here.
    }
     * }
     */
    public interface ValidationFailHandler {
        /**
         * This is invoked when a validation has failed.
         *
         * @param formatter The formatter that contains the control with the
         *                  invalid content.
         * @param exception The exception that contains the error message.
         *
         * @return {@code true} to validate the next controls; otherwise, no
         *         further control is validated.
         */
        boolean validationFailed(ValidatorTextFormatter formatter, ValidatorException exception);
    }

    private ValidationFailHandler validationFailHandler;

    /**
     * {@inheritDoc}
     */
    public ValidatorTextFormatterList() {
    }

    /**
     * {@inheritDoc}
     */
    public ValidatorTextFormatterList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * {@inheritDoc}
     */
    public ValidatorTextFormatterList(Collection<? extends ValidatorTextFormatter> c) {
        super(c);
    }

    /**
     * Gets the handler that shall be invoked when a validation fails.
     *
     * @return The set handler or {@code null} if no handler is set.
     *
     * @see #setValidationFailHandler(ValidationFailHandler)
     */
    public ValidationFailHandler getValidationFailHandler() {
        return validationFailHandler;
    }

    /**
     * Sets the handler to invoke when a validation fails.
     * <p>
     * A call to {@link #validate()} validates each control that are encapsulated
     * in the contained formatters. If a validation fails and a handler is set,
     * the handler is called with the formatter that's text i not valid. If the
     * handler returns {@code false}, the loop is left and no further validation
     * is done.
     *
     * @param validationFailHandler The handler to set. This may be {@code null}
     *                              (the default).
     */
    public void setValidationFailHandler(ValidationFailHandler validationFailHandler) {
        this.validationFailHandler = validationFailHandler;
    }

    /**
     * Validates all input that is inserted into this {@code ValidatorTextFormatterList}.
     * <p>
     * If a handler ist set by {@link #setValidationFailHandler(ValidationFailHandler)}
     * this is called on each validation failure as long as the handler does not
     * return {@code false}, what means that the validation loop is left.
     *
     * @return {@code true} if all input is valid; otherwise, {@code false}.
     */
    public boolean validate() {
        ValidatorExceptionRef exceptionRef = new ValidatorExceptionRef();
        boolean valid = true;
        
        for (ValidatorTextFormatter formatter: this) {
            boolean isValid = formatter.validate(exceptionRef);

            if (!isValid && validationFailHandler != null) {
                if (!validationFailHandler.validationFailed(formatter, exceptionRef.getException()))
                    return false;
            }

            valid = valid && isValid;
        }

        return valid;
    }
}
