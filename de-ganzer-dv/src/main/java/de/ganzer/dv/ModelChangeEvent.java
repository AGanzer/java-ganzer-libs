package de.ganzer.dv;

import java.util.EventObject;
import java.util.Objects;

/**
 * The event that is raised when a model has changed.
 *
 * @since 6.0.0
 */
public class ModelChangeEvent extends EventObject {
    private final String propertyName;
    private final Object oldValue;
    private final Object newValue;

    /**
     * Creates a new instance.
     *
     * @param source The source that raises the event.
     * @param propertyName the name of the changed property.
     * @param oldValue The old value.
     * @param newValue The new value.
     *
     * @throws NullPointerException {@code propertyName} or {@code source} is
     *          {@code null}.
     */
    public ModelChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
        super(source);

        Objects.requireNonNull(source, "source is null.");
        Objects.requireNonNull(propertyName, "propertyName is null.");

        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Gets the name of the changed property.
     *
     * @return The name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Gets the previously set value.
     *
     * @return The old value.
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * Gets the now value.
     *
     * @return The new value.
     */
    public Object getNewValue() {
        return newValue;
    }
}
