package de.ganzer.core.dv;

/**
 * A change event for indexed properties.
 *
 * @since 5.6.0
 */
public class IndexedModelChangeEvent extends ModelChangeEvent{
    private final int index;

    /**
     * Creates a new instance.
     *
     * @param source The source that raises the event.
     * @param propertyName the name of the changed property.
     * @param oldValue The old value.
     * @param newValue The new value.
     * @param index The index of the changed item.
     *
     * @throws NullPointerException {@code propertyName} or {@code source} is
     *         {@code null}.
     */
    public IndexedModelChangeEvent(Object source, String propertyName, Object oldValue, Object newValue, int index) {
        super(source, propertyName, oldValue, newValue);
        this.index = index;
    }

    /**
     * Gets the index of the changed item.
     *
     * @return The index.
     */
    public int getIndex() {
        return index;
    }
}
