package de.ganzer.swing.controls;

import javax.swing.JToolBar;

/**
 * A custom toolbar component that provides an overflow menu for items that do
 * not fit on the toolbar.
 * 
 * @since 6.6.0
 */
public class GToolBar extends JToolBar {
    /**
     * Creates a new toolbar; orientation defaults to {@link #HORIZONTAL}.
     */
    public GToolBar() {
    }

    /**
     * Creates a new toolbar with the specified {@code orientation}.
     * <p>
     * The {@code orientation} must be either {@link #HORIZONTAL}
     * or {@link #VERTICAL}.
     *
     * @param orientation The initial orientation. It must be either
     *         {@link #HORIZONTAL} or {@link #VERTICAL}.
     *
     * @throws IllegalArgumentException if orientation is neither
     *         {@link #HORIZONTAL} nor {@link #VERTICAL}
     */
    public GToolBar(int orientation) {
        super(orientation);
    }

    /**
     * Creates a new toolbar with the specified {@code name}.
     * <p>
     * The name is used as the title of the undocked toolbar. The default
     * orientation is {@link #HORIZONTAL}.
     *
     * @param name The name of the toolbar.
     */
    public GToolBar(String name) {
        super(name);
    }

    /**
     * Creates a new toolbar with a specified {@code name} and
     * {@code orientation}.
     * <p>
     * All other constructors call this constructor. If {@code orientation} is
     * an invalid value, an exception will be thrown.
     *
     * @param name The name of the toolbar.
     * @param orientation The initial orientation. It must be either
     *         {@link #HORIZONTAL} or {@link #VERTICAL}.
     *
     * @throws IllegalArgumentException if orientation is neither
     *         {@link #HORIZONTAL} nor {@link #VERTICAL}
     */
    public GToolBar(String name, int orientation) {
        super(name, orientation);
    }
}
