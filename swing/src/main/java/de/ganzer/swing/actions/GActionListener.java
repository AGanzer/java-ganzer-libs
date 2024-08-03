package de.ganzer.swing.actions;

import java.util.EventListener;

/**
 * The listener interface for receiving {@link GAction} events.
 * <p>
 * The class that is interested in processing an {@link GAction} event
 * implements this interface, and registers itself with an action, using
 * {@link GAction#addActionListener} method. When the action event occurs,
 * that object's {@link #actionPerformed} method is invoked.
 *
 * @see GAction
 * @see GActionEvent
 */
public interface GActionListener extends EventListener {
    /**
     * Invoked when an {@link GAction} event occurs.
     *
     * @param event the event to be processed
     */
    void actionPerformed(GActionEvent event);
}
