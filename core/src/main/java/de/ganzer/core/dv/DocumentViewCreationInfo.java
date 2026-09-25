package de.ganzer.core.dv;

import java.util.Objects;

/**
 * Holds the information used to create a view that provides template
 * information.
 *
 * @param <V> The type of the view.
 * @param <D> The type of the view's presenter.
 *
 * @since 5.6.0
 */
public class DocumentViewCreationInfo<D extends Document, V extends DocumentView<D>> {
    private final DocumentViewTemplate<?, V> template;

    /**
     * Creates a new instance.
     *
     * @param template The template that wants to create the view.
     *
     * @throws NullPointerException {@code template} is {@code null}.
     */
    public DocumentViewCreationInfo(DocumentViewTemplate<?, V> template) {
        Objects.requireNonNull(template, "template must not be null.");
        this.template = template;
    }

    /**
     * Gets the template that has created the information.
     *
     * @return The template.
     */
    public DocumentViewTemplate<?, V> getTemplate() {
        return template;
    }
}
