package de.ganzer.swing.controls;

import java.util.EventListener;

/**
 * Listener interface for tab-changing events.
 */
public interface TabChangingListener extends EventListener {
    /**
     * Called when a tab is about to change.
     *
     * @param e The event object.
     */
    void tabChanging(TabChangingEvent e);
}
