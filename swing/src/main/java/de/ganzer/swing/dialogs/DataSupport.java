package de.ganzer.swing.dialogs;

/**
 * An interface that should be implemented by dialogs or frames that accepts
 * initial data.
 *
 * @param <Data> The type of the accepted data.
 */
public interface DataSupport<Data> {
    /**
     * Gets the data.
     *
     * @return The data or {@code null} if no data is available.
     */
    Data getData();
}
