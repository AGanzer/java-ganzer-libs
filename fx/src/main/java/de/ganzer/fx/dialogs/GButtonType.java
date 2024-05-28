package de.ganzer.fx.dialogs;

import de.ganzer.fx.internals.FXMessages;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * Defines additional button types that are missed in {@link ButtonType}.
 */
public final class GButtonType {
    /**
     * "Yes To All" button.
     */
    public static final ButtonType YES_TO_ALL;

    /**
     * "No To All" button.
     */
    public static final ButtonType NO_TO_ALL;

    /**
     * "Retry" button.
     */
    public static final ButtonType RETRY;

    /**
     * "Abort" button.
     */
    public static final ButtonType ABORT;

    /**
     * "Ignore" button.
     */
    public static final ButtonType IGNORE;

    static {
        YES_TO_ALL = new ButtonType(FXMessages.get("yesToAllButton"), ButtonBar.ButtonData.YES);
        NO_TO_ALL = new ButtonType(FXMessages.get("noToAllButton"), ButtonBar.ButtonData.NO);
        RETRY = new ButtonType(FXMessages.get("retryButton"), ButtonBar.ButtonData.OK_DONE);
        ABORT = new ButtonType(FXMessages.get("abortButton"), ButtonBar.ButtonData.CANCEL_CLOSE);
        IGNORE = new ButtonType(FXMessages.get("ignoreButton"), ButtonBar.ButtonData.CANCEL_CLOSE);
    }
}
