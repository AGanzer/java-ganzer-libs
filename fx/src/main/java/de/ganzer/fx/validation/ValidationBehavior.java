package de.ganzer.fx.validation;

import de.ganzer.core.validation.ValidatorException;

import java.util.function.Consumer;

/**
 * This is used by {@link ValidationTextFormatter} to validate the text of a text
 * field.
 *
 * @see ValidationTextFormatter#validate(ValidationBehavior)
 *
 * @since 5.4.0
 */
public enum ValidationBehavior {
    /**
     * A message box is shown on invalid input and the text field is focused.
     *
     * @see ValidationTextFormatter#setErrorConsumer(Consumer)
     */
    SHOW_MESSAGE_BOX,

    /**
     * Invalid input ist marked by a visual hint to show the validator's error
     * message.
     *
     * @see ValidationTextFormatter#setHintProvider(ValidationHintProvider)
     */
    SET_VISUAL_HINTS,

    /**
     * A {@link ValidatorException} is thrown on invalid input.
     */
    THROW_EXCEPTION,

    /**
     * Validates without any further action. Existing visual hints are removed
     * on valid input nevertheless.
     *
     * @since 5.4.0
     */
    VALIDATION_ONLY
}
