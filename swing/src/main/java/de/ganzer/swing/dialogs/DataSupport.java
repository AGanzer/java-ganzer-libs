package de.ganzer.swing.dialogs;

/**
 * An interface that should be implemented by dialogs or frames that accepts
 * initial data.
 *
 * @param <Data> The type of the accepted data.
 */
public interface DataSupport<Data> {
    /**
     * Gets the data that was applied by {@link #initControls}
     *
     * @return The data that ws given to {@link #initControls} or {@code null}
     *         if no data was specified.
     */
    Data getData();

    /**
     * Called to apply the initial data to the controls.
     * <p>
     * Implementors should store this data in a local variable to modify it when
     * the dialog or frame is closed.
     *
     * @param data The data to apply.
     */
    void initControls(Data data);
}
