package de.ganzer.dv;

import de.ganzer.core.util.Strings;

import java.util.*;
import java.util.function.Predicate;

/**
 * A template to use with {@link BasicDVManager} to create a template-based document.
 *
 * @param <D> the type of the document ot create.
 *
 * @since 6.0.0
 */
public class DocumentTemplate<D extends Document> {
    /**
     * No options.
     */
    public static final int NONE = 0x00;
    /**
     * If this option is set, the template is not shown in the list of available
     * templates for new documents.
     */
    public static final int IS_HIDDEN = 0x01;
    /**
     * If this option is set, new documents of this type have their own sequence
     * of new-numbers; otherwise, the sequence is shared with oder document types.
     */
    public static final int OWN_NEW_NUMBER = 0x02;
    /**
     * If this option is set, Creating a new document does not automatically
     * create a new view except that one of the registered view templates marks
     * a view as mandatory.
     *
     * @see ViewTemplate#IS_MANDATORY
     */
    public static final int NO_AUTO_VIEW = 0x04;
    /**
     * If this option is set, the document is not automatically closed when the
     * last view is closed.
     */
    public static final int NO_AUTO_CLOSE = 0x08;
    /**
     * This option marks the template as the default that is used if no template
     * is specified to create a document.
     * <p>
     * <b>NOTE:</b> Only one default view template can be registered by
     * {@link BasicDVManager#registerDocumentTemplate(DocumentTemplate)}.
     */
    public static final int IS_DEFAULT = 0x10;
    /**
     * If this option is set, the model will not display numbers for new models.
     */
    public static final int NO_NEW_NUMBER = 0x20;
    /**
     * If this option is set, the created document is always write protected.
     */
    public static final int READ_ONLY_DOCUMENTS = 0x40;
    /**
     * If this option is set, the document will use the views of the parent
     * document instead of creating its own views.
     * <p>
     * <b>NOTE:</b> Mandatory views are always created even if this option is
     * set.
     */
    public static final int USE_PARENT_VIEWS = 0x80;
    /**
     * If this option is set, the document will notify all views of the child
     * documents about changes in the document's data.
     */
    public static final int NOTIFY_CHILD_VIEWS_ON_CHANGE = 0x100;

    private static final Map<DocumentTemplate<?>, Integer> newNumbers = new HashMap<>();

    private static int globalNewNumber;

    private final List<ViewTemplate<D, ?>> viewTemplates = new ArrayList<>();
    private final String displayName;
    private final Predicate<String> canHandleSource;
    private final DocumentSupplier<D> documentSupplier;
    private final String newName;
    private final String filter;
    private final String defaultExtension;
    private final int options;
    private final String newNameNumberFormat;

    /**
     * Creates a new instance.
     *
     * @param displayName The display name of the template.
     * @param canHandleSource Determines whether the document can read a data
     *        source.
     * @param documentSupplier Creates the document.
     * @param newName The name of new documents.
     * @param filter The filter to use to open documents from existing sources.
     * @param defaultExtension The default extension to use for saving documents
     *        if none is specified or {@code null} for no extension.
     * @param options The options to set. This is any combination of {@link #IS_HIDDEN},
     *         {@link #OWN_NEW_NUMBER}, {@link #NO_AUTO_VIEW}, {@link #NO_AUTO_CLOSE}
     *         and {@link #NO_NEW_NUMBER}.
     *
     * @throws NullPointerException {@code displayName}, {@code canHandleSource}
     *         {@code documentSupplier} or {@code newName} is {@code null}.
     */
    public DocumentTemplate(String displayName,
                            Predicate<String> canHandleSource,
                            DocumentSupplier<D> documentSupplier,
                            String newName,
                            String filter,
                            String defaultExtension,
                            int options) {
        this(displayName, canHandleSource, documentSupplier, newName, filter, defaultExtension, options, null);
    }

    /**
     * Creates a new instance.
     *
     * @param displayName The display name of the template.
     * @param canHandleSource Determines whether the document can read a data
     *        source.
     * @param documentSupplier Creates the document.
     * @param newName The name of new documents.
     * @param filter The filter to use to open documents from existing sources.
     * @param defaultExtension The default extension to use for saving documents
     *        if none is specified or {@code null} for no extension.
     * @param options The options to set. This is any combination of {@link #IS_HIDDEN},
     *         {@link #OWN_NEW_NUMBER}, {@link #NO_AUTO_VIEW}, {@link #NO_AUTO_CLOSE}
     *         and {@link #NO_NEW_NUMBER}.
     * @param newNameNumberFormat The format String to use for new documents that
     *         have a new number. If this is {@code null} "%s %d" is used.
     *
     * @throws NullPointerException {@code displayName}, {@code canHandleSource}
     *         {@code documentSupplier} or {@code newName} is {@code null}.
     */
    public DocumentTemplate(String displayName,
                            Predicate<String> canHandleSource,
                            DocumentSupplier<D> documentSupplier,
                            String newName,
                            String filter,
                            String defaultExtension,
                            int options,
                            String newNameNumberFormat) {
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(canHandleSource, "canHandleSource must not be null");
        Objects.requireNonNull(documentSupplier, "documentSupplier must not be null");
        Objects.requireNonNull(newName, "newName must not be null");

        this.displayName = displayName;
        this.canHandleSource = canHandleSource;
        this.documentSupplier = documentSupplier;
        this.newName = newName;
        this.filter = filter;
        this.defaultExtension = defaultExtension;
        this.options = options;
        this.newNameNumberFormat = newNameNumberFormat != null ? newNameNumberFormat : "%s %d";
    }

    /**
     * Gets the display name of the template.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the filter used to filter data sources to open with this template.
     *
     * @return The filter or {@code null} if no filter is set.
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Gets the default file extension used for saving documents.
     *
     * @return The default file extension or {@code null} if no default
     *         extension is set.
     */
    public String getDefaultExtension() {
        return defaultExtension;
    }

    /**
     * Indicates whether the template is hidden.
     *
     * @return {@code true} if the template is hidden.
     *
     * @see #IS_HIDDEN
     */
    public boolean isHidden() {
        return (options & IS_HIDDEN) != 0;
    }

    /**
     * Indicates whether the template is the default.
     *
     * @return {@code true} if the template is the default.
     *
     * @see #IS_DEFAULT
     */
    public boolean isDefault() {
        return (options & IS_DEFAULT) != 0;
    }

    /**
     * Indicates whether the template automatically should create a view.
     *
     * @return {@code true} if the template automatically should create a view.
     *
     * @see #NO_AUTO_VIEW
     */
    public boolean autoCreateView() {
        return (options & NO_AUTO_VIEW) == 0;
    }

    /**
     * Indicates whether the document shall automatically be closed when the
     * last view is closed.
     *
     * @return {@code true} if the document is automatically closed.
     *
     * @see #NO_AUTO_CLOSE
     */
    public boolean isAutoClose() {
        return (options & NO_AUTO_CLOSE) == 0;
    }

    /**
     * Indicates whether the document has a sequential number in its name if it
     * is a new document.
     *
     * @return {@code true} if the document has a new number.
     *
     * @see #NO_NEW_NUMBER
     */
    public boolean hasNewNumber() {
        return (options & NO_NEW_NUMBER) == 0;
    }

    /**
     * Indicates whether the document is always write protected.
     *
     * @return {@code true} if the document is always write protected.
     *
     * @see #READ_ONLY_DOCUMENTS
     */
    public boolean documentsAreReadOnly() {
        return (options & READ_ONLY_DOCUMENTS) != 0;
    }

    /**
     * Indicates whether the document will use the views of the parent document
     * instead of creating its own views.
     *
     * @return {@code true} if the document will use the views of the parent
     *         document.
     *
     * @see #USE_PARENT_VIEWS
     */
    public boolean useParentViews() {
        return (options & USE_PARENT_VIEWS) != 0;
    }

    /**
     * Indicates whether the document will notify all views of the child
     * documents about changes in the document's data.
     *
     * @return {@code true} if the document will notify all views of the child
     *         documents about changes in the document's data.
     *
     * @see #NOTIFY_CHILD_VIEWS_ON_CHANGE
     */
    public boolean notifyChildViewsOnChange() {
        return (options & NOTIFY_CHILD_VIEWS_ON_CHANGE) != 0;
    }

    /**
     * Gets the registered view templates.
     *
     * @return An unmodifiable list of registered view templates.
     *
     * @see #registerViewTemplate(ViewTemplate)
     */
    public List<ViewTemplate<?, ?>> getViewTemplates() {
        return Collections.unmodifiableList(viewTemplates);
    }

    /**
     * Registers a view template.
     *
     * @param template The template to register.
     *
     * @throws NullPointerException if {@code template} is {@code null}.
     * @throws IllegalArgumentException if {@code template} is mandatory and
     *         a mandatory view template is already registered or of
     *         {@code template} is default and a default view template is
     *         already registered.
     *
     * @see ViewTemplate#IS_MANDATORY
     * @see ViewTemplate#IS_DEFAULT
     */
    public void registerViewTemplate(ViewTemplate<D, ?> template) {
        Objects.requireNonNull(template, "template must not be null.");

        if (template.isMandatory() && viewTemplates.stream().anyMatch(ViewTemplate::isMandatory))
            throw new IllegalArgumentException("A mandatory view template is already registered.");

        if (template.isDefault() && viewTemplates.stream().anyMatch(ViewTemplate::isDefault))
            throw new IllegalArgumentException("A default view template is already registered.");

        viewTemplates.add(template);
    }

    /**
     * Indicates whether a document of this template can read and write the data
     * of the specified source.
     *
     * @param dataSource The source to check. This is implementation defined but
     *         should in most cases be a string to a file or internet address.
     *
     * @return {@code true} if a document of this template can read and write
     *         the data of {@code dataSource}.
     */
    public boolean canHandleDataSource(String dataSource) {
        return canHandleSource.test(dataSource);
    }

    /**
     * Creates a new empty document.
     *
     * @param parent The parent document or {@code null} if there is no parent.
     *
     * @return The created document.
     *
     * @throws IllegalStateException If {@link #autoCreateView()} is {@code true}
     *         and no view template is registered.
     */
    public D createDocument(Document parent) {
        try {
            return createDocument(null, parent, true, false);
        } catch (DVLoadException e) {
            // Should never happen on new Data.
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a document.
     *
     * @param name The name of the document.
     * @param parent The parent document or {@code null} if there is no parent.
     * @param newData {@code true} to create a document with new empty data.
     * @param readOnly {@code true} if the document's data cannot be modified.
     *
     * @return The created document.
     *
     * @throws IllegalStateException If {@link #autoCreateView()} is {@code true}
     *         and no view template is registered.
     * @throws DVLoadException on any error loading the data.
     */
    public D createDocument(String name, Document parent, boolean newData, boolean readOnly) throws DVLoadException {
        if (newData) {
            if (Strings.isNullOrBlank(name))
                name = newName;

            if (hasNewNumber()) {
                int number;

                if ((options & OWN_NEW_NUMBER) != 0) {
                    newNumbers.putIfAbsent(this, 0);
                    number = newNumbers.get(this) + 1;
                    newNumbers.put(this, number);
                } else {
                    number = ++globalNewNumber;
                }

                name = String.format(newNameNumberFormat, name, number);
            }
        }

        var info = new DocumentCreationInfo<>(this, parent, name, readOnly || documentsAreReadOnly(), newData);
        D document = documentSupplier.createDocument(info);

        viewTemplates.stream()
                .filter(ViewTemplate::isMandatory)
                .findFirst()
                .ifPresent(tpl -> tpl.createView(document));

        if (autoCreateView() && (parent == null || !useParentViews())) {
            var template = viewTemplates.stream().filter(ViewTemplate::isDefault).findFirst();

            if (template.isPresent() && !template.get().isMandatory())
                template.get().createView(document);

            var templates = viewTemplates.stream().filter(ViewTemplate::isAutoView).toList();

            for (var tpl : templates)
                if (!tpl.isMandatory() && !tpl.isDefault())
                    tpl.createView(document);

            if (document.getViews().isEmpty()) {
                template = viewTemplates.stream().findFirst();

                if (template.isEmpty())
                    throw new IllegalStateException("DocumentTemplate " + displayName + " has no view template defined.");

                template.get().createView(document);
            }
        }

        return document;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
