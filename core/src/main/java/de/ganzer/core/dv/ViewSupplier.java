package de.ganzer.core.dv;

/**
 * Used by {@link ViewTemplate} to create a view.
 *
 * @param <D> The type of the document.
 * @param <V> The type of the view.
 *
 * @since 6.0.0
 */
public interface ViewSupplier<D extends Document, V extends View<D>> {
    /**
     * Creates the view from the specified information.
     *
     * @param info The information for creating the view.
     *
     * @return The created view.
     */
    V createView(ViewCreationInfo<D, V> info);
}
