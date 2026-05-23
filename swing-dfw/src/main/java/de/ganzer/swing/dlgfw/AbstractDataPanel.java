package de.ganzer.swing.dlgfw;

import de.ganzer.swing.dialogs.DataSupport;
import de.ganzer.swing.dialogs.ModifiableDataSupport;

import java.util.Objects;

/**
 * The base class for all center panels that accepts external data.
 *
 * @param <Data> The type of the data to work with.
 */
@SuppressWarnings("unused")
public abstract  class AbstractDataPanel<Data> extends AbstractPanel implements Initializer<Data> {
    private final DataSupport<Data> owner;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     *
     * @param owner The owner window that supports data.
     *
     * @throws NullPointerException {@code owner} is {@code null}.
     */
    public AbstractDataPanel(DataSupport<Data> owner) {
        Objects.requireNonNull(owner, "owner must not be null.");
        this.owner = owner;
    }

    /**
     * Creates a new <code>JPanel</code> with <code>FlowLayout</code>
     * and the specified buffering strategy.
     * If <code>isDoubleBuffered</code> is true, the <code>JPanel</code>
     * will use a double buffer.
     *
     * @param isDoubleBuffered a boolean, true for double-buffering, which
     *         uses additional memory space to achieve fast, flicker-free
     *         updates
     * @param owner The owner window that supports data.
     *
     * @throws NullPointerException {@code owner} is {@code null}.
     */
    public AbstractDataPanel(boolean isDoubleBuffered, ModifiableDataSupport<Data> owner) {
        super(isDoubleBuffered);
        Objects.requireNonNull(owner, "owner must not be null.");
        this.owner = owner;
    }

    /**
     * Gets the window that owns this panel.
     *
     * @return The window set at construction.
     */
    public DataSupport<Data> getOwner() {
        return owner;
    }

    /**
     * Gets the data of the window that owns this panel.
     *
     * @return The data of window set at construction or {@code null} if the
     *         owner's data is.
     */
    public Data getData() {
        return owner != null ? owner.getData() : null;
    }
}
