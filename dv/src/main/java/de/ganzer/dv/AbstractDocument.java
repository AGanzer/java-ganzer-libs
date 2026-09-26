package de.ganzer.dv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A basic abstract document that implements the {@link Document} interface.
 * <p>
 * A document of this type can only be created with an instance of
 * {@link DocumentCreationInfo}.
 *
 * @since 6.0.0
 */
public abstract class AbstractDocument extends AbstractModel implements Document {
    private final DocumentTemplate<? extends Document> template;
    private final List<View<? extends Document>> views = new ArrayList<>();

    private boolean closed;

    /**
     * Gets the template that has created the document.
     *
     * @return The template that has created the document.
     */
    @Override
    public DocumentTemplate<? extends Document> getTemplate() {
        return template;
    }

    /**
     * Adss a view to the document.
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
    public void addView(View<? extends Document> view) {
        if (view.getDocument() == this)
            throw new IllegalArgumentException("View is already added to a document.");

        views.add(view);
    }

    /**
     * Removes a view from the document.
     * <p>
     * If {@link DocumentTemplate#isAutoClose()} of the document's template is
     * {@code true}, the document will be closed automatically.
     * <p>
     * <b>NOTE:</b> This should always be invoked by a view that implements
     * {@link View} when the view is closed (closed in the sense of
     * destroyed but not just hidden to re-show it later).
     * <p>
     * Implementors should ensure that {@code view.setDocument(null)} is
     * invoked.
     *
     * @param view The view to remove.
     */
    @Override
    public void removeView(View<? extends Document> view) {
        views.remove(view);
        view.setDocument(null);

        if (view.getTemplate().isMandatory() || getTemplate().isAutoClose() && views.isEmpty())
            close();
    }

    /**
     * Gets the views of the document.
     *
     * @return An unmodifiable list of the views of the document.
     */
    @Override
    public List<View<? extends Document>> getViews() {
        return Collections.unmodifiableList(views);
    }

    /**
     * Writes the data into a file, a database, or any other target.
     * <p>
     * This resets the modification, the read-only, and the new data flags.
     *
     * @throws DVSaveException on any error.
     *
     * @see #doSaveData()
     */
    @Override
    public void saveData() throws DVSaveException {
        if (isNewData())
            saveDataAs();
        else
            super.saveData();
    }

    /**
     * Closes the document with all its open views and without any further action.
     * <p>
     * To ensure that the document saves all modified data, invoke
     * {@link #canClose()} before invoking this method.
     * <p>
     * <b>NOTE:</b> Inheritors have to ensure that the base method is invoked
     * to remove the document from the manager's document list.
     */
    @Override
    public void close() {
        closed = true;
        DVManager.documentClosed(this);

        views.forEach(View::forceClose);
    }

    /**
     * Gets a value indicating whether the document is closed.
     *
     * @return {@code true} if the document is closed.
     */
    @Override
    public boolean isClosed() {
        return closed;
    }

    /**
     * Creates a new instance.
     * <p>
     * {@link #doCreateData()} is invoked if {@link DocumentCreationInfo#isNewData()}
     * is {@code true}; otherwise, {@link #doLoadData()} is invoked.
     *
     * @param info The information for initializing the model.
     *
     * @throws IOException on any error loading data.
     */
    protected AbstractDocument(DocumentCreationInfo<? extends Document> info) throws IOException {
        super(info.getName(), info.isReadOnly(), info.isNewData());
        this.template = info.getTemplate();
    }
}
