package de.ganzer.swing.dlgfw;

import java.awt.event.ActionEvent;

/**
 * The event that is used to query a window to close itself.
 */
public class QueryCloseEvent extends ActionEvent {
    private final boolean applyData;

    /**
     * Creates a new instance.
     *
     * @param source The object that originated the event.
     * @param applyData Indicates whether a dialog shall not set its "escaped"
     *        indicator. This is ignored in frames.
     *
     * @throws IllegalArgumentException if {@code source} is {@code null}.
     */
    public QueryCloseEvent(Object source, boolean applyData) {
        super(source, ActionEvent.ACTION_FIRST, null);
        this.applyData = applyData;
    }

    /**
     * Indicates whether a dialog shall set its "escaped" indicator.
     *
     * @return {@code false} to close the dialog as the user would have pressed
     *         the [Esc] key.
     */
    public boolean shouldApplyData() {
        return applyData;
    }
}
