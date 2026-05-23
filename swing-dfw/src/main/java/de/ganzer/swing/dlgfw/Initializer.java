package de.ganzer.swing.dlgfw;

/**
 * Can be implemented by UI elements that supports initialization of its
 * controls.
 *
 * @param <Data> The type of the data to support.
 */
public interface Initializer<Data> {
    /**
     * Called to initialize the controls from the specified data.
     *
     * @param data The data where to initialize the controls with.
     */
    void initControls(Data data);
}
