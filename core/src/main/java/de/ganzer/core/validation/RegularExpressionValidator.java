package de.ganzer.core.validation;

import de.ganzer.core.CoreMessages;

import java.util.regex.Pattern;

/**
 * The RegularExpressionValidator class defines a validator that validates text
 * by using a regular expression.
 */
public class RegularExpressionValidator extends Validator {
    private Pattern pattern;

    /**
     * Creates a new instance of the validator where every input is valid.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     */
    public RegularExpressionValidator() {
    }

    /**
     * Creates a new instance of the validator where every input is valid.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     */
    public RegularExpressionValidator(int options) {
        super(options);
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     *
     * @param pattern The pattern to use for validation. If this is {@code null},
     *                every input is valid.
     */
    public RegularExpressionValidator(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Creates a new instance of the validator.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     * @param pattern The pattern to use for validation. If this is {@code null},
     *                every input is valid.
     */
    public RegularExpressionValidator(int options, Pattern pattern) {
        super(options);
        this.pattern = pattern;
    }

    /**
     * Gets the pattern that is used for validation.
     *
     * @return The used pattern or {@code null} if all input is valid.
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Sets the pattern to use for validation.
     *
     * @param pattern The pattern to use for validation. If this is {@code null},
     *                every input is valid.
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * This implementation calls {@link Validator#doInputValidation} and checks
     * whether the input does match the regular expression.
     *
     * @param text     The text to validate. This is never {@code null}.
     * @param autoFill Indicates whether the text is allowed to be modified.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    @Override
    protected boolean doInputValidation(StringBuilder text, boolean autoFill) {
        if (!super.doInputValidation(text, autoFill))
            return false;

        if ((text.length() == 0))
            return true;

        var end = firstFailurePoint(pattern, text.toString());

        return end == -1 || end == text.length();
    }

    /**
     * This implementation calls the {@link Validator#doInputValidation} and checks
     * whether the input does match the regular expression.
     *
     * @param text The text to validate. This is never {@code null}.
     * @param er   A reference to a possible exception. If this method returns
     *             {@code false}, the encapsulated exception is set to an
     *             instance of {@link ValidatorException}. This must not be
     *             {@code null}.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    @Override
    protected boolean doValidate(String text, ValidatorExceptionRef er) {
        if (!super.doValidate(text, er))
            return false;

        if ((text.length() == 0) || pattern == null)
            return true;

        if (pattern.matcher(text).matches())
            return true;

        er.setException(new ValidatorException(getErrorMessage() != null
                ? getErrorMessage()
                : String.format(CoreMessages.get("inputDoesNotMatchExpression"), getPattern())));

        return false;
    }

    private static int firstFailurePoint(Pattern pattern, String text) {
        for (int i = 1; i <= text.length(); ++i) {
            var m = pattern.matcher(text.substring(0, i));

            if (!m.matches() && !m.hitEnd())
                return i - 1;
        }

        return pattern.matcher(text).matches() ? -1 : text.length();
    }
}