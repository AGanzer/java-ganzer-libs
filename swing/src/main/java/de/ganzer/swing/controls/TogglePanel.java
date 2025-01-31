package de.ganzer.swing.controls;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.BeanProperty;
import java.beans.JavaBean;
import java.util.Objects;

/**
 * A collapsable panel that con be inserted into an {@link Accordion}.
 */
@SuppressWarnings("unused")
@JavaBean(defaultProperty = "title", description = "A component which provides a collapsable container.")
public class TogglePanel extends JPanel {
    /**
     * The property name of the {@link #isCollapsed()} property.
     */
    public static final String COLLAPSED_PROPERTY = "collapsed";
    /**
     * The property name of the {@link #getTitle()} property.
     */
    public static final String TITLE_PROPERTY = "title";
    /**
     * The property name of the {@link #getIcons()} property.
     */
    public static final String ICONS_PROPERTY = "icons";
    /**
     * The property name of the {@link #getContentPane()} property.
     */
    public static final String CONTENT_PANE_PROPERTY = "contentPane";
    /**
     * The property name of the {@link #isIconVisible()} property.
     */
    public static final String ICON_VISIBLE_PROPERTY = "iconVisible";
    /**
     * The property name of the {@link #getHorizontalAlignment()} property.
     */
    public static final String HORIZONTAL_ALIGNMENT_PROPERTY = "horizontalAlignment";
    /**
     * The property name of the {@link #isFocusable()} property.
     */
    public static final String FOCUSABLE_PROPERTY = "focusable";
    /**
     * The property name of the {@link #getToolTipText()} property.
     */
    public static final String TOOL_TIP_TEXT_PROPERTY = "toolTipText";

    private final TitleButton titleButton;
    private TogglePanelIcons icons;
    private Container contentPane;
    private boolean collapsed;
    private boolean iconVisible = true;

    /**
     * Creates a collapsable panel with no title, empty content and default
     * icons set.
     *
     * @see TogglePanelIcons#getDefaultIcons()
     */
    public TogglePanel() {
        this(null, TogglePanelIcons.getDefaultIcons(), new JPanel());
    }

    /**
     * Creates a collapsable panel with empty content and default icons set.
     *
     * @param title The title of the panel that is shown in the header.
     *
     * @see TogglePanelIcons#getDefaultIcons()
     */
    public TogglePanel(String title) {
        this(title, TogglePanelIcons.getDefaultIcons(), new JPanel());
    }

    /**
     * Creates a collapsable panel with no title and empty content set.
     *
     * @param icons The icons to use for collapsed an expanded panels.
     *
     * @throws NullPointerException {@code icons} is {@code null}.
     */
    public TogglePanel(TogglePanelIcons icons) {
        this(null, icons, new JPanel());
    }

    /**
     * Creates a collapsable panel with no title and default icons set.
     *
     * @param contentPane The content to set.
     *
     * @see TogglePanelIcons#getDefaultIcons()
     *
     * @throws NullPointerException {@code contentPane} is {@code null}.
     */
    public TogglePanel(Container contentPane) {
        this("", TogglePanelIcons.getDefaultIcons(), contentPane);
    }

    /**
     * Creates a collapsable panel with empty content set.
     *
     * @param title The title of the panel that is shown in the header.
     * @param icons The icons to use for collapsed an expanded panels.
     *
     * @throws NullPointerException {@code icons} is {@code null}.
     */
    public TogglePanel(String title, TogglePanelIcons icons) {
        this(title, icons, new JPanel());
    }

    /**
     * Creates a collapsable panel with default icons set.
     *
     * @param title The title of the panel that is shown in the header.
     * @param contentPane The content to set.
     *
     * @throws NullPointerException {@code contentPane} is {@code null}.
     *
     * @see TogglePanelIcons#getDefaultIcons()
     *
     * @throws NullPointerException {@code contentPane} is {@code null}.
     */
    public TogglePanel(String title, Container contentPane) {
        this(title, TogglePanelIcons.getDefaultIcons(), contentPane);
    }

    /**
     * Creates a collapsable panel.
     *
     * @param title The title of the panel that is shown in the header.
     * @param icons The icons to use for collapsed an expanded panels.
     * @param contentPane The content to set.
     *
     * @throws NullPointerException {@code icons} or {@code contentPane} is
     *         {@code null}.
     */
    public TogglePanel(String title, TogglePanelIcons icons, Container contentPane) {
        super(new BorderLayout());

        Objects.requireNonNull(icons, "icons must not be null.");
        Objects.requireNonNull(contentPane, "contentPane must not be null.");

        this.icons = icons;
        this.contentPane = contentPane;

        titleButton = new TitleButton(title, icons.getOpened());
        titleButton.addActionListener(e -> setCollapsed(!isCollapsed()));

        add(titleButton, BorderLayout.NORTH);
        add(this.contentPane, BorderLayout.CENTER);
    }

    /**
     * Gets the title that is displayed in the header.
     *
     * @return The title or {@code null} of no title is set.
     */
    public String getTitle() {
        return titleButton.getText();
    }

    /**
     * Sets the title that is displayed in the header.
     *
     * @param title The title to set.
     */
    @BeanProperty(
            visualUpdate = true,
            description = "The title of the toggle panel.")
    public void setTitle(String title) {
        if (titleButton.getText().equals(title))
            return;

        var orgTitle = titleButton.getText();
        titleButton.setText(title);

        firePropertyChange(TITLE_PROPERTY, orgTitle, title);
    }

    /**
     * Gets the content of the panel.
     *
     * @return The content.
     */
    public Container getContentPane() {
        return contentPane;
    }

    /**
     * Sets the content of the panel.
     *
     * @param contentPane The content to set.
     *
     * @throws NullPointerException {@code contentPane} is {@code null}.
     */
    public void setContentPane(Container contentPane) {
        if (this.contentPane == contentPane)
            return;

        var orgPane = this.contentPane;
        this.contentPane = contentPane;

        if (!isCollapsed()) {
            remove(orgPane);
            add(this.contentPane, BorderLayout.CENTER);
        }

        firePropertyChange(CONTENT_PANE_PROPERTY, orgPane, contentPane);
    }

    /**
     * Gets the icons to use for collapsed and expanded panels.
     *
     * @return The used icons.
     */
    public TogglePanelIcons getIcons() {
        return icons;
    }

    /**
     * Sets the icons to use for collapsed and expanded panels.
     *
     * @param icons The icons to use.
     *
     * @throws NullPointerException {@code icons} is {@code null}.
     *
     * @see TogglePanelIcons#getDefaultIcons()
     */
    public void setIcons(TogglePanelIcons icons) {
        if (this.icons == icons)
            return;

        var orgIcons = this.icons;
        this.icons = icons;

        if (iconVisible)
            titleButton.setIcon(collapsed ? icons.getClosed() : icons.getOpened());

        firePropertyChange(ICONS_PROPERTY, orgIcons, icons);
    }

    /**
     * Gets the horizontal alignment of the title's text.
     *
     * @return The alignment.
     */
    public int getHorizontalAlignment() {
        return titleButton.getHorizontalAlignment();
    }

    /**
     * Sets the horizontal alignment of the title's text.
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
            description = "The horizontal alignment of the text.")
    public void setHorizontalAlignment(int horizontalAlignment) {
        if (titleButton.getHorizontalAlignment() == horizontalAlignment)
            return;

        var orgAlignment = titleButton.getHorizontalAlignment();
        titleButton.setHorizontalAlignment(horizontalAlignment);

        firePropertyChange(HORIZONTAL_ALIGNMENT_PROPERTY, orgAlignment, titleButton.getHorizontalAlignment());
    }

    /**
     * Gets a value that indicates whether the icons are visible.
     * <p>
     * Toggle panels with invisible icons cannot be toggled by a click on the
     * icon.
     *
     * @return {@code true} if the icons are visible.
     */
    public boolean isIconVisible() {
        return iconVisible;
    }

    /**
     * Sets the visibility of the icons.
     * <p>
     * Toggle panels with invisible icons cannot be toggled by a click on the
     * icon.
     *
     * @param iconVisible {@code false} to hide the icons.
     */
    @BeanProperty(
            visualUpdate = true,
            description = "The visibility of the icon.")
    public void setIconVisible(boolean iconVisible) {
        if (this.isIconVisible() == iconVisible)
            return;

        var orgIconVisible = this.iconVisible;
        this.iconVisible = iconVisible;

        if (iconVisible)
            titleButton.setIcon(collapsed ? icons.getClosed() : icons.getOpened());
        else
            titleButton.setIcon(null);

        firePropertyChange(ICON_VISIBLE_PROPERTY, orgIconVisible, iconVisible);
    }

    /**
     * Gets a value that indicates whether the panel is collapsed.
     *
     * @return {@code true} if the panel is collapsed.
     */
    public boolean isCollapsed() {
        return collapsed;
    }

    /**
     * Sets the collapsed status of the panel.
     *
     * @param collapsed {@code true} to collapse the panel; {@code false} to
     *        expand it.
     */
    @BeanProperty(
            visualUpdate = true,
            description = "The collapsed state of the toggle panel.")
    public void setCollapsed(boolean collapsed) {
        if (this.collapsed == collapsed)
            return;

        this.collapsed = collapsed;

        if (collapsed)
            remove(contentPane);
        else
            add(contentPane, BorderLayout.CENTER);

        if (iconVisible)
            titleButton.setIcon(collapsed ? icons.getClosed() : icons.getOpened());

        firePropertyChange(COLLAPSED_PROPERTY, !collapsed, collapsed);

        var root = SwingUtilities.getRoot(this);

        if (root != null)
            SwingUtilities.updateComponentTreeUI(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFocusable() {
        return titleButton.isFocusable();
    }

    /**
     * {@inheritDoc}
     */
    @BeanProperty(
            visualUpdate = true,
            description = "The focusable state of the toggle panel.")
    @Override
    public void setFocusable(boolean focusable) {
        if (titleButton.isFocusable() == focusable)
            return;

        var orgFocusable = this.titleButton.isFocusable();
        titleButton.setFocusable(focusable);

        firePropertyChange(FOCUSABLE_PROPERTY, orgFocusable, focusable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolTipText() {
        return titleButton.getToolTipText();
    }

    /**
     * {@inheritDoc}
     */
    @BeanProperty(
            visualUpdate = true,
            description = "The tool tip text of the toggle panel.")
    @Override
    public void setToolTipText(String text) {
        if (titleButton.getToolTipText().equals(text))
            return;

        var orgToolTipText = this.titleButton.getToolTipText();
        super.setToolTipText(text);

        firePropertyChange(TOOL_TIP_TEXT_PROPERTY, orgToolTipText, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getMaximumSize() {
        if (!collapsed)
            return super.getMaximumSize();

        var size = titleButton.getPreferredSize();
        size.width = super.getMaximumSize().width;

        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        getContentPane().setSize(width, getContentPane().getHeight());
    }

    private static class TitleButton extends JButton {
        private final JLabel iconLabel;

        public TitleButton(String title, ImageIcon icon) {
            super(title);

            iconLabel = new JLabel(icon);
            add(iconLabel);

            setHorizontalAlignment(SwingConstants.LEADING);
            setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
        }

        public void setIcon(ImageIcon icon) {
            iconLabel.setIcon(icon);

            if (icon != null) {
                setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
                add(iconLabel);
            } else {
                remove(iconLabel);
                setLayout(null);
            }
        }
    }
}
