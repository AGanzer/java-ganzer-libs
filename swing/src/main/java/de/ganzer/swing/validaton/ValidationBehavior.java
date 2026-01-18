package de.ganzer.swing.validaton;

import de.ganzer.core.validation.ValidatorException;

import java.util.function.Consumer;

/**
 * This is used by {@link ValidationFilter} to validate the text of a text
 * field.
 *
 * @see ValidationFilter#validate(ValidationBehavior)
 */
public enum ValidationBehavior {
    /**
     * A message box is shown on invalid input and the text field is focused.
     *
     * @see ValidationFilter#setErrorConsumer(Consumer)
     */
    SHOW_MESSAGE_BOX,

    /**
     * Invalid input ist marked by a visual hint to show the validator's error
     * message.
     *
     * @see ValidationFilter#setHintProvider(ValidationHintProvider)
     */
    SET_VISUAL_HINTS,

    /**
     * A {@link ValidatorException} is thrown on invalid input.
     */
    THROW_EXCEPTION,

    /**
     * Validates without any further action. Existing visual hints are removed
     * on valid input nevertheless.
     */
    VALIDATION_ONLY
}
