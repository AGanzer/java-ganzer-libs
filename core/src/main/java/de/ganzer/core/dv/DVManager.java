package de.ganzer.core.dv;

import java.util.*;

/**
 * A singleton document-view-manager.
 *
 * @since 5.6.0
 */
public class DVManager {
    private static final List<DocumentTemplate<?>> templates = new ArrayList<>();
    private static final List<Document> openDocuments = new ArrayList<>();

    /**
     * Adds a document template to the manager.
     *
     * @param template The template to add.
     */
    public static void registerDocumentTemplate(DocumentTemplate<?> template) {
        templates.add(template);
    }

    /**
     * Gets a list of all registered document templates.
     *
     * @return An unmodifiable list of registered document templates.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static List<DocumentTemplate<?>> getDocumentTemplates() {
        return Collections.unmodifiableList(templates);
    }

    /**
     * Gets a list of all open documents.
     *
     * @return An unmodifiable list of open documents.
     */
    public static List<Document> getOpenDocuments() {
        return Collections.unmodifiableList(openDocuments);
    }

    /**
     * Creates a document with new empty data based on the default template and
     * adds it to the list of open documents.
     *
     * @param parent The parent document, or {n@code ull} if the document has no
     *        parent.
     *
     * @return The newly created document.
     *
     * @throws IllegalStateException If no document template is registered.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static Document createDocument(Document parent) {
        return createDocument(parent, null);
    }

    /**
     * Creates a document with new empty data based on the provided template and
     * adds it to the list of open documents.
     *
     * @param parent The parent document, or {n@code ull} if the document has no
     *        parent.
     * @param template The template to use for creating the document. If this is
     *        {@code null}, a default template will be used. if there is no
     *        default template, the first registered template will be used.
     *
     * @return The newly created document.
     *
     * @throws IllegalStateException If no document template is registered.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static Document createDocument(Document parent, DocumentTemplate<?> template) {
        if (templates.isEmpty())
            throw new IllegalStateException("No document template is registered.");

        if (template == null) {
            template = templates.stream().filter(DocumentTemplate::isDefault).findFirst().orElse(null);

            if (template == null)
                template = templates.get(0);
        }

        Document document = template.createDocument(parent);
        openDocuments.add(document);

        return document;
    }
}
