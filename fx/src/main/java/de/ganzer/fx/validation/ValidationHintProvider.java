package de.ganzer.fx.validation;

import de.ganzer.core.validation.ValidatorException;
import javafx.scene.control.TextInputControl;

/**
 * This is used by {@link ValidationTextFormatter} to set visual hints that inform the
 * user about invalid input.
 *
 * @since 5.4.0
 */
public interface ValidationHintProvider {
    /**
     * Called to show the visual hints.
     *
     * @param target The target control where to show the hints.
     * @param e The exception that causes the error.
     */
    void showHints(TextInputControl target, ValidatorException e);

    /**
     * Called to hide the visual hints.
     *
     * @param target The target control where to hide the hints.
     */
    void hideHints(TextInputControl target);

    /**
     * Called if a visual hint is already shown but the message has changed.
     * <p>
     * Inheritors that does not use the exception's message, can ignore this.
     *
     * @param target The target control where to show the hints.
     * @param e The exception that causes the error.
     *
     * @since 5.4.0
     */
    void updateHints(TextInputControl target, ValidatorException e);
}
