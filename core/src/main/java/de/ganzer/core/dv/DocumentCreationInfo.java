package de.ganzer.core.dv;

import de.ganzer.core.util.Strings;

import java.util.Objects;

/**
 * Holds the information that is used to create a model that is derived from
 * {@link AbstractDocument}.
 *
 * @param <D> The type of the document.
 */
public class DocumentCreationInfo<D extends Document> {
    private final DocumentTemplate<D> template;
    private final Document parent;
    private final String name;
    private final boolean readOnly;
    private final boolean newData;

    /**
     * Creates a new instance.
     *
     * @param template The template that wants to create the model.
     * @param parent The parent document or {@code null} if there is no parent.
     * @param name The name of the model to set.
     * @param newData Indicates whether the model shall create new data.
     * @param readOnly Indicates whether the model is read-only.
     *
     * @throws NullPointerException {@code template} is {@code null}.
     * @throws IllegalArgumentException if {@code newData} is {@code false} and
     *         {@code name} is {@code null} or empty or does contain only blanks.
     *
     * @since 6.0.0
     */
    public DocumentCreationInfo(DocumentTemplate<D> template, Document parent, String name, boolean readOnly, boolean newData) {
        this.parent = parent;
        Objects.requireNonNull(template, "template must not be null.");

        if (newData && Strings.isNullOrBlank(name))
            throw new IllegalArgumentException("name is null or blank");

        this.template = template;
        this.name = name;
        this.readOnly = readOnly;
        this.newData = newData;
    }

    /**
     * Gets the templates that has created the information.
     *
     * @return The template.
     */
    public DocumentTemplate<D> getTemplate() {
        return template;
    }

    /**
     * Gets the parent document.
     *
     * @return The parent document or {@code null} if there is no parent.
     */
    public Document getParent() {
        return parent;
    }

    /**
     * Gets the name to set to the model.
     *
     * @return The name or {@code null} if this is not set.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the read-only indicator to set.
     *
     * @return The indicator to set.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Gets the new data indicator to set.
     *
     * @return The indicator to set.
     */
    public boolean isNewData() {
        return newData;
    }
}
