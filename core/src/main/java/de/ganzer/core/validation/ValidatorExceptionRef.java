package de.ganzer.core.validation;

/**
 * This class is used by the {@link Validator} class to get an error message on
 * invalid input when you don't want to implement a try-catch block.
 * <p>
 * <pre>{@code
 * public bool validateInput(String input, Validator validator) {
 *     ValidatorExceptionRef er = new ValidatorExceptionRef();
 *
 *     if (validator.validate(input, er))
 *       return true;
 *
 *     System.out.println(er.getException().getMessage());
 *     return false;
 * }
 * }</pre>
 */
public class ValidatorExceptionRef {
    private ValidatorException exception;

    /**
     * Gets the encapsulated exception.
     *
     * @return The exception that describes the invalid input or {@code null}
     * if the input was valid.
     */
    public ValidatorException getException() {
        return exception;
    }

    /**
     * Sets the exception.
     *
     * @param exception The exception to set. This should only be used within
     *                  a validator.
     */
    public void setException(ValidatorException exception) {
        this.exception = exception;
    }
}
