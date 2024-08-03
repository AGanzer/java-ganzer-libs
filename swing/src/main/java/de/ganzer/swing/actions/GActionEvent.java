package de.ganzer.swing.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * This event ist used by {@link GAction} instances to notify that an action is
 * performed.
 */
public class GActionEvent {
    private final GAction source;
    private final String actionCommand;
    private final ActionEvent origin;

    /**
     * Creates a new event from the specified arguments.
     *
     * @param source The sending Action of the event.
     * @param actionCommand The actions command of the sending action.
     * @param origin The original action event, send by a button or menu item.
     *
     * @throws NullPointerException {@code sender} or {@code origin} is
     *         {@code null}.
     */
    public GActionEvent(GAction source, String actionCommand, ActionEvent origin) {
        Objects.requireNonNull(source, "sender must not be null.");
        Objects.requireNonNull(origin, "origin must not be null.");

        this.source = source;
        this.actionCommand = actionCommand;
        this.origin = origin;
    }

    /**
     * Gets the sending action.
     *
     * @return The sending action.
     */
    public GAction getSource() {
        return source;
    }

    /**
     * Gets the sending action's command.
     *
     * @return The sending action's command or {@code null} if no command was
     *         set.
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * Gets the original action event, send by a button or menu item.
     *
     * @return The original event.
     */
    public ActionEvent getOrigin() {
        return origin;
    }
}
