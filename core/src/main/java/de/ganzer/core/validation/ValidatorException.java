package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;
import de.ganzer.core.util.Strings;

/**
 * This exception is used to notify about invalid text.
 */
public class ValidatorException extends RuntimeException {
    private final Class<?> sourceClass;
    private final Validator source;

    /**
     * {@inheritDoc}
     */
    @Deprecated(forRemoval = true, since = "5.4.0")
    public ValidatorException() {
        this(null, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated(forRemoval = true, since = "5.4.0")
    public ValidatorException(String message) {
        this(message, null, null);
    }

    /**
     * Creates a new instance.
     *
     * @param message The message to set.
     * @param sourceClass The class of the validator that has detected an invalid
     *        input.
     * @param source The validator instance that has detected an invalid
     *        input.
     *
     * @param <V> Should extend {@link Validator}.
     *
     * @since 5.4.0
     */
    public <V extends Validator> ValidatorException(String message, Class<V> sourceClass, Validator source) {
        super(Strings.isNullOrBlank(message) ? CoreMessages.get("invalidInput") : message);

        this.sourceClass = sourceClass;
        this.source = source;
    }

    /**
     * Gets the class of the Validator that detected the input as invalid.
     *
     * @return The class of the validator or {@code null} if no class is set.
     *
     * @since 5.4.0
     */
    public Class<?> getSourceClass() {
        return sourceClass;
    }

    /**
     * Gets the Validator that detected the input as invalid.
     *
     * @return The validator or {@code null} if no validator is set.
     *
     * @since 5.4.0
     */
    public Validator getSource() {
        return source;
    }
}
