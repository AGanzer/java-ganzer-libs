package de.ganzer.core.dv;

import java.util.Objects;

/**
 * Holds the information used to create a view that provides template
 * information.
 *
 * @param <V> The type of the view.
 * @param <D> The type of the view's presenter.
 *
 * @since 6.0.0
 */
public class ViewCreationInfo<D extends Document, V extends View<D>> {
    private final ViewTemplate<?, V> template;

    /**
     * Creates a new instance.
     *
     * @param template The template that wants to create the view.
     *
     * @throws NullPointerException {@code template} is {@code null}.
     */
    public ViewCreationInfo(ViewTemplate<?, V> template) {
        Objects.requireNonNull(template, "template must not be null.");
        this.template = template;
    }

    /**
     * Gets the template that has created the information.
     *
     * @return The template.
     */
    public ViewTemplate<?, V> getTemplate() {
        return template;
    }
}
