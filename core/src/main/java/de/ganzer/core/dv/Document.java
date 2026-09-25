package de.ganzer.core.dv;

import java.util.Collections;
import java.util.List;

/**
 * An interface to a model that is created with template information.
 * <p>
 * Other than a basic model, a document is always file- or stream-based and is
 * usually able to load data and writes it into another target.
 * <p>
 * For a more easy creation of documents and its possible views, document types
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
 * tpl.addViewTemplate(new DocumentViewTemplate<>(
 *         "Welcome",
 *         WelcomePanel::new,
 *         v -> getMainView().addChildView(v),
 *         DocumentViewTemplate.NOT_CLOSABLE,
 *         null,
 *         null));
 * DVManager.registerDocumentTemplate(tpl);
 * }</pre>
 * This interface provides child documents, but the implementation itself is
 * responsive for managing child documents with all its actions.
 *
 * @since 5.6.0
 */
public interface Document extends Model {
    /**
     * Gets the template that has created the document.
     *
     * @return The template that has created the document.
     */
    DocumentTemplate<?> getDocumentTemplate();

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
}
