package de.ganzer.swing.actions;

import java.util.EventListener;

/**
 * The listener that is called when an action is selected (or the last action
 * is deselected) within a {@link GToggleActionGroup}.
 * <p>
 * The class that is interested in processing an {@link GToggleActionGroup}
 * event implements this interface, and registers itself with an action, using
 * {@link GToggleActionGroup#addSelectedActionChangedListener} method. When the
 * action event occurs, that object's {@link #selectedActionChanged} method is invoked.
 *
 * @see GAction
 * @see GSelectedActionChangedEvent
 */
public interface GSelectedActionChangedListener extends EventListener {
    /**
     * Invoked when an {@link GToggleActionGroup} event occurs.
     *
     * @param event the event to be processed
     */
    void selectedActionChanged(GSelectedActionChangedEvent event);
}
