package de.ganzer.core.validation;

/**
 * The ValidatorOptions defines the options that can be used with
 * {@link Validator} classes.
 */
public class ValidatorOptions {
    /**
     * No option.
     */
    public static final int NONE = 0x00;

    /**
     * An empty string is invalid.
     */
    public static final int NEEDS_INPUT = 0x01;

    /**
     * If the text to validate contains whitespaces only the text is considered
     * as not empty. If this option is not set, the text to validate is invalid
     * if it contains whitespaces only and the option {@link #NEEDS_INPUT} is set.
     */
    public static final int BLANKS_VALID = 0x02;

    /**
     * Input text is automatically filled with special characters.
     */
    public static final int  AUTO_FILL = 0x04;
}
