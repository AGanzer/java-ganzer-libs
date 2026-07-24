package de.ganzer.swing.dlgfw;

import java.awt.event.ActionEvent;

/**
 * The event that is used to query a window to close itself.
 */
public class QueryCloseEvent extends ActionEvent {
    /**
     * The command used to indicate that modified data should be applied.
     */
    public static final String APPLY_DATA_COMMAND = "apply-data";
    /**
     * The command used to indicate that modified data should not be applied.
     */
    public static final String ESCAPE_DIALOG_COMMAND = "escape-dialog";

    /**
     * Creates a new instance.
     *
     * @param source The object that originated the event.
     * @param applyData Indicates whether a dialog shall apply modified data.
     *         This is ignored in frames.
     *
     * @throws IllegalArgumentException if {@code source} is {@code null}.
     */
    public QueryCloseEvent(Object source, boolean applyData) {
        super(source, ActionEvent.ACTION_FIRST, applyData ? APPLY_DATA_COMMAND : ESCAPE_DIALOG_COMMAND);
    }
}
