package de.ganzer.core.validation;

/**
 * The TextFormat enumeration defines the formatting types of text.
 * <p>
 * These values are used by {@link Validator#formatText} only.
 */
public enum TextFormat {
    /**
     * Text must be formatted to display it in a more readable way.
     * However, the text must still be valid for the validator.
     */
    DISPLAY,

    /**
     * Text must be formatted to edit it in a more easy way.
     * However, the text must still be valid for the validator.
     */
    EDIT
}
