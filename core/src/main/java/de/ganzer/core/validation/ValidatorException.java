package de.ganzer.core.validation;

/**
 * This exception is used to notify about invalid text.
 */
public class ValidatorException extends RuntimeException {
    /**
     * {@inheritDoc}
     */
    public ValidatorException() {
    }

    /**
     * {@inheritDoc}
     */
    public ValidatorException(String message) {
        super(message);
    }
}
