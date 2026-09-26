package de.ganzer.core.dv;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A template that creates views for {@link Document} based models.
 *
 * @param <D> The type of the document.
 * @param <V> The type of the view.
 *
 * @since 5.6.0
 */
public class ViewTemplate<D extends Document, V extends View<D>> {
    /**
     * If this option is set, the template is not shown in the list of available
     * templates for new views.
     */
    public static final int IS_HIDDEN = 0x01;
    /**
     * This option marks the template as the default that is used if no template
     * is specified to create a view.
     */
    public static final int IS_DEFAULT = 0x02;
    /**
     * This option marks the template as mandatory. A document cannot exist
     * without a mandatory view. If this view is closed, the document is closed
     * also.
     */
    public static final int IS_MANDATORY = 0x04;
    /**
     * If this option is set, the view as well as the document cannot be closed
     * by the user.
     */
    public static final int NOT_CLOSABLE = 0x08;

    private final String displayName;
    private final ViewSupplier<D, V> viewSupplier;
    private final Consumer<V> showView;
    private final int options;
    private final String modificationHintFormat;
    private final String readOnlyHintFormat;

    /**
     * Creates a new instance.
     *
     * @param displayName The display name of the template.
     * @param viewSupplier Creates a new view.
     * @param showView Displays the view.
     * @param options The options to set. This can be any combination of
     *        {@link #IS_HIDDEN}, {@link #IS_DEFAULT}, {@link #IS_MANDATORY}
     *        and {@link #NOT_CLOSABLE}.
     * @param modificationHintFormat The format string to use to display the
     *        document's name with a modification mark. If this is {@code null},
     *        "%s*" is used.
     * @param readOnlyHintFormat The format string to use to display the
     *        document's name with a read-only mark. If this is {@code null},
     *        "%s (R)" is used.
     *
     * @throws NullPointerException {@code displayName}, {@code presenterSupplier}
     *         {@code viewSupplier} or {@code showView} is {@code null}.
     */
    public ViewTemplate(String displayName,
                        ViewSupplier<D, V> viewSupplier,
                        Consumer<V> showView,
                        int options,
                        String modificationHintFormat,
                        String readOnlyHintFormat) {
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(viewSupplier, "viewSupplier must not be null");
        Objects.requireNonNull(showView, "showView must not be null");

        this.displayName = displayName;
        this.viewSupplier = viewSupplier;
        this.showView = showView;
        this.options = options;
        this.modificationHintFormat = modificationHintFormat != null ? modificationHintFormat : "%s*";
        this.readOnlyHintFormat = readOnlyHintFormat != null ? readOnlyHintFormat : "%s (R)";
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
     * Indicates whether the view is mandatory.
     *
     * @return {@code true} if the view is mandatory.
     */
    public boolean isMandatory() {
        return (options & IS_MANDATORY) != 0;
    }

    /**
     * Indicates whether the view is closable.
     *
     * @return {@code true} if the view is closable.
     */
    public boolean isClosable() {
        return (options & NOT_CLOSABLE) == 0;
    }

    /**
     * Gets the format string for the document's name with a modification mark.
     *
     * @return The format string.
     */
    public String getModificationHintFormat() {
        return modificationHintFormat;
    }

    /**
     * Gets the format string for the document's name with a read-only mark.
     *
     * @return The format string.
     */
    public String getReadOnlyHintFormat() {
        return readOnlyHintFormat;
    }

    /**
     * Creates a new presenter with a view.
     *
     * @param document The document the presenter shall work on.
     *
     * @return The created presenter.
     */
    public V createView(D document) {
        var cvInfo = new ViewCreationInfo<>(this);
        var view = viewSupplier.createView(cvInfo);

        if (view == null)
            return null;

        document.addView(view);
        view.setDocument(document);

        showView.accept(view);

        return view;
    }
}
