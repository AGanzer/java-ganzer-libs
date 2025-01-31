package de.ganzer.swing.controls;

import de.ganzer.swing.internals.Images;

import javax.swing.ImageIcon;
import java.util.Objects;

/**
 * Defines the icons to use for {@link TogglePanel} instances.
 */
public class TogglePanelIcons {
    private final ImageIcon opened;
    private final ImageIcon closed;

    /**
     * Creates a new instance with the specified icons.
     *
     * @param opened The icon to use for opened panels.
     * @param closed The icon to use for closed panels.
     *
     * @throws NullPointerException {@code opened} of {@code closed} is
     *         {@code null}.
     */
    public TogglePanelIcons(ImageIcon opened, ImageIcon closed) {
        Objects.requireNonNull(opened, "opened must not be null.");
        Objects.requireNonNull(closed, "closed must not be null.");

        this.opened = opened;
        this.closed = closed;
    }

    /**
     * Gets the icon that is used for opened panels.
     *
     * @return The used icon.
     */
    public ImageIcon getOpened() {
        return opened;
    }

    /**
     * Gets the icon that is used for closed panels.
     *
     * @return The used icon.
     */
    public ImageIcon getClosed() {
        return closed;
    }

    /**
     * Gets the default icons to use for opened and closed panels.
     *
     * @return The default icons.
     */
    public static TogglePanelIcons getDefaultIcons() {
        return new TogglePanelIcons(
                Images.load("toggle-arrow-opened-16"),
                Images.load("toggle-arrow-closed-16"));
    }
}
