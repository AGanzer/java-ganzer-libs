package de.ganzer.swing.controls;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.beans.BeanProperty;
import java.beans.JavaBean;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Defines a panel that contains collapsable panels to create an accordion panel.
 */
@SuppressWarnings("unused")
@JavaBean(defaultProperty = "iconsVisible", description = "A component which provides a collection of collapsable containers.")
public class Accordion extends JPanel implements Iterable<TogglePanel> {
    public static final String ICON_VISIBLE_PROPERTY = "iconVisible";
    public static final String HORIZONTAL_ALIGNMENT_PROPERTY = "horizontalAlignment";
    public static final String FOCUSABLE_PROPERTY = "focusable";

    private final List<TogglePanel> panels = new ArrayList<TogglePanel>();
    private boolean iconsVisible;
    private int horizontalAlignment = SwingConstants.LEADING;
    private boolean focusable = true;

    /**
     * Creates a new accordion panel with visible icons.
     */
    public Accordion() {
        this(true);
    }

    /**
     * Creates a new accordion panel.
     *
     * @param iconsVisible {@code false} to hide the icons.
     *
     * @see #isIconsVisible()
     */
    public Accordion(boolean iconsVisible) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.iconsVisible = iconsVisible;
    }

    /**
     * Gets a value that indicates whether the icons are visible.
     * <p>
     * Toggle panels with invisible icons cannot be toggled by a click on the
     * icon.
     *
     * @return {@code true} if the icons are visible.
     */
    public boolean isIconsVisible() {
        return iconsVisible;
    }

    /**
     * Sets the visibility of the icons of all contained panels.
     * <p>
     * Toggle panels with invisible icons cannot be toggled by a click on the
     * icon.
     *
     * @param iconsVisible {@code false} to hide the icons.
     */
    @BeanProperty(
            visualUpdate = true,
            description = "The visibility of the icons in the contained toggle panels.")
    public void setIconsVisible(boolean iconsVisible) {
        if (this.iconsVisible == iconsVisible)
            return;

        var orgIconVisible = this.iconsVisible;
        this.iconsVisible = iconsVisible;

        for (var panel: panels)
            panel.setIconVisible(iconsVisible);

        firePropertyChange(ICON_VISIBLE_PROPERTY, orgIconVisible, iconsVisible);
    }

    /**
     * Gets the horizontal alignment of the text of the contained panels.
     *
     * @return The alignment.
     *
     * @see #setHorizontalAlignment(int)
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Sets the horizontal alignment of the text of the contained panels.
     *
     * @param horizontalAlignment Either {@link SwingConstants#LEFT},
     *        {@link SwingConstants#CENTER},  {@link SwingConstants#RIGHT},
     *        {@link SwingConstants#LEADING} or {@link SwingConstants#TRAILING}.
     */
    @BeanProperty(
            visualUpdate = true,
            enumerationValues = {
                    "SwingConstants.LEFT",
                    "SwingConstants.CENTER",
                    "SwingConstants.RIGHT",
                    "SwingConstants.LEADING",
                    "SwingConstants.TRAILING"},
            description = "The horizontal alignment of the text of the contained toggle panels.")
    public void setHorizontalAlignment(int horizontalAlignment) {
        if (this.horizontalAlignment == horizontalAlignment)
            return;

        var orgAlignment = this.horizontalAlignment;
        this.horizontalAlignment = horizontalAlignment;

        for (var panel: panels)
            panel.setHorizontalAlignment(orgAlignment);

        firePropertyChange(HORIZONTAL_ALIGNMENT_PROPERTY, orgAlignment, this.horizontalAlignment);
    }

    /**
     * Gets the contained panels.
     *
     * @return The contained panels.
     */
    public TogglePanel[] getPanels() {
        return panels.toArray(new TogglePanel[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFocusable() {
        return focusable;
    }

    /**
     * {@inheritDoc}
     */
    @BeanProperty(
            visualUpdate = true,
            description = "The focusable state of the contained toggle panels.")
    @Override
    public void setFocusable(boolean focusable) {
        if (this.focusable == focusable)
            return;

        var orgFocusable = this.focusable;
        this.focusable = focusable;

        for (var panel: panels)
            panel.setFocusable(focusable);

        firePropertyChange(FOCUSABLE_PROPERTY, orgFocusable, focusable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        int newWidth = getWidth();

        for (var panel: panels)
            panel.setSize(newWidth, panel.getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(int index) {
        super.remove(index);

        panels.get(index).removePropertyChangeListener(TogglePanel.COLLAPSED_PROPERTY, this::onPanelToggled);
        panels.remove(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<TogglePanel> iterator() {
        return panels.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if (!(comp instanceof TogglePanel panel))
            throw new IllegalArgumentException("Panels must be of type TogglePanel");

        panel.setCollapsed(true);
        panel.setIconVisible(iconsVisible);
        panel.setHorizontalAlignment(horizontalAlignment);
        panel.setFocusable(focusable);
        panel.addPropertyChangeListener(TogglePanel.COLLAPSED_PROPERTY, this::onPanelToggled);

        panels.add(panel);
        super.addImpl(comp, constraints, index);
    }

    private void onPanelToggled(PropertyChangeEvent event) {
        if ((Boolean)event.getNewValue())
            return;

        for (TogglePanel panel: panels)
            if (panel != event.getSource())
                panel.setCollapsed(true);
    }
}
