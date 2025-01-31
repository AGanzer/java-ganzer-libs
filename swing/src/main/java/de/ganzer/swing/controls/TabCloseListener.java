package de.ganzer.swing.controls;

import java.util.EventListener;

/**
 * The listener used to inform clients that a closable tab page is about to be
 * closed.
 *
 * @see ClosableTabbedPane
 */
public interface TabCloseListener extends EventListener {
    /**
     * Invoked when the close button of a closeable tab page is pressed.
     *
     * @param index The zero-based index of the page that's button was clicked.
     */
    void closeTabPerformed(int index);
}
