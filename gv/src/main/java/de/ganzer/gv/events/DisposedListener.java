package de.ganzer.gv.events;

/**
 * The interface that should be implemented to listen on disposing events.
 *
 * @param <T> The type of the sender of the event.
 */
public interface DisposedListener<T> {
    /**
     * Called when a disposable object is disposed.
     *
     * @param event The send event.
     */
    void onDisposed(Event<T> event);
}
