package de.ganzer.swing.dlgfw;

/**
 * May be implemented by content panes that work on modifiable data.
 *
 * @param <Data> The type of the data to work with.
 */
public interface ModifiableDataPanel<Data> extends Initializer<Data> {
    /**
     * Called to validate the user's input.
     *
     * @return {@code true} if the input is valid; otherwise, {@code false}.
     */
    boolean validateInput();

    /**
     * Called to update the data with the user's input.
     *
     * @param data The data to update.
     */
    void updateData(Data data);
}
