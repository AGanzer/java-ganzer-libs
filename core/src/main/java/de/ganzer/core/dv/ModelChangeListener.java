package de.ganzer.core.dv;

/**
 * Interface to an object that handles property changes of a model.
 *
 * @since 5.6.0
 */
public interface ModelChangeListener {
    /**
     * Called when a model's propert has changed.
     *
     * @param e The event that contains the change information.
     */
    void modelChanged(ModelChangeEvent e);
}
