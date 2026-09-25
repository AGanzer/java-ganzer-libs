package de.ganzer.core.dv;

/**
 * A basic abstract document that implements the {@link Document} interface.
 * <p>
 * A document of this type can only be created with an instance of
 * {@link DocumentCreationInfo}.
 *
 * @param <D> The type of the document.
 *
 * @since 5.6.0
 */
public abstract class AbstractDocument<D extends Document> extends AbstractModel implements Document {
    private final DocumentTemplate<D> documentTemplate;

    /**
     * Creates a new instance.
     * <p>
     * {@link #doCreateData()} is invoked if {@link DocumentCreationInfo#isNewData()}
     * is {@code true}; otherwise, {@link #doLoadData()} is invoked.
     *
     * @param info The information for initializing the model.
     */
    protected AbstractDocument(DocumentCreationInfo<D> info) {
        super(info.getName(), info.isReadOnly(), info.isNewData());
        this.documentTemplate = info.getTemplate();
    }

    /**
     * Gets the template that has created the document.
     *
     * @return The template that has created the document.
     */
    @Override
    public DocumentTemplate<D> getDocumentTemplate() {
        return documentTemplate;
    }
}
