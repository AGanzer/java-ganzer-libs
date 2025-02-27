package de.ganzer.gv.events;

/**
 * The interface that should be implemented to listen on disposing events.
 *
 * @param <S> The type of the sender of the event.
 */
public interface PropertyChangedListener<S> {
    /**
     * Called when a property has changed.
     *
     * @param event The event.
     */
    void onPropertyChanged(PropertyChangedEvent<S> event);
}
