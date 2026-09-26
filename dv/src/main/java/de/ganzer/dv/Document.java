package de.ganzer.dv;

import de.ganzer.dv.internals.DVMessages;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * An interface to a model that is created with template information.
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
 *         DocumentTemplate.NO_NEW_NUMBER,
 *         null);
 * tpl.addViewTemplate(new ViewTemplate<>(
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
public interface Document extends Model {
    /**
     * Gets the parent document.
     *
     * @return The parent document or {@code null} if there is no parent. This
     *          implementation does always return {@code null}.
     */
    default Document getParent() {
        return null;
    }

    /**
     * Gets the child documents.
     *
     * @return The child documents or an empty collection if the document does
     *          not have children. This implementation does always return an
     *          empty collection.
     */
    default List<Document> getChildren() {
        return Collections.emptyList();
    }

    /**
     * Gets the template that has created the document.
     *
     * @return The template that has created the document.
     */
    DocumentTemplate<?> getTemplate();

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
    void addView(View<? extends Document> view);

    /**
     * Removes a view from the document.
     * <p>
     * If {@link DocumentTemplate#isAutoClose()} of the document's template is
     * {@code true} or if the given view is mandatory, the document will be
     * closed automatically without any further action. To ensure that all data
     * will be saved correctly, the view should invoke
     * {@link #canCloseView(View)} before removing the view.
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
    void removeView(View<? extends Document> view);

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
    default boolean canCloseView(View<? extends Document> view) {
        if (!view.getTemplate().isMandatory() && !getTemplate().isAutoClose())
            return true;

        return getViews().size() > 1 || canClose();
    }

    /**
     * Gets a value indicating whether the document can be closed.
     * <p>
     * This implementation queries the user to save if the document is modified.
     * Depending on the user's choice, the document can be closed or not. On
     * error, the error is shown to the user and {@code false} is returned.
     *
     * @return {@code true} if the document can be closed.
     *
     * @see #isModified()
     * @see #setModified(boolean)
     * @see DVNavigationService#querySave(String)
     * @see DVNavigationService#showError(String, Throwable)
     */
    default boolean canClose() {
        if (!isModified())
            return true;

        Boolean result = DVNavigationService.getInstance().querySave(getName());

        if (result == null)
            return false;

        if (!result)
            return true;

        try {
            saveData();
        } catch (DVSaveException e) {
            DVNavigationService.getInstance().showError(e.getLocalizedMessage(), e);
            return false;
        }

        return !isModified();
    }

    /**
     * Writes the data into a file, a database, or any other target where the
     * name is queried from the user.
     * <p>
     * Implementors should reset the modification, the new-data nad the
     * read-only flags.
     * <p>
     * This implementation does nothing if the user cancels the operation;
     * otherwise, it sets the new name and invokes {@link #saveData()}.
     *
     * @throws DVSaveException on any error.
     *
     * @see #saveData()
     * @see DVNavigationService#querySaveLocation(String, String)
     */
    default void saveDataAs() throws DVSaveException {
        var saveName = DVNavigationService.getInstance().querySaveLocation(getName(), getTemplate().getFilter());

        if (saveName == null)
            return;

        setName(saveName);
        saveData();
    }

    /**
     * Closes the document with all its open views and without any further action.
     * <p>
     * To ensure that the document saves all modified data, invoke
     * {@link #canClose()} befor invoking this method.
     * <p>
     * <b>NOTE:</b> Implementors have to call {@link DVManager#documentClosed}
     * if this method is invoked.
     */
    void close();

    /**
     * Gets a value indicating whether the document is closed.
     *
     * @return {@code true} if the document is closed.
     */
    boolean isClosed();

    /**
     * Gets the open views of the document.
     *
     * @return The open views of the document.
     */
    List<View<? extends Document>> getViews();
}
