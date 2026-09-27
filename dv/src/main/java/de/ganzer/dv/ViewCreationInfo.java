package de.ganzer.dv;

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
    private final ViewTemplate<D, V> template;
    private final D document;

    /**
     * Creates a new instance.
     *
     * @param template The template that wants to create the view.
     *
     * @throws NullPointerException {@code template} is {@code null}.
     */
    public ViewCreationInfo(ViewTemplate<D, V> template, D document) {
        Objects.requireNonNull(template, "template must not be null.");
        Objects.requireNonNull(document, "document must not be null.");

        this.template = template;
        this.document = document;
    }

    /**
     * Gets the template that has created the information.
     *
     * @return The template.
     */
    public ViewTemplate<D, V> getTemplate() {
        return template;
    }

    /**
     * Gets the document that owns the view.
     *
     * @return The document.
     */
    public D getDocument() {
        return document;
    }
}
