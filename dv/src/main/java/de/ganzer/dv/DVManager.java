package de.ganzer.dv;

import de.ganzer.core.util.Strings;

import java.io.IOException;
import java.util.*;

/**
 * A singleton document-view-manager.
 *
 * @since 6.0.0
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
     * @throws IOException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static Document openDocument(Document parent, String dataSource, boolean readOnly) throws IOException {
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
     * @throws IOException on any error loading the data.
     * @throws NullPointerException If the given data source is {@code null}.
     * @throws IllegalStateException If no document template is registered.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static Document openDocument(Document parent, String dataSource, DocumentTemplate<?> template, boolean readOnly) throws IOException {
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
     * @throws IOException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     * @see DVNavigationService#queryLocationsToOpen(List, String)
     */
    public static List<Document> openDocuments(Document parent, boolean readOnly) throws IOException {
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
     * @throws IOException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     * @see DVNavigationService#queryLocationsToOpen(List, String)
     */
    public static List<Document> openDocuments(Document parent, DocumentTemplate<?> template, boolean readOnly) throws IOException {
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
     * @throws IOException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static List<Document> openDocuments(Document parent, Collection<String> dataSources, boolean readOnly) throws IOException {
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
}
