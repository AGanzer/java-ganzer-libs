package de.ganzer.dv;

import de.ganzer.core.Pair;

import java.util.*;

/**
 * The same as {@code PropertyChangeSupport} but for implementors of the
 * {@link Model} interface. The difference ist that the originator of a change
 * event ist not notified.
 *
 * @since 6.0.0
 */
public class ModelChangeSupport {
    private final Map<String, Set<Pair<Object, ModelChangeListener>>> listeners = new HashMap<>();
    private final Object source;

    /**
     * Creates a new instance.
     *
     * @param source The source for all events that are raised by this object.
     *
     * @throws NullPointerException {@code source} is {@code null}.
     */
    public ModelChangeSupport(Object source) {
        Objects.requireNonNull(source, "source must not be null.");
        this.source = source;
    }

    /**
     * Adds a listener for the specified property.
     *
     * @param propertyName The name of the property to add the listener fo or
     *         {@code null} or an empty string to listen to all properties.
     * @param listener The listener to install.
     * @param originator The originator that installs the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    public void addChangeListener(String propertyName, ModelChangeListener listener, Object originator) {
        Objects.requireNonNull(originator, "originator must not be null.");
        Objects.requireNonNull(listener, "listener must not be null.");

        var name = propertyName != null ? propertyName : "";

        listeners.putIfAbsent(name, new HashSet<>());
        listeners.get(name).add(new Pair<>(originator, listener));
    }

    /**
     * Removes an installed listener for the specified property.
     *
     * @param propertyName The name of the property to add the listener fo or
     *         {@code null} or an empty string for listeners that are installed
     *         for all properties.
     * @param listener The listener to install.
     * @param originator The originator that has installed the event.
     *
     * @throws NullPointerException {@code originator} or {@code listener} is
     *          {@code null}.
     */
    public void removeChangeListener(String propertyName, ModelChangeListener listener, Object originator) {
        Objects.requireNonNull(originator, "originator must not be null.");
        Objects.requireNonNull(listener, "listener must not be null.");

        var name = propertyName != null ? propertyName : "";
        var set = listeners.get(name);

        if (set != null)
            set.remove(new Pair<>(originator, listener));
    }

    /**
     * Fires a change event for the specified property.
     *
     * @param propertyName The property where to fire the change event for or
     *         {@code null} or an empty string if no property is specified.
     * @param originator The originator that causes the event. This is not
     *         notified of the change. This should be {@code null} to notify
     *         all listeners.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    public void fireChange(String propertyName, Object originator, Object oldValue, Object newValue) {
        fireChange(originator, new ModelChangeEvent(source, propertyName != null ? propertyName : "", oldValue, newValue));
    }

    /**
     * Fires a change event for the specified property.
     *
     * @param propertyName The property where to fire the change event for or
     *         {@code null} or an empty string if no property is specified.
     *
     * @param originator The originator that causes the event. This is not
     *         notified of the change. This should be {@code null} to notify
     *         all listeners.
     * @param index The index of the changed item.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    public void fireIndexedChange(String propertyName, Object originator, int index, Object oldValue, Object newValue) {
        fireChange(originator, new IndexedModelChangeEvent(source, propertyName != null ? propertyName : "", oldValue, newValue, index));
    }

    /**
     * Fires a change event with the specified event.
     *
     * @param originator The originator that causes the event. This is not
     *         notified of the change. This should be {@code null} to notify
     *         all listeners.
     * @param event the event to fire.
     *
     * @throws NullPointerException {@code event} is {@code null}.
     */
    public void fireChange(Object originator, ModelChangeEvent event) {
        Objects.requireNonNull(event, "event is null.");

        if (event.getPropertyName().isEmpty())
            fireChange(originator, event, listeners.get(""));
        else {
            var namedSet = listeners.get(event.getPropertyName());
            var unnamedSet = listeners.get("");

            if (namedSet != null)
                fireChange(originator, event, namedSet);

            if (unnamedSet != null)
                fireChange(originator, event, unnamedSet);
        }
    }

    /**
     * Invoked by {@link #fireChange(Object, ModelChangeEvent)} ti fire the
     *          specified event to the given listeners.
     *
     * @param originator The originator that causes the event.
     * @param event The event to fire.
     * @param listeners The listeners to notify.
     */
    protected void fireChange(Object originator, ModelChangeEvent event, Set<Pair<Object, ModelChangeListener>> listeners) {
        for (var pair : listeners) {
            if (pair.getFirst() != originator)
                pair.getSecond().modelChanged(event);
        }
    }
}
