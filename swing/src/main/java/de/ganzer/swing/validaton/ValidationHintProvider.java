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
}
