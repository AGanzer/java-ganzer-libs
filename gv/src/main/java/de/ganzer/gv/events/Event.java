package de.ganzer.gv.events;

/**
 * The basic event of all events.
 *
 * @param <T> The type of the sender of the event.
 */
public class Event<T> {
    private final T sender;

    /**
     * Creates a new instance.
     *
     * @param sender The sender of the event.
     */
    public Event(T sender) {
        this.sender = sender;
    }

    /**
     * Gets the sender of the event.
     *
     * @return The sender.
     */
    public T getSender() {
        return sender;
    }
}
