package com.example.uitests.swing.tests;

import com.formdev.flatlaf.FlatClientProperties;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.swing.validaton.ValidationHintProvider;

import javax.swing.text.JTextComponent;

/**
 * A hint provider that changes the tooltip and the border of the text component.
 */
@SuppressWarnings("unused")
public class FlatLaFBorderValidationHint implements ValidationHintProvider {
    private final boolean keepToolTip;

    private String orgTooltip;

    /**
     * Creates a new instance where tooltips are not kept.
     *
     * @see #FlatLaFBorderValidationHint(boolean)
     */
    public FlatLaFBorderValidationHint() {
        this(false);
    }

    /**
     * Creates a new instance with a red colored thin border.
     *
     * @param keepToolTip If this is {@code true}, the tooltips will not be
     *         changed on invalid input.
     */
    public FlatLaFBorderValidationHint(boolean keepToolTip) {
        this.keepToolTip = keepToolTip;
    }

    /**
     * Gets a value that indicates whether tooltips are kept on invalid input.
     *
     * @return {@code true} if tooltips are not changed.
     */
    public boolean shouldKeepToolTip() {
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
        if (!shouldKeepToolTip()) {
            orgTooltip = target.getToolTipText();
            target.setToolTipText(e.getLocalizedMessage());
        }

        target.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
    }

    /**
     * Called to hide the visual hints.
     *
     * @param target The target text field where to hide the hints.
     */
    @Override
    public void hideHints(JTextComponent target) {
        if (!shouldKeepToolTip())
            target.setToolTipText(orgTooltip);

        target.putClientProperty(FlatClientProperties.OUTLINE, null);
        orgTooltip = null;
    }

    /**
     * Called if a visual hint is already shown but the message has changed.
     * <p>
     * This implementation updates the tooltip.
     *
     * @param target The target text field where to show the hints.
     * @param e The exception that causes the error.
     *
     * @since 5.4.0
     */
    @Override
    public void updateHints(JTextComponent target, ValidatorException e) {
        if (!shouldKeepToolTip())
            target.setToolTipText(e.getLocalizedMessage());
    }
}
