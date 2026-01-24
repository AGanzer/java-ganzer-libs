package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;
import de.ganzer.core.util.Strings;

import java.util.Objects;

/**
 * This defines a validator that supports basic validation of text.
 * <p>
 * The validation that can be done by an instance of this class is
 * to test whether an input is required.
 */
public class Validator {
    /**
     * The default error message for required input.
     * @see #setRequiredErrorMessage(String)
     * @since 5.4.0
     */
    public final static String DEFAULT_REQUIRED_ERROR_MESSAGE = CoreMessages.get("inputRequired");
    /**
     * The default error message for blank input.
     * @see #setBlanksErrorMessage(String)
     * @since 5.4.0
     */
    public final static String DEFAULT_BLANKS_ERROR_MESSAGE = CoreMessages.get("blanksNotAllowed");

    private String requiredErrorMessage = DEFAULT_REQUIRED_ERROR_MESSAGE;
    private String blanksErrorMessage = DEFAULT_BLANKS_ERROR_MESSAGE;
    private int options;
    private String errorMessage;
    private Object tag;

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     */
    public Validator() {
        options = ValidatorOptions.NEEDS_INPUT;
    }

    /**
     * Creates a new instance of the validator.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     */
    public Validator(int options) {
        this.options = options;
    }

    /**
     * Gets the options that are used by the validator.
     *
     * @return The used options. This may be any combination of the
     * {@link ValidatorOptions} constants.
     */
    public int getOptions() {
        return options;
    }

    /**
     * Sets the options to use.
     *
     * @param validatorOptions The options to use. This may be any combination
     *                         of the {@link ValidatorOptions} constants.
     */
    public void setOptions(int validatorOptions) {
        this.options = validatorOptions;
    }

    /**
     * Gets the message that is shown if the input is empty, but it is required.
     *
     * @return The error message to use. The default is
     *         {@link #DEFAULT_REQUIRED_ERROR_MESSAGE}.
     *
     * @since 5.4.0
     */
    public String getRequiredErrorMessage() {
        return requiredErrorMessage;
    }

    /**
     * Sets the message that is shown if the input is empty, but it is required.
     *
     * @param requiredErrorMessage The message to use. If this is
     *        {@code null}, empty or does contain white spaces only,
     *        {@link #DEFAULT_REQUIRED_ERROR_MESSAGE} is used.
     *
     * @since 5.4.0
     */
    public void setRequiredErrorMessage(String requiredErrorMessage) {
        this.requiredErrorMessage = Strings.isNullOrBlank(requiredErrorMessage)
                ? DEFAULT_REQUIRED_ERROR_MESSAGE
                : requiredErrorMessage;
    }

    /**
     * Gets the message that is shown if the input contains only blanks, but
     * this is not allowed.
     *
     * @return The error message to use. The default is
     *         {@link #DEFAULT_BLANKS_ERROR_MESSAGE}.
     *
     * @since 5.4.0
     */
    public String getBlanksErrorMessage() {
        return blanksErrorMessage;
    }

    /**
     * Sets the message that is shown if the input contains only blanks, but
     * this is not allowed.
     *
     * @param blanksErrorMessage The message to use. If this is
     *        {@code null}, empty or does contain white spaces only,
     *        {@link #DEFAULT_BLANKS_ERROR_MESSAGE} is used.
     *
     * @since 5.4.0
     */
    public void setBlanksErrorMessage(String blanksErrorMessage) {
        this.blanksErrorMessage = Strings.isNullOrBlank(blanksErrorMessage)
                ? DEFAULT_BLANKS_ERROR_MESSAGE
                : blanksErrorMessage;
    }

    /**
     * Gets the common error message to use with this validator.
     * <p>
     * If this is {@code null}, the granulated messages of the respective
     * validator are used (like {@link #getBlanksErrorMessage()} or
     * {@link #getRequiredErrorMessage()}).
     *
     * @return The error message to use if a text is invalid or {@code null}
     *         if this is not set.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message to use with this validator.
     * <p>
     * If this is {@code null}, the granulated messages of the respective
     * validator are used (like {@link #getBlanksErrorMessage()} or
     * {@link #getRequiredErrorMessage()}).
     * <p>
     * If errorMessage is empty or contains whitespaces only, the error
     * message ist set to {@code null}.
     *
     * @param errorMessage The error message to use if a text is invalid. If
     *         is empty or contains whitespaces only, {@code null} is set.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = Strings.isNullOrBlank(errorMessage)
                ? null
                : errorMessage;
    }

    /**
     * Gets a tag object.
     * <p>
     * This property can be used to store application specific information
     * that must be associated with a validator. It is not used by the
     * validator itself.
     *
     * @return The set a tag object.
     */
    public Object getTag() {
        return tag;
    }

    /**
     * Sets a tag object.
     * <p>
     * This property can be used to store application specific information
     * that must be associated with a validator. It is not used by the
     * validator itself.
     *
     * @param tag The object to set.
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * Determines whether the specified option is set.
     *
     * @param validatorOption The option to query.
     *
     * @return {@code true} if the specified option is set. If validatorOption
     * is a combination of multiply options, {@code true} is returned if at
     * least one of the queried options is set.
     */
    public boolean hasOption(int validatorOption) {
        return (options & validatorOption) != 0;
    }

    /**
     * Validates the specified text.
     * <p>
     * {@code isValidInput} is usable to check an input that is not completed
     * yet. I.e. the user inputs a single character into an edit line and this input
     * must be validated. If {@code isValidInput} returns {@code false},
     * the input character should be cockled.
     *
     * @param text     The text to validate. This must not be {@code null}.
     * @param autoFill If the {@link #getOptions() options} contain
     *                 {@link ValidatorOptions#AUTO_FILL} and autoFill is
     *                 {@code true}, text is filled with special
     *                 characters if necessary. If this is {@code false},
     *                 the text is not automatically filled, even if the option
     *                 {@link ValidatorOptions#AUTO_FILL} is set.
     *
     * @return The result of {@link #doInputValidation}.
     *
     * @throws NullPointerException text is {@code null}.
     */
    public final boolean isValidInput(StringBuilder text, boolean autoFill) {
        Objects.requireNonNull(text, "text");
        return doInputValidation(text, autoFill && hasOption(ValidatorOptions.AUTO_FILL));
    }

    /**
     * Validates the specified text.
     * <p>
     * This calls {@link #validate(String, ValidatorExceptionRef)}.
     *
     * @param input  The text to validate. This must not be {@code null}.
     *
     * @throws ValidatorException input is invalid.
     */
    public final void validate(String input) throws ValidatorException {
        ValidatorExceptionRef er = new ValidatorExceptionRef();

        if (!validate(input, er))
            throw er.getException();
    }

    /**
     * Validates the specified text.
     * <p>
     * This calls {@link #doValidate}.
     *
     * @param input  The text to validate. This must not be {@code null}.
     * @param er     A reference to a possible exception. If this method returns
     *               {@code false}, the encapsulated exception is set to an
     *               instance of {@link ValidatorException}. This must not be
     *               {@code null}.
     *
     * @return {@code true} if input is valid; otherwise, {@code false}
     * is returned.
     *
     * @throws NullPointerException {@code input} or {@code er} is {@code null}.
     *
     * @see ValidatorExceptionRef for an example.
     */
    public final boolean validate(String input, ValidatorExceptionRef er) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(er, "er");

        return doValidate(input, er);
    }

    /**
     * Reformats the specified text.
     * <p>
     * This function is useful if the displayed text is of a format that is
     * painful to edit.
     *
     * @param input  The text to reformat. This must not be {@code null}.
     * @param how    How to format the text. Must be one of the {@link TextFormat}
     *               values. If this is {@link TextFormat#DISPLAY}, the text is
     *               formatted to be displayed in a fine format. If this is
     *               {@link TextFormat#EDIT}, the text is formatted to be an easy
     *               editable text.
     *
     * @return The result of {@link #doFormatText}
     *
     * @throws NullPointerException input is {@code null}.
     */
    public final String formatText(String input, TextFormat how) {
        Objects.requireNonNull(input, "input");
        return doFormatText(input, how);
    }

    /**
     * This implementation does nothing and returns always {@code true}.
     * Inheritors should overwrite this to do a more complex validation.
     *
     * @param text     The text to validate. This is never {@code null}. The text
     *                 must not be modified if autoFill is {@code false}.
     * @param autoFill Indicates whether the text is allowed to be modified.
     *
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    protected boolean doInputValidation(StringBuilder text, boolean autoFill) {
        return true;
    }

    /**
     * This implementation does a basic validation. It checks whether the text
     * is empty if the {@link #getOptions() options} contain
     * {@link ValidatorOptions#NEEDS_INPUT}. If the text contains whitespaces
     * only, the input is invalid, if the {@link #getOptions() options} does
     * not contain {@link ValidatorOptions#BLANKS_VALID}.
     * <p>
     * Inheritors should overwrite this to do a more complex validation.
     *
     * @param text The text to validate. This is never {@code null}. The text
     *             must not be modified if autoFill is {@code false}.
     * @param er   A reference to a possible exception. If this method returns
     *             {@code false}, the encapsulated exception is set to an
     *             instance of {@link ValidatorException}. This must not be
     *             {@code null}.
     *
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    protected boolean doValidate(String text, ValidatorExceptionRef er) {
        er.setException(null);

        if (text.isEmpty()) {
            if (hasOption(ValidatorOptions.NEEDS_INPUT))
                er.setException(new ValidatorException(
                        errorMessage != null ? getErrorMessage() : getRequiredErrorMessage(),
                        Validator.class,
                        this));
        } else if (text.trim().isEmpty() && !hasOption(ValidatorOptions.BLANKS_VALID)) {
            er.setException(new ValidatorException(
                    errorMessage != null ? getErrorMessage() : getBlanksErrorMessage(),
                    Validator.class,
                    this));
        }

        return er.getException() == null;
    }

    /**
     * Called to reformats the specified text.
     * <p>
     * This implementation does nothing and returns text. Inheritors should overwrite
     * this to reformat the text.
     *
     * @param text The text to reformat. This is never {@code null}
     * @param how  How to format the text. Must be one of the {@link TextFormat}
     *             values. If this is {@link TextFormat#DISPLAY}, the text is
     *             formatted to be displayed in a fine format. If this is
     *             {@link TextFormat#EDIT}, the text is formatted to be an easy
     *             editable text.
     *
     * @return This implementation does return text. Inheritors may return a modified
     * text. However, the modified text should still be valid.
     */
    protected String doFormatText(String text, TextFormat how) {
        return text;
    }
}
