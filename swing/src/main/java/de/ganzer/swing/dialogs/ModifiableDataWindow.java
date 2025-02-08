package de.ganzer.swing.dialogs;

/**
 * This interface should be implemented by windows that are able to modify data.
 *
 * @param <Data> The type of the modifiable data.
 *
 * @see AbstractModifiableDialog
 */
public interface ModifiableDataWindow<Data> extends EscapableWindow, DataSupport<Data> {
    /**
     * Gets the data that was applied by {@link #initControls}
     *
     * @return The data that ws given to {@link #initControls} or {@code null}
     *         if no data was specified.
     */
    Data getData();

    /**
     * Invokes the set data consumer if the data is valid.
     *
     * @return {@code true} if the data is valid and the consumer is invoked;
     *         otherwise, {@code false}.
     */
    boolean applyChangedData();
}
