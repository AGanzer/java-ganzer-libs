package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A text field with an optional image at the right edge.
 *
 * @since 5.6.0
 */
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
    public void setIcon(Icon icon) {
        if (icon == getIcon())
            return;

        if (icon == null) {
            if (UIManager.getLookAndFeel().getClass().getName().contains(".flatlaf.")) {
                putClientProperty("JTextField.trailingComponent", null);
            } else {
                remove(iconLabel);
                setMargin(new Insets(0, 0, 0, 0));
            }

            iconLabel.setIcon(null);
        } else {
            iconLabel.setIcon(icon);

            if (UIManager.getLookAndFeel().getClass().getName().contains(".flatlaf.")) {
                putClientProperty("JTextField.trailingComponent", iconLabel);
            } else {
                add(iconLabel, BorderLayout.EAST);

                var rm = iconLabel.getPreferredSize().width;
                var m = getMargin();

                setMargin(new Insets(m.top, m.left, m.bottom, rm));
            }
        }

        revalidate();
        repaint();
    }

/* Mit insets:
    public void setIcon(Icon icon) {
        if (icon == getIcon())
            return;

        if (icon == null) {
            if (iconLabel != null) {
                remove(iconLabel);
                iconLabel = null;
            }
        } else {
            if (iconLabel == null) {
                var insets = getInsets();
                var size = getPreferredSize().height - insets.top - insets.bottom;

                iconLabel = new JLabel();
                iconLabel.setPreferredSize(new Dimension(size, size));

                add(iconLabel, BorderLayout.EAST);
            }

            iconLabel.setIcon(icon);
        }

        revalidate();
        repaint();
    }

    @Override
    public Insets getInsets() {
        Insets insets = super.getInsets();

        return iconLabel != null
                ? new Insets(insets.top, insets.left, insets.bottom, insets.right + iconLabel.getPreferredSize().width)
                : insets;
    }

    @Override
    public Insets getInsets(Insets insets) {
        super.getInsets(insets);

        if (iconLabel != null)
            insets.right += iconLabel.getPreferredSize().width;

        return insets;
    }
*/

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
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);

        if (iconLabel != null)
            iconLabel.setBackground(this.getBackground());
    }

    private void init() {
        if (!UIManager.getLookAndFeel().getClass().getName().contains(".flatlaf."))
            setLayout(new BorderLayout());

        iconLabel.setOpaque(true);
        iconLabel.setBackground(this.getBackground());
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                iconClicked.actionPerformed(new ActionEvent(this, 0, null));
            }
        });
    }
}
