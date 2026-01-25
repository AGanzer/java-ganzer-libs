package de.ganzer.fx.validation;

import de.ganzer.core.util.Strings;
import de.ganzer.core.validation.ValidatorException;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

/**
 * A hint provider that changes the tooltip and the style of the control.
 *
 * @since 5.4.0
 */
@SuppressWarnings("unused")
public class StyleValidationHint implements ValidationHintProvider {
    private static final String DEFAULT_ERROR_STYLE = "-fx-border-color: #CC0000; -fx-focus-color: #FF0000;";

    private final String errorStyle;
    private final boolean keepToolTip;

    private String orgStyle;
    private Tooltip orgTooltip;

    /**
     * Creates a new instance with a red error border and where tooltips are not
     * kept.
     */
    public StyleValidationHint() {
        this(null, false);
    }

    /**
     * Creates a new instance with the specified error style and where tooltips
     * are not kept.
     *
     * @param errorStyle The error style to set. If this is {@code null} or
     *        empty or does contain only whitespaces, a red border style is
     *        used.
     */
    public StyleValidationHint(String errorStyle) {
        this(errorStyle, false);
    }

    /**
     * Creates a new instance.
     *
     * @param errorStyle The error style to set. If this is {@code null} or
     *        empty or does contain only whitespaces, a red border style is
     *        used.
     * @param keepToolTip If this is {@code true}, the tooltips will not be
     *         changed on invalid input.
     */
    public StyleValidationHint(String errorStyle, boolean keepToolTip) {
        this.errorStyle = Strings.isNullOrBlank(errorStyle) ? DEFAULT_ERROR_STYLE : errorStyle;
        this.keepToolTip = keepToolTip;
    }

    /**
     * Gets the usd error style.
     *
     * @return The error style.
     */
    public final String getErrorStyle() {
        return errorStyle;
    }

    /**
     * Gets a value that indicates whether tooltips are kept on invalid input.
     *
     * @return {@code true} if tooltips are not changed.
     */
    public final boolean shouldKeepToolTip() {
        return keepToolTip;
    }

    /**
     * Called to show the visual hints.
     *
     * @param target The target control where to show the hints.
     * @param e The exception that causes the error.
     */
    @Override
    public void showHints(TextInputControl target, ValidatorException e) {
        if (!shouldKeepToolTip()) {
            orgTooltip = target.getTooltip();
            target.setTooltip(new Tooltip(e.getLocalizedMessage()));
        }

        orgStyle = target.getStyle();
        target.setStyle(errorStyle);
    }

    /**
     * Called to hide the visual hints.
     *
     * @param target The target control where to hide the hints.
     */
    @Override
    public void hideHints(TextInputControl target) {
        if (!shouldKeepToolTip())
            target.setTooltip(orgTooltip);

        target.setStyle(orgStyle);

        orgTooltip = null;
        orgStyle = null;
    }

    /**
     * Called if a visual hint is already shown but the message has changed.
     * <p>
     * Inheritors that does not use the exception's message, can ignore this.
     *
     * @param target The target control where to show the hints.
     * @param e The exception that causes the error.
     *
     * @since 5.4.0
     */
    @Override
    public void updateHints(TextInputControl target, ValidatorException e) {
        if (!shouldKeepToolTip())
            target.setTooltip(new Tooltip(e.getLocalizedMessage()));
    }
}
