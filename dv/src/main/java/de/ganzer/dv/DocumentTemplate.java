package de.ganzer.dv;

import de.ganzer.core.util.Strings;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

/**
 * A template to use with {@link DVManager} to create a template-based document.
 *
 * @param <D> the type of the document ot create.
 *
 * @since 6.0.0
 */
public class DocumentTemplate<D extends Document> {
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
     */
    public static final int IS_DEFAULT = 0x10;
    /**
     * If this option is set, the model will not display numbers for new models.
     */
    public static final int NO_NEW_NUMBER = 0x20;

    private static final Map<DocumentTemplate<?>, Integer> newNumbers = new HashMap<>();

    private static int globalNewNumber;

    private final List<ViewTemplate<D, ?>> viewTemplates = new ArrayList<>();
    private final String displayName;
    private final Predicate<String> canHandleSource;
    private final DocumentSupplier<D> documentSupplier;
    private final String newName;
    private final String filter;
    private final int options;
    private final String newNameNumberFormat;

    /**
     * Creates a new instance.
     *
     * @param displayName The display name of the template.
     * @param canHandleSource Determines whether a data source can be read by
     *        the document.
     * @param documentSupplier Creates the document.
     * @param newName The name of new documents.
     * @param filter The filter to use to open documents from existing sources.
     * @param options The options to set. This is any combination of {@link #IS_HIDDEN},
     *        {@link #OWN_NEW_NUMBER}, {@link #NO_AUTO_VIEW}, {@link #NO_AUTO_CLOSE}
     *        and {@link #NO_NEW_NUMBER}.
     * @param newNameNumberFormat The format String to use for new documents that
     *        have a new number. If this is {@code null} "%s %d" is used.
     *
     * @throws NullPointerException {@code displayName}, {@code canHandleSource}
     *         {@code documentSupplier} or {@code newName} is {@code null}.
     */
    public DocumentTemplate(String displayName, Predicate<String> canHandleSource, DocumentSupplier<D> documentSupplier, String newName, String filter, int options, String newNameNumberFormat) {
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(canHandleSource, "canHandleSource must not be null");
        Objects.requireNonNull(documentSupplier, "documentSupplier must not be null");
        Objects.requireNonNull(newName, "newName must not be null");

        this.displayName = displayName;
        this.canHandleSource = canHandleSource;
        this.documentSupplier = documentSupplier;
        this.newName = newName;
        this.filter = filter;
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
     * Gets the filter that is used to filter data sources to open with this
     * template.
     *
     * @return The filter or {@code null} if no filter is set.
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Indicates whether the template is hidden.
     *
     * @return {@code true} if the template is hidden.
     */
    public boolean isHidden() {
        return (options & IS_HIDDEN) != 0;
    }

    /**
     * Indicates whether the template is the default.
     *
     * @return {@code true} if the template is the default.
     */
    public boolean isDefault() {
        return (options & IS_DEFAULT) != 0;
    }

    /**
     * Indicates whether the template automatically should create a view.
     *
     * @return {@code true} if the template automatically should create a view.
     */
    public boolean isAutoView() {
        return (options & NO_AUTO_VIEW) == 0;
    }

    /**
     * Indicates whether the document shall be automatically be closed when the
     * last view is closed.
     *
     * @return {@code true} if the document is automatically closed.
     */
    public boolean isAutoClose() {
        return (options & NO_AUTO_CLOSE) == 0;
    }

    /**
     * Indicates whether the document has a sequential number in its name if it
     * is a new document.
     *
     * @return {@code true} if the document has a new number.
     */
    public boolean hasNewNumber() {
        return (options & NO_NEW_NUMBER) == 0;
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
     * @param viewTemplate The template to register.
     */
    public void registerViewTemplate(ViewTemplate<D, ?> viewTemplate) {
        viewTemplates.add(viewTemplate);
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
     * @throws IllegalStateException If {@link #isAutoView()} is {@code true}
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
     * @throws IllegalStateException If {@link #isAutoView()} is {@code true}
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

        var info = new DocumentCreationInfo<>(this, parent, name, readOnly, newData);
        D document = documentSupplier.createDocument(info);

        var template = viewTemplates.stream().filter(ViewTemplate::isMandatory).findFirst();

        if (template.isPresent()) {
            template.get().createView(document);
        } else if (isAutoView()) {
            template = viewTemplates.stream().filter(ViewTemplate::isDefault).findFirst();

            if (template.isEmpty())
                template = viewTemplates.stream().findFirst();

            if (template.isEmpty())
                throw new IllegalStateException("DocumentTemplate " + displayName + " has no view template defined.");

            template.get().createView(document);
        }

        return document;
    }
}
