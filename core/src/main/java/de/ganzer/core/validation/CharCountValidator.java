package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;

/**
 * This CharCountValidator class defines a validator that requires a specified
 * range of characters to input.
 */
public class CharCountValidator extends Validator {
    private int maxLength;
    private int minLength;

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     */
    public CharCountValidator() {
    }

    /**
     * Creates a new instance of the validator.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     */
    public CharCountValidator(int options) {
        super(options);
    }

    /**
     * Gets the maximum length of a valid text.
     *
     * @return The maximum length of a valid text.
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the maximum length of a valid text.
     * <p>
     * Setting this value to 0, causes each text length that is not less than
     * {link #getMinLength} to be valid. This is the default.
     * <p>
     * If maxLength is less than {link #getMinLength} and greater than 0,
     * the minimum length is set to maxLength, if {link #getMinLength} is not 0.
     *
     * @param maxLength The maximum length to set.
     * @throws IllegalArgumentException If maxLength is less than 0.
     */
    public void setMaxLength(int maxLength) {
        if (maxLength < 0)
            throw new IllegalArgumentException("maxLength");

        this.maxLength = maxLength;

        if (minLength > 0 && maxLength < minLength)
            minLength = maxLength;
    }

    /**
     * Gets the minimum length of a valid text.
     *
     * @return The minimum length of a valid text.
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * Sets the minimum length of a valid text.
     * <p>
     * Setting this value to 0, causes each text length that is not greater than
     * {link #getMaxLength} to be valid. This is the default.
     * <p>
     * Setting the minimum length to a value greater than 0, does not imply that
     * an empty text is invalid. An empty text is invalid only if the option
     * {@link ValidatorOptions#NEEDS_INPUT} is set. Setting the minimum length
     * to 0 means, that a not empty text must have a minimum length of minLength
     * characters.
     * <p>
     * If minLength is greater than {link #getMaxLength} and greater than 0,
     * the maximum length is set to minLength, if {link #getMaxLength} is not 0.
     *
     * @param minLength The minimum length to set.
     * @throws IllegalArgumentException If minLength is less than 0.
     */
    public void setMinLength(int minLength) {
        if (minLength < 0)
            throw new IllegalArgumentException("minLength");

        this.minLength = minLength;

        if (maxLength > 0 && minLength > maxLength)
            maxLength = minLength;
    }

    /**
     * This implementation calls {@link Validator#doInputValidation}
     * and checks whether the text length is less than or equal to
     * {@link #getMaxLength()} if it is greater than 0.
     *
     * @param text     The text to validate. This is never {@code null}. The
     *                 text must not be modified if autoFill is {@code false}.
     * @param autoFill Indicates whether the text is allowed to be modified.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    protected boolean doInputValidation(StringBuilder text, boolean autoFill) {
        if (!super.doInputValidation(text, autoFill))
            return false;

        return maxLength == 0 || text.length() <= maxLength;
    }

    /**
     * This implementation  calls the {@link Validator#doInputValidation} and
     * checks whether the text length is in the range that is specified by
     * {@link #getMinLength()} and {@link #getMaxLength()}.
     *
     * @param text   The text to validate. This is never {@code null}. The text
     *               must not be modified if autoFill is {@code false}.
     * @param er     A reference to a possible exception. If this method returns
     *               {@code false}, the encapsulated exception is set to an
     *               instance of {@link ValidatorException}. This must not be
     *               {@code null}.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    protected boolean doValidate(String text, ValidatorExceptionRef er) {
        if (!super.doValidate(text, er))
            return false;

        if (text.isEmpty())
            return true;

        if (maxLength != 0 && text.length() > maxLength)
            er.setException(new ValidatorException(String.format(getErrorMessage() != null ? getErrorMessage() : CoreMessages.get("inputExceedsMaxLength"), maxLength)));
        else if (minLength != 0 && text.length() < minLength)
            er.setException(new ValidatorException(String.format(getErrorMessage() != null ? getErrorMessage() : CoreMessages.get("inputBelowMinLength"), minLength)));

        return er.getException() == null;
    }
}
