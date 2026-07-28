package de.ganzer.swing.controls.flatlaf;

import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.controls.GTextField;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingContainer;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.BeanProperty;
import java.beans.JavaBean;

/**
 * A text field with an optional image at the right edge.
 *
 * @since 5.6.0
 */
@JavaBean(defaultProperty = "UIClassID",
        description = "A single-line text editor with an optional icon.")
@SwingContainer(false)
@SuppressWarnings("unused")
public class GIconTextField extends GTextField {
    /**
     * The default command of the action that is sent when the icon is clicked.
     * This can be changed by invoking {@link GAction#setCommand(String)} of
     * the action returned by {@link #getIconClickedAction()}.
     */
    public static final String ICON_CLICKED_COMMAND = "GIconTextField.iconClicked";

    private final JLabel iconLabel = new JLabel();
    private final GAction iconClicked = new GAction().command(ICON_CLICKED_COMMAND);

    /**
     * Constructs a new <code>TextField</code>.  A default model is created,
     * the initial string is <code>null</code>,
     * and the number of columns is set to 0.
     */
    public GIconTextField() {
        init();
    }

    /**
     * Constructs a new <code>TextField</code> initialized with the
     * specified text. A default model is created and the number of
     * columns is 0.
     *
     * @param text the text to be displayed, or <code>null</code>
     */
    public GIconTextField(String text) {
        super(text);
        init();
    }

    /**
     * Constructs a new empty <code>TextField</code> with the specified
     * number of columns.
     * A default model is created and the initial string is set to
     * <code>null</code>.
     *
     * @param columns the number of columns to use to calculate
     *         the preferred width; if columns is set to zero, the
     *         preferred width will be whatever naturally results from
     *         the component implementation
     */
    public GIconTextField(int columns) {
        super(columns);
        init();
    }

    /**
     * Constructs a new <code>TextField</code> initialized with the
     * specified text and columns.  A default model is created.
     *
     * @param text the text to be displayed, or <code>null</code>
     * @param columns the number of columns to use to calculate
     *         the preferred width; if columns is set to zero, the
     *         preferred width will be whatever naturally results from
     *         the component implementation
     */
    public GIconTextField(String text, int columns) {
        super(text, columns);
        init();
    }

    /**
     * Constructs a new <code>JTextField</code> that uses the given text
     * storage model and the given number of columns.
     * This is the constructor through which the other constructors feed.
     * If the document is <code>null</code>, a default model is created.
     *
     * @param doc the text storage to use; if this is <code>null</code>,
     *         a default will be provided by calling the
     *         <code>createDefaultModel</code> method
     * @param text the initial string to display, or <code>null</code>
     * @param columns the number of columns to use to calculate
     *         the preferred width &gt;= 0; if <code>columns</code>
     *         is set to zero, the preferred width will be whatever
     *         naturally results from the component implementation
     *
     * @throws IllegalArgumentException if <code>columns</code> &lt; 0
     */
    public GIconTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        init();
    }

    /**
     * Gets the action that is performed when the icon is clicked.
     * <p>
     * The default command of the action is {@link #ICON_CLICKED_COMMAND}.
     * This can be changed by invoking {@link GAction#setCommand(String)} of
     * the action returned by this method.
     * <p>
     * The sender of the performed action is always the action returned by this
     * method.
     *
     * @return The action that is performed when the icon is clicked.
     */
    public GAction getIconClickedAction() {
        return iconClicked;
    }

    /**
     * Gets the cursor that is displayed when the icon is hovered.
     *
     * @return The cursor that is displayed when the icon is hovered.
     */
    public Cursor getIconCursor() {
        return iconLabel.getCursor();
    }

    /**
     * Sets the cursor that is displayed when the icon is hovered.
     *
     * @param cursor The cursor to display when the icon is hovered.
     */
    @BeanProperty(bound = false, description = "The cursor to display when the icon is hovered.")
    public void setIconCursor(Cursor cursor) {
        iconLabel.setCursor(cursor);
    }

    /**
     * Gets the icon that is displayed in the text field.
     *
     * @return The icon that is displayed in the text field, or {@code null} if
     *          no icon is set.
     */
    public Icon getIcon() {
        return iconLabel.getIcon();
    }

    /**
     * Sets the icon to display in the text field.
     *
     * @param icon The icon to display, or {@code null} to remove the icon.
     */
    @BeanProperty(bound = false, description = "the icon to display.")
    public void setIcon(Icon icon) {
        if (icon == getIcon())
            return;

        iconLabel.setIcon(icon);

        if (icon == null) {
            if (UIManager.getLookAndFeel().getClass().getName().contains(".flatlaf.")) {
                putClientProperty("JTextField.trailingComponent", null);
            } else {
                remove(iconLabel);
            }
        } else {
            if (UIManager.getLookAndFeel().getClass().getName().contains(".flatlaf.")) {
                putClientProperty("JTextField.trailingComponent", iconLabel);
            } else {
                add(iconLabel, BorderLayout.EAST);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Sets the background color of this component.  The background
     * color is used only if the component is opaque, and only
     * by subclasses of <code>JComponent</code> or
     * <code>ComponentUI</code> implementations.  Direct subclasses of
     * <code>JComponent</code> must override
     * <code>paintComponent</code> to honor this property.
     * <p>
     * It is up to the look and feel to honor this property, some may
     * choose to ignore it.
     *
     * @param bg the desired background <code>Color</code>
     *
     * @see Component#getBackground
     * @see #setOpaque
     */
    @BeanProperty(preferred = true, visualUpdate = true, description = "The background color of the component.")
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);

        if (iconLabel != null)
            iconLabel.setBackground(this.getBackground());
    }

    // This does not work properly, but it is better than not to use own UI:
    @Override
    public void updateUI() {
        super.updateUI();

        if (!UIManager.getLookAndFeel().getClass().getName().contains(".flatlaf."))
            setUI(new IconTextFieldUI());
    }

    private static class IconTextFieldUI extends BasicTextFieldUI {
        @Override
        protected Rectangle getVisibleEditorRect() {
            Rectangle r = super.getVisibleEditorRect();

            JTextComponent c = getComponent();

            if (c instanceof GIconTextField tf) {
                Icon icon = tf.getIcon();

                if (icon != null) {
                    r.width -= icon.getIconWidth() + 2;
                }
            }

            return r;
        }
    }

    private void init() {
        if (!UIManager.getLookAndFeel().getClass().getName().contains(".flatlaf."))
            setLayout(new BorderLayout());

        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        iconLabel.setOpaque(false);
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                iconClicked.actionPerformed(new ActionEvent(this, 0, null));
            }
        });
    }
}
