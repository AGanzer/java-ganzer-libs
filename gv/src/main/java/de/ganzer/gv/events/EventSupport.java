package de.ganzer.gv.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class EventSupport<E, L> {
    private final List<L> listeners = new ArrayList<>();

    public void addListener(L listener) {
        Objects.requireNonNull(listener, "listener must not be null.");
        listeners.add(listener);
    }

    public void removeListener(L listener) {
        listeners.remove(listener);
    }

    public void fireEvent(E event) {
        Objects.requireNonNull(event, "event must not be null.");

        for (int i = listeners.size() - 1; i >= 0; i--)
            fireEvent(listeners.get(i), event);
    }

    protected abstract void fireEvent(L listener, E event);
}
