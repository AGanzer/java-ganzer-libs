package de.ganzer.gv.events;

/**
 * The basic event of all events.
 *
 * @param <S> The type of the sender of the event.
 */
public class Event<S> {
    private final S sender;

    /**
     * Creates a new instance.
     *
     * @param sender The sender of the event.
     */
    public Event(S sender) {
        this.sender = sender;
    }

    /**
     * Gets the sender of the event.
     *
     * @return The sender.
     */
    public S getSender() {
        return sender;
    }
}
