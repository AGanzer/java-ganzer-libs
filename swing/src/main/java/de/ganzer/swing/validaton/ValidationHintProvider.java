package de.ganzer.swing.validaton;

import de.ganzer.core.validation.ValidatorException;

import javax.swing.text.JTextComponent;

/**
 * This is used by {@link ValidationFilter} to set visual hints that inform the
 * user about invalid input.
 */
public interface ValidationHintProvider {
    /**
     * Called to show the visual hints.
     *
     * @param target The target text field where to show the hints.
     * @param e The exception that causes the error.
     */
    void showHints(JTextComponent target, ValidatorException e);

    /**
     * Called to hide the visual hints.
     *
     * @param target The target text field where to hide the hints.
     */
    void hideHints(JTextComponent target);

    /**
     * Called if a visual hint is already shown but the message has changed.
     * <p>
     * Inheritors that does not use the exception's message, can ignore this.
     *
     * @param target The target text field where to show the hints.
     * @param e The exception that causes the error.
     *
     * @since 5.4.0
     */
    void updateHints(JTextComponent target, ValidatorException e);
}
