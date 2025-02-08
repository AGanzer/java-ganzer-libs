package de.ganzer.swing.dialogs;

import java.awt.event.WindowEvent;

/**
 * An interface that should be implemented by windows that provide information
 * about how the window was closed.
 */
public interface EscapableWindow {
    /**
     * Gets a value that indicates whether the window is closed by the Esc key
     * or by the window's Close button or the Close menu or by a Cancel button.
     *
     * @return {@code false} if the window is closed by an OK button and
     *         {@code true} if the window is closed in any other way.
     */
    boolean isEscaped();

    /**
     * Closes the window.
     *
     * @param escaped Indicates whether the window is closed by the Esc key or
     *        by the window's Close button or the Close menu or by a Cancel
     *        button.
     */
    void closeWindow(boolean escaped);
}
