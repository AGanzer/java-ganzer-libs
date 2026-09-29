package de.ganzer.dv;

import de.ganzer.core.Services;
import de.ganzer.dv.services.DVNavigationService;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A basic abstract document that is created with template information.
 * <p>
 * Other than a basic model, a document is always file- or stream-based and is
 * usually able to load data and writes it into another target.
 * <p>
 * For a more easy creation of documents and their possible views, document types
 * can be registered by {@link DVManager#registerDocumentTemplate}.
 * <p>
 * The following example shows how a template is created that simply creates a
 * static view that cannot be edited and that cannot be closed:
 * <pre>{@code
 * var tpl = new DocumentTemplate<>(
 *         "Welcome",
 *         s -> false,
 *         WelcomeDocument::new,
 *         "Welcome",
 *         DocumentTemplate.NO_NEW_NUMBER | DocumentTemplate.IS_HIDDEN,
 *         null);
 * tpl.registerViewTemplate(new ViewTemplate<>(
 *         "Welcome",
 *         WelcomePanel::new,
 *         v -> getMainView().addChildView(v),
 *         ViewTemplate.NOT_CLOSABLE,
 *         null,
 *         null));
 * DVManager.registerDocumentTemplate(tpl);
 * }</pre>
 * This interface provides child documents, but the implementation itself is
 * responsive for managing child documents with all its actions.
 * <p>
 * Other than a model, a document does implement all logic that is necessary to
 * manage its children, its data, and its views, in conjunction with the
 * {@link DVManager}.
 *
 * @since 6.0.0
 */
public abstract class Document extends Model {
    private final DocumentTemplate<? extends Document> template;
    private final Document parent;
    private final List<View<? extends Document>> views = new ArrayList<>();
    private final List<Document> children = new ArrayList<>();

    private boolean closed;

    /**
     * Creates a new instance.
     * <p>
     * {@link #doCreateData()} is invoked if {@link DocumentCreationInfo#isNewData()}
     * is {@code true}; otherwise, {@link #doLoadData()} is invoked.
     *
     * @param info The information for initializing the model.
     *
     * @throws DVLoadException on any error loading data.
     */
    protected Document(DocumentCreationInfo<? extends Document> info) throws DVLoadException {
        super(info.getName(), info.isReadOnly(), info.isNewData());
        template = info.getTemplate();
        parent = info.getParent();

        if (parent != null)
            parent.addChild(this);
    }

    /**
     * Gets the parent document.
     *
     * @return The parent document or {@code null} if there is no parent. This
     *          implementation does always return {@code null}.
     */
    public final Document getParent() {
        return parent;
    }

    /**
     * Gets the child documents.
     *
     * @return The child documents or an empty collection if the document does
     *          not have children. This implementation does always return an
     *          empty collection.
     */
    public final List<Document> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Gets the template that has created the document.
     *
     * @return The template that has created the document.
     */
    public final DocumentTemplate<? extends Document> getTemplate() {
        return template;
    }

    /**
     * Gets the title of the document.
     * <p>
     * The title is the name of the document for display purposes.
     *
     * @return The title. This implementation does return the name part of
     *          {@link #getName()} if this is a file path or a URL.
     */
    public String getTitle() {
        try {
            URI uri = URI.create(getName());

            if (uri.getScheme() != null) {
                String path = uri.getPath();

                if (path != null && !path.isEmpty()) {
                    return Path.of(path).getFileName().toString();
                }
            }
        } catch (IllegalArgumentException ignored) {
        }

        return Path.of(getName()).getFileName().toString();
    }

    /**
     * Gets the views of the document.
     *
     * @return An unmodifiable list of the views of the document.
     */
    public final List<View<? extends Document>> getViews() {
        return Collections.unmodifiableList(views);
    }

    /**
     * Adds a view to the document.
     * <p>
     * <b>NOTE:</b> This is invoked automatically after a view is created and
     * should never be called by any client code.
     *
     * @param view The view to add.
     *
     * @throws IllegalArgumentException If the view is already added to a
     *         document.
     */
    public void addView(View<? extends Document> view) {
        if (view.getDocument() != this)
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
     * destroyed but not just hidden to re-show it later). The view's document
     * should be set to {@code null} by the view.
     * <p>
     * <b>NOTE:</b> To ensure that the document saves all modified data in the
     * case it is closed, invoke {@link #canCloseView(View)} before invoking
     * this method.
     *
     * @param view The view to remove.
     */
    public void removeView(View<? extends Document> view) {
        views.remove(view);

        if (view.getTemplate().isMandatory() || getTemplate().isAutoClose() && views.isEmpty())
            close();
    }

    /**
     * Gets a value indicating whether the given view can be closed.
     *
     * @param view The view to check.
     *
     * @return {@code true} if the view can be closed. This implementation returns
     *         {@code true} if the view is not mandatory and the document is not
     *         set to auto-close. Otherwise, it returns {@code true} if there is
     *         more than one open view or if the document itself can be closed.
     */
    public boolean canCloseView(View<? extends Document> view) {
        if (!view.getTemplate().isMandatory() && !getTemplate().isAutoClose())
            return true;

        return getViews().size() > 1 || canClose();
    }

    /**
     * Sets the name of the model.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #NAME_PROPERTY}.
     *
     * @param name The name to set.
     */
    @Override
    public void setName(String name) {
        super.setName(name);
        notifyTitleChange();
    }

    /**
     * Sets or unsets the modification flag of the model.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #MODIFIED_PROPERTY}.
     *
     * @param modified {@code true} to indicate the model as modified.
     */
    @Override
    public void setModified(boolean modified) {
        super.setModified(modified);
        notifyTitleChange();
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
     * Resets the read-only flag.
     * <p>
     * This fires a property change event with the property name set to
     * {@link #READ_ONLY_PROPERTY}.
     */
    @Override
    protected void resetReadOnly() {
        super.resetReadOnly();
        notifyTitleChange();
    }

    /**
     * Writes the data into a file, a database, or any other target where the
     * name is queried from the user as long as {@link #isSaveAsSupported()}
     * returns {@code true}.
     * <p>
     * This implementation does nothing if the user cancels the operation;
     * otherwise, it sets the new name and invokes {@link #saveData()}.
     * <p>
     * <b>NOTE:</b> An instance of {@link DVNavigationService} has to be registered
     * by {@link Services#register(Class, Object)}.
     *
     * @throws DVSaveException on any error.
     *
     * @see #saveData()
     * @see #isSaveAsSupported()
     * @see DVNavigationService#querySaveLocation(String, String)
     */
    public void saveDataAs() throws DVSaveException {
        if (!isSaveAsSupported())
            return;

        var saveName = ((DVNavigationService) Services.get(DVNavigationService.class)).querySaveLocation(getName(), getTemplate().getFilter());

        if (saveName == null)
            return;

        setName(saveName);
        setNewData(false);
        super.saveData();
    }

    /**
     * Gets a value that indicates whether this document supports
     * {link #saveDataAs()}.
     * <p>
     * Normally each document can be saved under another name even if it is
     * read-only. For documents that should not be saved (like a Welcome page),
     * this method has to be overridden to return {@code false}.
     *
     * @return {@code true} if {@link #saveDataAs()} is supported. This default
     *          implementation does always return {@code true}.
     */
    public boolean isSaveAsSupported() {
        return true;
    }

    /**
     * Gets a value indicating whether the document can be closed.
     * <p>
     * This implementation queries the user to save if the document is modified.
     * Depending on the user's choice, the document can be closed or not. On
     * error, the error is shown to the user and {@code false} is returned.
     * <p>
     * <b>NOTE:</b> An instance of {@link DVNavigationService} has to be registered
     * by {@link Services#register(Class, Object)}.
     *
     * @return {@code true} if the document can be closed.
     *
     * @see #isModified()
     * @see #setModified(boolean)
     * @see DVNavigationService#querySave(String)
     * @see DVNavigationService#showError(String, Throwable)
     */
    public boolean canClose() {
        if (!isModified())
            return true;

        Boolean result = ((DVNavigationService) Services.get(DVNavigationService.class)).querySave(getName());

        if (result == null)
            return false;

        if (!result)
            return true;

        try {
            saveData();
        } catch (DVSaveException e) {
            ((DVNavigationService) Services.get(DVNavigationService.class)).showError(e.getLocalizedMessage(), e);
            return false;
        }

        return !isModified();
    }

    /**
     * Closes the document with all its open views and without any further action.
     * <p>
     * <b>NOTE:</b> To ensure that the document saves all modified data, invoke
     * {@link #canClose()} before invoking this method.
     * <p>
     * <b>NOTE:</b> Inheritors have to ensure that the base method is invoked
     * to remove the document from the manager's document list and its parent
     * document.
     */
    public void close() {
        close(true);
    }

    /**
     * Gets a value indicating whether the document is closed.
     *
     * @return {@code true} if the document is closed.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Invoked by {@link #loadData()} to load data from a storage.
     * <p>
     * This implementation invokes {@link #createInputStream()} and calls
     * {@link #doLoadData(InputStream)} on itself and on all child documents.
     * <p>
     * The inheritors may decide whether to override this method if there
     * are no children or the override {@link #createInputStream()} and
     * {@link #doLoadData(InputStream)}. If {@link #createInputStream()} returns
     * {@code null}, this method does nothing.
     *
     * @throws IOException on any error.
     */
    @Override
    protected void doLoadData() throws IOException {
        InputStream in = createInputStream();

        if (in == null)
            return;

        try (BufferedInputStream bin = new BufferedInputStream(in)) {
            doLoadData(bin);

            for (var child : children)
                child.doLoadData(bin);
        }
    }

    /**
     * Invoked by {@link #saveData()} to write the data into a storage.
     * <p>
     * This implementation invokes {@link #createOutputStream()} and calls
     * {@link #doSaveData(OutputStream)} on itself and on all child documents.
     * <p>
     * The inheritors may decide whether to override this method if there
     * are no children or the override {@link #createOutputStream()} and
     * {@link #doSaveData(OutputStream)}. If {@link #createOutputStream()}
     * returns {@code null}, this method does nothing.
     *
     * @throws IOException on any error.
     */
    @Override
    protected void doSaveData() throws IOException {
        OutputStream out = createOutputStream();

        if (out == null)
            return;

        try (BufferedOutputStream bout = new BufferedOutputStream(out)) {
            doSaveData(bout);

            for (var child : children)
                child.doSaveData(bout);
        }
    }

    /**
     * Invoked by {@link #loadData()} to load data from a storage.
     * <p>
     * This implementation does nothing.
     *
     * @throws IOException on any error.
     *
     * @see doLoadData()
     */
    @SuppressWarnings({"unused", "RedundantThrows"})
    protected void doLoadData(InputStream is) throws IOException {
    }

    /**
     * Invoked by {@link #saveData()} to write the data into a storage.
     * <p>
     * This implementation does nothing.
     *
     * @throws IOException on any error.
     *
     * @see doSaveData()
     */
    @SuppressWarnings({"RedundantThrows", "unused"})
    protected void doSaveData(OutputStream os) throws IOException {
    }

    /**
     * Invoked to create an input stream for reading the document's data.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The input stream. This is always encapsulated within a
     *         {@link BufferedInputStream} by {@link #doLoadData())}.
     *
     * @throws IOException on any error.
     *
     * @see doLoadData()
     */
    @SuppressWarnings("RedundantThrows")
    protected InputStream createInputStream() throws IOException {
        return null;
    }

    /**
     * Invoked to create an output stream for writing the document's data.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The output stream. This is always encapsulated within a
     *         {@link BufferedOutputStream} by {@link #doSaveData()}.
     *
     * @throws IOException on any error.
     *
     * @see doSaveData()
     */
    @SuppressWarnings("RedundantThrows")
    protected OutputStream createOutputStream() throws IOException {
        return null;
    }

    /**
     * Notifies all views about changes in the document's data.
     * <p>
     * Inheritors should invoke this to notify all views about changed data.
     *
     * @param originator The view that caused the change. This will not be
     *        notified.
     * @param context The context of change. This is implementation defined and
     *        may be {@code null} to indicate that the view should be updated
     *        completely.
     */
    protected void notifyDataChange(View<?> originator, Object context) {
        for (View<? extends Document> view : views)
            if (view != originator)
                view.documentDataChanged(context);
    }

    /**
     * Notifies all views to update its title.
     */
    private void notifyTitleChange() {
        // May be invoked on initialization where the views are not created yet:
        //
        if (views != null)
            for (View<? extends Document> view : views)
                view.updateTitle();
    }

    private void close(boolean removeFromParent) {
        closed = true;
        DVManager.documentClosed(this);

        views.forEach(View::forceClose);

        if (removeFromParent && parent != null)
            parent.removeChild(this);

        for (Document child : children)
            child.close(false);

        children.clear();
    }

    private void addChild(Document child) {
        if (child != null)
            children.add(child);
    }

    private void removeChild(Document child) {
        if (child != null)
            children.remove(child);
    }
}
