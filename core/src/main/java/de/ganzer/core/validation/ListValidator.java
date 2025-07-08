package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;

import java.util.List;

/**
 * A validator that accepts input that is contained in a list of valid inputs.
 */
public class ListValidator extends Validator {
    private List<String> validInputs;
    private boolean ignoreCase;

    /**
     * Creates a new instance of the validator where all inputs are valid.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     */
    public ListValidator() {
    }

    /**
     * Creates a new instance of the validator where all inputs are valid.
     *
     * @param options The options to set. This may be any combination of the
     *         {@link ValidatorOptions} constants.
     */
    public ListValidator(int options) {
        super(options);
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     *
     * @param validInputs The list with the valid inputs or {@code null} to make
     *        all inputs valid.
     */
    public ListValidator(List<String> validInputs) {
        this.validInputs = validInputs;
    }

    /**
     * Creates a new instance of the validator.
     *
     * @param options The options to set. This may be any combination of the
     *         {@link ValidatorOptions} constants.
     * @param validInputs The list with the valid inputs or {@code null} to make
     *        all inputs valid.
     */
    public ListValidator(int options, List<String> validInputs) {
        super(options);
        this.validInputs = validInputs;
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     *
     * @param validInputs The list with the valid inputs or {@code null} to make
     *        all inputs valid.
     * @param ignoreCase {@code true} to ignore case sensitivity.
     */
    public ListValidator(List<String> validInputs, boolean ignoreCase) {
        this.validInputs = validInputs;
        this.ignoreCase = ignoreCase;
    }

    /**
     * Creates a new instance of the validator.
     *
     * @param options The options to set. This may be any combination of the
     *         {@link ValidatorOptions} constants.
     * @param validInputs The list with the valid inputs or {@code null} to make
     *        all inputs valid.
     * @param ignoreCase {@code true} to ignore case sensitivity.
     */
    public ListValidator(int options, List<String> validInputs, boolean ignoreCase) {
        super(options);
        this.validInputs = validInputs;
        this.ignoreCase = ignoreCase;
    }

    /**
     * Gets the list of valid inputs.
     *
     * @return The list of valid inputs or {@code null} if all inputs are valid.
     */
    public List<String> getValidInputs() {
        return validInputs;
    }

    /**
     * Sets the list of valid inputs.
     *
     * @param validInputs Sets the list of valid inputs or {@code null} if all
     *        inputs shall be valid.
     */
    public void setValidInputs(List<String> validInputs) {
        this.validInputs = validInputs;
    }

    /**
     * Gets a value indicating whether the case sensitivity is ignored.
     *
     * @return {@code true} if the case sensitivity is ignored.
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    /**
     * Sets a value indicating whether the case sensitivity shall be ignored.
     *
     * @param ignoreCase {@code true} to ignore the case sensitivity.
     */
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    /**
     * Tests whether the input is within the valid values.
     *
     * @param text The text to validate. This is never {@code null}. The text
     *         must not be modified if autoFill is {@code false}.
     * @param autoFill Indicates whether the text is allowed to be modified.
     *
     * @return {@code true} if text is valid; otherwise, {@code false} is
     *         returned.
     */
    @Override
    protected boolean doInputValidation(StringBuilder text, boolean autoFill) {
        if (!super.doInputValidation(text, autoFill))
            return false;

        if (validInputs == null || validInputs.isEmpty() || text.isEmpty())
            return true;

        String input = text.toString();

        if (ignoreCase)
            return validInputs.stream().anyMatch(s -> s.equalsIgnoreCase(input));
        else
            return validInputs.contains(input);
    }

    /**
     * Tests whether the input is within the valid values.
     * <p>
     * Inheritors should overwrite this to do a more complex validation.
     *
     * @param text The text to validate. This is never {@code null}. The text
     *         must not be modified if autoFill is {@code false}.
     * @param er A reference to a possible exception. If this method returns
     *         {@code false}, the encapsulated exception is set to an
     *         instance of {@link ValidatorException}. This must not be
     *         {@code null}.
     *
     * @return {@code true} if text is valid; otherwise, {@code false} is
     *         returned.
     */
    @Override
    protected boolean doValidate(String text, ValidatorExceptionRef er) {
        if (!super.doValidate(text, er))
            return false;

        if (validInputs == null || validInputs.isEmpty() || text.isEmpty())
            return true;

        boolean result;

        if (ignoreCase)
            result = validInputs.stream().anyMatch(s -> s.equalsIgnoreCase(text));
        else
            result = validInputs.contains(text);

        if (!result)
            er.setException(new ValidatorException(CoreMessages.get("inputDoesNotMatchList")));

        return result;
    }
}
