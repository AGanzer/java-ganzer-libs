package de.ganzer.gv.events;

public class PropertyChangedSupport<S> extends EventSupport<PropertyChangedEvent<S>, PropertyChangedListener<S>> {
    @Override
    protected void fireEvent(PropertyChangedListener<S> listener, PropertyChangedEvent<S> event) {
        listener.onPropertyChanged(event);
    }
}
