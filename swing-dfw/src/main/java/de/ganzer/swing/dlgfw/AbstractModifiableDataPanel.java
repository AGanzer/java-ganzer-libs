package de.ganzer.swing.dlgfw;

import de.ganzer.swing.dialogs.ModifiableDataSupport;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Objects;

/**
 * The base class for all center panels that supports modification of external
 * data.
 *
 * @param <Data> The type of the data to work with.
 */
@SuppressWarnings("unused")
public abstract class AbstractModifiableDataPanel<Data> extends AbstractPanel implements ModifiableDataPanel<Data> {
    private final ModifiableDataSupport<Data> owner;

    /**
     * A document listener that simply sets the modification flag of the
     * owning dialog or frame.
     */
    public class SimpleDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            getOwner().setDataModified(true);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            getOwner().setDataModified(true);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            getOwner().setDataModified(true);
        }
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     *
     * @param owner The owner window that supports modifiable data.
     *
     * @throws NullPointerException {@code owner} is {@code null}.
     */
    public AbstractModifiableDataPanel(ModifiableDataSupport<Data> owner) {
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
     * @param owner The owner window that supports modifiable data.
     *
     * @throws NullPointerException {@code owner} is {@code null}.
     */
    public AbstractModifiableDataPanel(boolean isDoubleBuffered, ModifiableDataSupport<Data> owner) {
        super(isDoubleBuffered);
        Objects.requireNonNull(owner, "owner must not be null.");
        this.owner = owner;
    }

    /**
     * Gets the window that owns this panel.
     *
     * @return The window set at construction.
     */
    public ModifiableDataSupport<Data> getOwner() {
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
