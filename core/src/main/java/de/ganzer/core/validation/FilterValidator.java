package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;

/**
 * The FilterValidator class defines a validator that filters an input by
 * comparing the input characters with valid and invalid characters.
 * <p>
 * The valid and invalid characters are specified by masks. Each character
 * within a mask string is used as is, excepted the characters that are bound
 * by a minus sign. This sign defines a range of characters to use. To add the
 * minus sign itself, it must be the first or the last character in the mask
 * or the left character must be higher than the right character (like "z-a"
 * or "2-1").
 * <p>
 * Examples:
 * <ul>
 *     <li> <b>"a-zA-Z"</b>: All alphabetical letters in lowercase and uppercase.
 *     <li> <b>"0-9.+-"</b>: The decimal digits as well as the colon and the
 *     positive and negativ signs.
 *     <li> <b>"9-0"</b>: '9' and '0' are inserted as well as '-'.
 *     <li> <b>"09-"</b>: '9' and '0' are inserted as well as '-'.
 *     <li> <b>"{@literal a-zA-Z\t\\}"</b>: All alphabetical letters in lowercase
 *     and uppercase as well as the tabulator and the backslash.
 * </ul>
 * <p>
 * Invalid characters are useful to invalidate characters that would be valid
 * by the specification of the valid mask. For example: It is less difficult to
 * write "0-9" for valid characters and "256" for invalid ones to exclude the
 * digits 2, 5 and 6 as to write "01347-9" for the valid characters only.
 */
public class FilterValidator extends CharCountValidator {
    private String validMask = "";
    private String invalidMask = "";
    private String validChars = "";
    private String invalidChars = "";

    /**
     * Creates a new instance of the validator where all characters are valid.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     */
    public FilterValidator() {
    }

    /**
     * Creates a new instance of the validator where all characters are valid.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     */
    public FilterValidator(int options) {
        super(options);
    }

    /**
     * Creates a new instance of the validator where all characters are valid.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     *
     * @param validMask The mask that specifies the valid characters. If this
     *                  is empty, all characters are valid. See {@link FilterValidator}
     *                  for an explanation of masks. If this is {@code null},
     *                  an empty string is set.
     */
    public FilterValidator(String validMask) {
        this.validMask = validMask == null ? "" : validMask;
        validChars = generateChars(this.validMask);
    }

    /**
     * Creates a new instance of the validator where all characters are valid.
     *
     * @param options   The options to set. This may be any combination of the
     *                  {@link ValidatorOptions} constants.
     * @param validMask The mask that specifies the valid characters. If this
     *                  is empty, all characters are valid. See {@link FilterValidator}
     *                  for an explanation of masks. If this is {@code null},
     *                  an empty string is set.
     */
    public FilterValidator(int options, String validMask) {
        super(options);

        this.validMask = validMask == null ? "" : validMask;
        validChars = generateChars(this.validMask);
    }

    /**
     * Creates a new instance of the validator where all characters are valid.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     *
     * @param validMask   The mask that specifies the valid characters. If this
     *                    is empty, all characters are valid. See {@link FilterValidator}
     *                    for an explanation of masks. If this is {@code null},
     *                    an empty string is set.
     * @param invalidMask The mask that specifies the invalid characters. If this
     *                    is empty, all characters are valid. See {@link FilterValidator}
     *                    for an explanation of masks. If this is {@code null},
     *                    an empty string is set.
     */
    public FilterValidator(String validMask, String invalidMask) {
        this.validMask = validMask == null ? "" : validMask;
        this.invalidMask = invalidMask == null ? "" : invalidMask;

        validChars = generateChars(this.validMask);
        invalidChars = generateChars(this.invalidMask);
    }

    /**
     * Creates a new instance of the validator where all characters are valid.
     *
     * @param options     The options to set. This may be any combination of the
     *                    {@link ValidatorOptions} constants.
     * @param validMask   The mask that specifies the valid characters. If this
     *                    is empty, all characters are valid. See {@link FilterValidator}
     *                    for an explanation of masks. If this is {@code null},
     *                    an empty string is set.
     * @param invalidMask The mask that specifies the invalid characters. If this
     *                    is empty, all characters are valid. See {@link FilterValidator}
     *                    for an explanation of masks. If this is {@code null},
     *                    an empty string is set.
     */
    public FilterValidator(int options, String validMask, String invalidMask) {
        super(options);

        this.validMask = validMask == null ? "" : validMask;
        this.invalidMask = invalidMask == null ? "" : invalidMask;

        validChars = generateChars(this.validMask);
        invalidChars = generateChars(this.invalidMask);
    }

    /**
     * Gets the mask that specifies the valid characters.
     * <p>
     * See {@link FilterValidator} for an explanation of masks.
     *
     * @return The mask or an empty string if all characters are valid.
     */
    public String getValidMask() {
        return validMask;
    }

    /**
     * Sets the mask for valid characters.
     * <p>
     * See {@link FilterValidator} for an explanation of masks.
     *
     * @param validMask The mask that specifies the characters to use.
     *                  If this is empty, all characters are valid. If
     *                  this is {@code null}, an empty string is set.
     */
    public void setValidMask(String validMask) {
        this.validMask = validMask == null ? "" : validMask;
        validChars = generateChars(this.validMask);
    }

    /**
     * Gets the mask that specifies the invalid characters.
     * <p>
     * See {@link FilterValidator} for an explanation of masks.
     *
     * @return The mask or an empty string if no character is invalid.
     */
    public String getInvalidMask() {
        return invalidMask;
    }

    /**
     * Sets the mask for invalid characters.
     * <p>
     * See {@link FilterValidator} for an explanation of masks.
     *
     * @param invalidMask The mask that specifies the characters to use.
     *                    If this is empty, no characters are invalid. If
     *                    this is {@code null}, an empty string is set.
     */
    public void setInvalidMask(String invalidMask) {
        this.invalidMask = invalidMask == null ? "" : invalidMask;
        invalidChars = generateChars(this.invalidMask);
    }

    /**
     * This implementation calls the {@link CharCountValidator#doInputValidation}
     * and checks whether the input does contain only valid characters.
     *
     * @param text     The text to validate. This is never {@code null}. The text
     *                 must not be modified if autoFill is {@code false}.
     * @param autoFill Indicates whether the text is allowed to be modified.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    @Override
    protected boolean doInputValidation(StringBuilder text, boolean autoFill) {
        return super.doInputValidation(text, autoFill) && validateText(text.toString());
    }

    /**
     * This implementation calls the {@link CharCountValidator#doValidate} and
     * checks whether the input does contain only valid characters.
     *
     * @param text   The text to validate. This is never {@code null}.
     * @param er     A reference to a possible exception. If this method returns
     *               {@code false}, the encapsulated exception is set to an
     *               instance of {@link ValidatorException}. This must not be
     *               {@code null}.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    @Override
    protected boolean doValidate(String text, ValidatorExceptionRef er) {
        if (!super.doValidate(text, er))
            return false;

        if (validateText(text))
            return true;

        er.setException(new ValidatorException(getErrorMessage() != null ? getErrorMessage() : CoreMessages.get("inputContainsInvalidCharacters")));
        return false;
    }

    private boolean validateText(String text) {
        if (text.isEmpty())
            return true;

        if (!validChars.isEmpty())
            for (int i = 0; i < text.length(); ++i)
                if (validChars.indexOf(text.charAt(i)) == -1)
                    return false;

        if (!invalidChars.isEmpty())
            for (int i = 0; i < text.length(); ++i)
                if (invalidChars.indexOf(text.charAt(i)) != -1)
                    return false;

        return true;
    }

    private String generateChars(String mask) {
        StringBuilder target = new StringBuilder();

        for (int m = 0; m < mask.length(); ++m) {
            // Check for a binding '-' and loop through the characters to fill:
            //
            if (mask.charAt(m) == '-' && m > 0 && m + 1 < mask.length() && mask.charAt(m - 1) < mask.charAt(m + 1)) {
                ++m;

                for (int c = mask.charAt(m - 2) + 1; c < mask.charAt(m); ++c)
                    target.append((char)c);
            }

            // Add the character where m points to:
            //
            target.append(mask.charAt(m));
        }

        return target.toString();
    }
}
