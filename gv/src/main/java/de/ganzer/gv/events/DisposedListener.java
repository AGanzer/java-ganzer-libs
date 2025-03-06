package de.ganzer.gv.events;

/**
 * The interface that should be implemented to listen on disposing events.
 *
 * @param <S> The type of the sender of the event.
 */
public interface DisposedListener<S> {
    /**
     * Called when a disposable object is disposed.
     *
     * @param event The event.
     */
    void onDisposed(Event<S> event);
}
