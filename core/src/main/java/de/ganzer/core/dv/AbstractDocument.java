package de.ganzer.core.dv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A basic abstract document that implements the {@link Document} interface.
 * <p>
 * A document of this type can only be created with an instance of
 * {@link DocumentCreationInfo}.
 *
 * @since 5.6.0
 */
public abstract class AbstractDocument extends AbstractModel implements Document {
    private final DocumentTemplate<? extends Document> documentTemplate;
    private final List<DocumentView<? extends Document>> openViews = new ArrayList<>();

    /**
     * Adss an open view to the document.
     * <p>
     * <b>NOTE:</b> This is invoked automatically after a view is created and
     * should never be called by any client code.
     *
     * @param view The view to add.
     *
     * @throws IllegalArgumentException If the view is already added to a
     *         document.
     */
    @Override
    public void addView(DocumentView<? extends Document> view) {
        if (view.getDocument() == this)
            throw new IllegalArgumentException("View is already added to a document.");

        openViews.add(view);
    }

    /**
     * Removes an open view from the document.
     * <p>
     * <b>NOTE:</b> This should always be invoked by a view that implements
     * {@link DocumentView} when the view is closed.
     *
     * @param view The view to remove.
     */
    @Override
    public void removeView(DocumentView<? extends Document> view) {
        openViews.remove(view);
        view.setDocument(null);
    }

    /**
     * Gets the views of the document.
     *
     * @return An unmodifiable list of the views of the document.
     */
    @Override
    public List<DocumentView<?>> getViews() {
        return Collections.unmodifiableList(openViews);
    }

    /**
     * Creates a new instance.
     * <p>
     * {@link #doCreateData()} is invoked if {@link DocumentCreationInfo#isNewData()}
     * is {@code true}; otherwise, {@link #doLoadData()} is invoked.
     *
     * @param info The information for initializing the model.
     */
    protected AbstractDocument(DocumentCreationInfo<? extends Document> info) {
        super(info.getName(), info.isReadOnly(), info.isNewData());
        this.documentTemplate = info.getTemplate();
    }

    /**
     * Gets the template that has created the document.
     *
     * @return The template that has created the document.
     */
    @Override
    public DocumentTemplate<? extends Document> getDocumentTemplate() {
        return documentTemplate;
    }
}
