package de.ganzer.gv.events;

public class DisposedSupport<S> extends EventSupport<Event<S>, DisposedListener<S>> {
    @Override
    protected void fireEvent(DisposedListener<S> listener, Event<S> event) {
        listener.onDisposed(event);
    }
}
