package de.ganzer.dv;

import de.ganzer.core.util.Strings;

import java.util.*;

/**
 * A singleton document-view-manager.
 * <p>
 * The manager needs some support to perform certain operations that depend
 * on the used UI framework. This support should be installed once at
 * application startup
 *
 * @see #registerSupport(DVManagerSupport)
 * @see DVManagerSupport
 *
 * @since 6.0.0
 */
public class DVManager {
    private static final List<DocumentTemplate<?>> templates = new ArrayList<>();
    private static final List<Document> openDocuments = new ArrayList<>();

    private static DVManagerSupport support;

    /**
     * Sets the support for the DVManager.
     * <p>
     * The manager needs some support to perform certain operations that depend
     * on the used UI framework. This support should be installed once at
     * application startup
     *
     * @param support The support to set.
     */
    public static void registerSupport(DVManagerSupport support) {
        DVManager.support = support;
    }

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
     * @param parent The parent document, or {@code null} if the document has no
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
     * @param parent The parent document, or {@code null} if the document has no
     *        parent.
     * @param template The template to use for creating the document. If this is
     *        {@code null}, a default template will be used. If there is no
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

        template = getTemplateToUse(null, template);

        Document document = template.createDocument(parent);
        openDocuments.add(document);

        return document;
    }

    /**
     * Opens an existing data source based on the template that matches the
     * source and adds it to the list of open documents.
     *
     * @param parent The parent document, or {@code null} if the document has no
     *        parent.
     * @param dataSource The data source to open.
     * @param readOnly {@code true} if the document should be opened in read-only
     *         mode, {@code false} otherwise.
     *
     * @return The opened document.
     *
     * @throws NullPointerException If the given data source is {@code null}.
     * @throws IllegalStateException If no document template is registered.
     * @throws DVLoadException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static Document openDocument(Document parent, String dataSource, boolean readOnly) throws DVLoadException {
        return openDocument(parent, dataSource, null, readOnly);
    }

    /**
     * Opens an existing data source based on the provided template and
     * adds it to the list of open documents.
     *
     * @param parent The parent document, or {@code null} if the document has no
     *        parent.
     * @param template The template to use for creating the document. If this is
     *        {@code null}, a template will be used that matches the given source.
     *        If there is no template that matches the given source, a default
     *        template will be used. If there is no default template, the first
     *        registered template will be used.
     * @param readOnly {@code true} if the document should be opened in read-only
     *         mode, {@code false} otherwise.
     *
     * @return The opened document.
     *
     * @throws DVLoadException on any error loading the data.
     * @throws NullPointerException If the given data source is {@code null}.
     * @throws IllegalStateException If no document template is registered.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static Document openDocument(Document parent, String dataSource, DocumentTemplate<?> template, boolean readOnly) throws DVLoadException {
        Objects.requireNonNull(dataSource, "dataSource must not be null.");

        template = getTemplateToUse(dataSource, template);

        Document document = template.createDocument(dataSource, parent, false, readOnly);
        openDocuments.add(document);

        return document;
    }

    /**
     * Opens existing data sources by querying the user to choose one or more.
     *
     * @param parent The parent documents, or {@code null} if the documents have
     *        no parent.
     * @param readOnly {@code true} if the documents should be opened in read-only
     *         mode, {@code false} otherwise.
     *
     * @return The opened documents.
     *
     * @throws IllegalStateException If no document template is registered.
     * @throws DVLoadException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     * @see DVNavigationService#queryLocationsToOpen(List, String)
     */
    public static List<Document> openDocuments(Document parent, boolean readOnly) throws DVLoadException {
        return openDocuments(parent, (DocumentTemplate<?>) null, readOnly);
    }

    /**
     * Opens existing data sources by querying the user to choose one or more.
     *
     * @param parent The parent documents, or {@code null} if the documents have
     *        no parent.
     * @param template The template that's filter should be initially used to
     *        choose a data source. If this is {@code null}, a default template
     *        will be used. If there is no default template, the first
     *        registered template will be used.
     * @param readOnly {@code true} if the documents should be opened in read-only
     *         mode, {@code false} otherwise.
     *
     * @return The opened documents.
     *
     * @throws IllegalStateException If no document template is registered.
     * @throws DVLoadException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     * @see DVNavigationService#queryLocationsToOpen(List, String)
     */
    public static List<Document> openDocuments(Document parent, DocumentTemplate<?> template, boolean readOnly) throws DVLoadException {
        var filters = templates.stream().map(DocumentTemplate::getFilter).filter(f -> !Strings.isNullOrBlank(f)).toList();
        var initial = getTemplateToUse(null, template);
        var locations = DVNavigationService.getInstance().queryLocationsToOpen(filters, initial.getFilter());

        return openDocuments(parent, locations, readOnly);
    }

    /**
     * Opens existing data sources based on the templates that match the given
     * sources and adds them to the list of open documents.
     *
     * @param parent The parent documents, or {@code null} if the documents have
     *        no parent.
     * @param dataSources The data sources to open.
     * @param readOnly {@code true} if the documents should be opened in read-only
     *         mode, {@code false} otherwise.
     *
     * @return The opened documents.
     *
     * @throws NullPointerException If the given data sources are {@code null}.
     * @throws IllegalStateException If no document template is registered.
     * @throws DVLoadException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static List<Document> openDocuments(Document parent, Collection<String> dataSources, boolean readOnly) throws DVLoadException {
        Objects.requireNonNull(dataSources, "dataSources must not be null.");

        var documents = new ArrayList<Document>();

        for (var dataSource : dataSources)
            documents.add(openDocument(parent, dataSource, null, readOnly));

        return documents;
    }

    private static DocumentTemplate<?> getTemplateToUse(String dataSource, DocumentTemplate<?> preferred) {
        if (templates.isEmpty())
            throw new IllegalStateException("No document template is registered.");

        if (preferred != null)
            return preferred;

        DocumentTemplate<?> template = null;

        if (!Strings.isNullOrBlank(dataSource))
            template = templates.stream().filter(t -> t.canHandleDataSource(dataSource)).findFirst().orElse(null);

        if (template == null) {
            template = templates.stream().filter(DocumentTemplate::isDefault).findFirst().orElse(null);

            if (template == null)
                template = templates.get(0);
        }

        return template;
    }

    /**
     * Queries all open documents whether tey can be closed.
     *
     * @return {@code true} if all documents can be closed.
     */
    public static boolean canClose() {
        return getOpenDocuments().stream().allMatch(Document::canClose);
    }

    /**
     * Returns the active view.
     *
     * @return the active view or {@code null} if no view is active.
     *
     * @throws IllegalStateException if no support is registered.
     *
     * @see #registerSupport(DVManagerSupport)
     */
    public static View<?> getActiveView() {
        if (support == null)
            throw new IllegalStateException("DVManagerSupport not registered");

        return support.getActiveView();
    }

    /**
     * Returns the active document.
     *
     * @return the active document or {@code null} if no document is active.
     */
    public static Document getActiveDocument() {
        var view = getActiveView();

        if (view != null)
            return view.getDocument();

        return null;
    }

    /**
     * Save the active document.
     * <p>
     * This is a shortcut for {@code DVManager.getActiveDocument().saveData()}.
     *
     * @throws DVSaveException on any error.
     */
    public static void saveActiveDocument() throws DVSaveException {
        var doc = getActiveDocument();

        if (doc != null)
            doc.saveData();
    }

    /**
     * Save the active document with another name.
     * <p>
     * This is a shortcut for {@code DVManager.getActiveDocument().saveDataAs()}.
     *
     * @throws DVSaveException on any error.
     */
    public static void saveActiveDocumentAs() throws DVSaveException {
        var doc = getActiveDocument();

        if (doc != null)
            doc.saveDataAs();
    }

    /**
     * Saves all open documents.
     *
     * @throws DVSaveException on any error.
     */
    public static void saveAllDocuments() throws DVSaveException {
        for (var doc : getOpenDocuments())
            doc.saveData();
    }

    /**
     * Removes the document from the manager's document list.
     * <p>
     * <b>NOTE:</b> This is automatically invoked by {@link AbstractDocument}
     * when it is closed. Implementors of {@link Document} have to ensure that
     * a closed document is removed from the manager's document list.
     *
     * @param doc The document to remove.
     *
     * @throws IllegalStateException if the {@code doc} is not closed.
     */
    public static void documentClosed(Document doc) {
        if (!doc.isClosed())
            throw new IllegalStateException("Document is not closed");

        openDocuments.remove(doc);
    }
}
