package de.ganzer.swing.validaton;

import de.ganzer.core.validation.ValidatorException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.util.Objects;

/**
 * A hint provider that changes the tooltip and the border of the text component.
 */
@SuppressWarnings("unused")
public class BorderValidationHint implements ValidationHintProvider {
    private final Border errorBorder;
    private final boolean keepToolTip;

    private String orgTooltip;
    private Border orgBorder;

    /**
     * Creates a new instance with a red colored thin border where tooltips are
     * not kept.
     *
     * @see BorderValidationHint#BorderValidationHint(Border, boolean)
     */
    public BorderValidationHint() {
        this(BorderFactory.createLineBorder(Color.RED, 1), false);
    }

    /**
     * Creates a new instance where tooltips are not kept.
     *
     * @param errorBorder The border to set.
     *
     * @throws NullPointerException {@code border} is {@code null}.
     *
     * @see BorderValidationHint#BorderValidationHint(Border, boolean)
     */
    public BorderValidationHint(Border errorBorder) {
        this(errorBorder, false);
    }

    /**
     * Creates a new instance with a red colored thin border.
     *
     * @param keepToolTip If this is {@code true}, the tooltips will not be
     *         changed on invalid input.
     *
     * @see BorderValidationHint#BorderValidationHint(Border, boolean)
     */
    public BorderValidationHint(boolean keepToolTip) {
        this(BorderFactory.createLineBorder(Color.RED, 1), keepToolTip);
    }

    /**
     * Creates a new instance.
     *
     * @param errorBorder The border to set.
     * @param keepToolTip If this is {@code true}, the tooltips will not be
     *         changed on invalid input.
     *
     * @throws NullPointerException {@code border} is {@code null}.
     */
    public BorderValidationHint(Border errorBorder, boolean keepToolTip) {
        Objects.requireNonNull(errorBorder, "errorBorder must not be null.");

        this.errorBorder = errorBorder;
        this.keepToolTip = keepToolTip;
    }

    /**
     * Gets the border to use for marking a text field with invalid input.
     *
     * @return The set border. The default is a red thin border.
     */
    public Border getErrorBorder() {
        return errorBorder;
    }

    /**
     * Gets a value that indicates whether tooltips are kept on invalid input.
     *
     * @return {@code true} if tooltips are not changed.
     */
    public boolean isToolTipKept() {
        return keepToolTip;
    }

    /**
     * Called to show the visual hints.
     *
     * @param target The target text field where to show the hints.
     * @param e The exception that causes the error.
     */
    @Override
    public void showHints(JTextComponent target, ValidatorException e) {
        orgTooltip = target.getToolTipText();
        orgBorder = target.getBorder();

        target.setToolTipText(e.getLocalizedMessage());
        target.setBorder(errorBorder);
    }

    /**
     * Called to hide the visual hints.
     *
     * @param target The target text field where to hide the hints.
     */
    @Override
    public void hideHints(JTextComponent target) {
        target.setToolTipText(orgTooltip);
        target.setBorder(orgBorder);

        orgTooltip = null;
        orgBorder = null;
    }
}
