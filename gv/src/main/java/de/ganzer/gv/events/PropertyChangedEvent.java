package de.ganzer.gv.events;

import java.util.Objects;

/**
 * The event that is used when a property has changed.
 *
 * @param <S> The type of the sender.
 */
public class PropertyChangedEvent<S> extends Event<S> {
    /**
     * The name of the property.
     */
    public final String propertyName;

    /**
     * Creates a new instance.
     *
     * @param sender The sender of the event.
     * @param propertyName The name of the property.
     *
     * @throws NullPointerException {@code propertyName} is {@code null}.
     */
    public PropertyChangedEvent(S sender, String propertyName) {
        super(sender);

        Objects.requireNonNull(propertyName, "propertyName must not be null.");
        this.propertyName = propertyName;
    }
}
