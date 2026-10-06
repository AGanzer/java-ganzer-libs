package de.ganzer.dv;

import de.ganzer.core.OS;
import de.ganzer.core.Services;
import de.ganzer.core.util.Strings;
import de.ganzer.dv.services.DVNavigationService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * A singleton document-view-manager.
 *
 * @since 6.0.0
 */
public class BasicDVManager {
    /**
     * The name of the "activeView" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String ACTIVE_VIEW_PROPERTY = "activeView";

    /**
     * The name of the "activeDocument" property used for {@link PropertyChangeEvent}'s.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String ACTIVE_DOCUMENT_PROPERTY = "activeDocument";

    /**
     * The name of the "openDocuments" property used for {@link PropertyChangeEvent}'s.
     * <p>
     * The old value is {@code null} on inserted documents. The new value is
     * {@code null} on removed documents.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public static final String OPEN_DOCUMENTS_PROPERTY = "openDocuments";

    private static final PropertyChangeSupport pcs = new PropertyChangeSupport(BasicDVManager.class);
    private static final List<DocumentTemplate<?>> templates = new ArrayList<>();
    private static final List<Document> openDocuments = new ArrayList<>();

    private static View<?> activeView;

    /**
     * Add a PropertyChangeListener to the listener list.
     * <p>
     * The listener is registered for all properties. The same listener object
     * may be added more than once and will be called as many times as it is
     * added.
     * <p>
     * If {@code listener} is {@code null}, no exception is thrown and
     * no action is taken.
     *
     * @param listener  The PropertyChangeListener to be added.
     */
    public static void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * <p>
     * This removes a PropertyChangeListener that was registered for all
     * properties. If {@code listener} was added more than once to the same
     * event source, it will be notified one less time after being removed.
     * <p>
     * If {@code listener} is {@code null}, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param listener  The PropertyChangeListener to be removed.
     */
    public static void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Add a PropertyChangeListener for a specific property.
     * <p>
     * The listener will be invoked only for that specific property. The same
     * listener object may be added more than once. For each property, the
     * listener will be invoked the number of times it was added for that
     * property.
     * If {@code propertyName} or {@code listener} is null, no exception is
     * thrown and no action is taken.
     *
     * @param propertyName  The name of the property to listen on.
     * @param listener  The PropertyChangeListener to be added.
     */
    public static void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     * <p>
     * If {@code listener} was added more than once to the same event source for
     * the specified property, it will be notified one less time after being
     * removed.
     * <p>
     * If {@code propertyName} is null, no exception is thrown and no action is
     * taken. If {@code listener} is null, or was never added for the specified
     * property, no exception is thrown and no action is taken.
     *
     * @param propertyName  The name of the property that was listened on.
     * @param listener  The PropertyChangeListener to be removed.
     */
    public static void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Adds a document template to the manager.
     *
     * @param template The template to add.
     *
     * @throws NullPointerException if {@code template} is {@code null}.
     * @throws IllegalArgumentException if {@code template} is default and a
     *         default view template is already registered.
     */
    public static void registerDocumentTemplate(DocumentTemplate<?> template) {
        Objects.requireNonNull(template, "template must not be null.");

        if (template.isDefault() && templates.stream().anyMatch(DocumentTemplate::isDefault))
            throw new IllegalArgumentException("A default document template is already registered.");

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
     * <p>
     * <b>NOTE:</b> An instance of {@link DVNavigationService} has to be registered
     * by {@link Services#register(Class, Object)}.
     *
     * @param parent The parent document, or {@code null} if the document has no
     *        parent.
     *
     * @return The newly created document or {@code null} if the user has canceled.
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
     * <p>
     * <b>NOTE:</b> An instance of {@link DVNavigationService} has to be registered
     * by {@link Services#register(Class, Object)}.
     *
     * @param parent The parent document, or {@code null} if the document has no
     *        parent.
     * @param template The template to use for creating the document. If this is
     *        {@code null}, a default template will be used. If there is no
     *        default template, the first registered template will be used.
     *
     * @return The newly created document or {@code null} if the user has canceled.
     *
     * @throws IllegalStateException If no document template is registered.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static Document createDocument(Document parent, DocumentTemplate<?> template) {
        if (template == null) {
            var tpls = templates.stream().filter(t -> !t.isHidden()).toList();

            template = tpls.size() > 1
                    ? ((DVNavigationService) Services.get(DVNavigationService.class)).chooseDocumentTemplate(tpls)
                    : templates.stream().filter(DocumentTemplate::isDefault).findFirst().orElse(templates.get(0));
        }

        if (template == null)
            return null;

        Document document = template.createDocument(parent);
        openDocuments.add(document);
        pcs.firePropertyChange(OPEN_DOCUMENTS_PROPERTY, null, document);

        return document;
    }

    /**
     * Creates a new view for the specified document.
     * <p>
     * The user will be queried to choose a template if the document's template
     * does contain more than one unhidden view template; otherwise, the default
     * template will be used.
     *
     * @param document The document where to create the view for.
     *
     * @return The created view or {@code null} if the user has canceled.
     *
     * @param <D> The type of the document.
     */
    public static <D extends Document> View<?> createView(D document) {
        return createView(document, null);
    }

    /**
     * Creates a new view for the specified document.
     *
     * @param document The document where to create the view for.
     * @param template The template to use for creating the view. If this is
     *         {@code null}, the user will be queried to choose a template if
     *         the document's template does contain more than one unhidden view
     *         template; otherwise, the default template will be used.
     *
     * @return The created view or {@code null} if the user has canceled.
     *
     * @param <D> The type of the document.
     */
    public static <D extends Document> View<?> createView(D document, ViewTemplate<D, ?> template) {
        Objects.requireNonNull(document, "Document must not be null.");

        ViewTemplate<D, ?> templateToUse = template;

        if (templateToUse == null) {
            var docViewTemplates = document.getTemplate().getViewTemplates();
            var tpls = docViewTemplates.stream()
                    .filter(t -> !t.isHidden())
                    .toList();

            ViewTemplate<?, ?> chosen = tpls.size() > 1
                    ? ((DVNavigationService) Services.get(DVNavigationService.class)).chooseViewTemplate(tpls)
                    : docViewTemplates.stream().filter(ViewTemplate::isDefault).findFirst().orElse(docViewTemplates.get(0));

            //noinspection unchecked
            templateToUse = (ViewTemplate<D, ?>) chosen;
        }

         return templateToUse != null ? templateToUse.createView(document) : null;
    }

    /**
     * Opens an existing data source based on the template that matches the
     * source and adds it to the list of open documents.
     * <p>
     * If the given source is already opened, the existing document will be
     * activated by bringing its default view to the front and is returned.
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
     * <p>
     * If the given source is already opened, the existing document will be
     * activated by bringing its default view to the front and is returned.
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

        Document document = getOpenDocuments().stream()
                // TODO: correct search for open document by name:
                .filter(doc -> OS.isWindows() ? doc.getName().equalsIgnoreCase(dataSource) : doc.getName().equals(dataSource))
                .findFirst()
                .orElse(null);

        if (document != null) {
            activateDocument(document);
        } else {
            template = getTemplateToUse(dataSource, template);

            document = template.createDocument(dataSource, parent, false, readOnly);
            openDocuments.add(document);
            pcs.firePropertyChange(OPEN_DOCUMENTS_PROPERTY, null, document);
        }

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
     * @return The opened documents or an empty list if the user has canceled.
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
     * <p>
     * <b>NOTE:</b> An instance of {@link DVNavigationService} has to be registered
     * by {@link Services#register(Class, Object)}.
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
     * @return The opened documents or an empty list if the user has canceled.
     *
     * @throws IllegalStateException If no document template is registered.
     * @throws DVLoadException on any error loading the data.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     * @see DVNavigationService#queryLocationsToOpen(List, String)
     */
    public static List<Document> openDocuments(Document parent, DocumentTemplate<?> template, boolean readOnly) throws DVLoadException {
        var filters = templates.stream()
                .map(DocumentTemplate::getFilter)
                .filter(f -> !Strings.isNullOrBlank(f))
                .toList();
        var initial = getTemplateToUse(null, template);
        var locations = ((DVNavigationService) Services.get(DVNavigationService.class)).queryLocationsToOpen(filters, initial.getFilter());

        return locations == null || locations.isEmpty()
                ? List.of()
                : openDocuments(parent, locations, readOnly);
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
     */
    public static View<?> getActiveView() {
        return activeView;
    }

    /**
     * Sets the active view and the active document.
     * <p>
     * <b>NOTE:</b> The client should invoke this if the active view has changed.
     *
     * @param view the new active view or {@code null} if no view is active.
     */
    public static void setActiveView(View<?> view) {
        if (activeView == view)
            return;

        var oldView = activeView;
        var oldDoc = activeView == null ? null : activeView.getDocument();
        var newDoc = view == null ? null : view.getDocument();

        activeView = view;

        pcs.firePropertyChange(ACTIVE_VIEW_PROPERTY, oldView, activeView);
        pcs.firePropertyChange(ACTIVE_DOCUMENT_PROPERTY, oldDoc, newDoc);
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
     * Tries to activate the given document.
     *
     * @param document The document to activate.
     *
     * @throws NullPointerException If the given document is {@code null}.
     */
    public static void activateDocument(Document document) {
        Objects.requireNonNull(document, "document must not be null.");

        document.getViews().stream()
                .filter(v -> v.getTemplate().isDefault())
                .findFirst().ifPresent(View::toFront);
    }

    /**
     * Save the active document.
     * <p>
     * This is a shortcut for {@code BasicDVManager.getActiveDocument().saveData()}.
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
     * This is a shortcut for {@code BasicDVManager.getActiveDocument().saveDataAs()}.
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
     * <b>NOTE:</b> This is automatically invoked by {@link Document}
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

        if (openDocuments.remove(doc))
            pcs.firePropertyChange(OPEN_DOCUMENTS_PROPERTY, doc, null);
    }

    private static DocumentTemplate<?> getTemplateToUse(String dataSource, DocumentTemplate<?> preferred) {
        if (templates.isEmpty())
            throw new IllegalStateException("No document template is registered.");

        if (preferred != null)
            return preferred;

        DocumentTemplate<?> template = null;

        if (!Strings.isNullOrBlank(dataSource))
            template = templates.stream().filter(t -> t.canHandleDataSource(dataSource)).findFirst().orElse(null);

        if (template != null)
            return template;

        return templates.stream().filter(DocumentTemplate::isDefault).findFirst().orElse(templates.get(0));
    }
}
