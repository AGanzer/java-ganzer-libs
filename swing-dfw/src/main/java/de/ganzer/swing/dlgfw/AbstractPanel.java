package de.ganzer.swing.dlgfw;

import de.ganzer.swing.actions.GAction;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.*;
import java.util.Objects;

/**
 * Defines a pane that should be used as base class for all dialog and frame
 * center panels.
 * <p>
 * This class has always a {@link BorderLayout} layout manager and implements
 * a factory method that fills all layout positions that are predefined for all
 * frames and dialogs. This contains the following sub panels in the
 * order they are queried and created:
 *
 * <ul>
 *     <li>{@link BorderLayout#CENTER}: The mandatory main content of the frame
 *         or dialog.</li>
 *     <li>{@link BorderLayout#NORTH}: The mandatory title panel that contains
 *         the name of the dialog or frame.</li>
 *     <li>{@link BorderLayout#SOUTH}: An optional panel for additional controls.
 *         </li>
 *     <li>{@link BorderLayout#EAST}: An optional panel for additional controls.
 *         </li>
 *     <li>{@link BorderLayout#WEST}: An optional panel for additional controls.
 *         </li>
 * </ul>
 *
 * All these panels are created in the derived classes, except the title panel.
 * This is created by {@code AbstractVPContentPane} itself and the only methods
 * that have to be overridden are {@link #createTitleLabel()} and optionally
 * {@link #createTitleIcon()} to provide the name of the frame or dialog and an
 * optional image that is displayed in front of the title. If the title shall be
 * filled with further controls, {@link #createCenteredTitleExtension()} and
 * {@link #createRightTitleExtension()} should be overridden.
 */
public abstract class AbstractPanel extends JPanel implements Disposable {
    private static Color titleBackground = Color.WHITE;

    private final GAction queryCloseAction = new GAction();

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public AbstractPanel() {
        super(new BorderLayout());
        setup();
    }

    /**
     * Creates a new <code>JPanel</code> with <code>FlowLayout</code>
     * and the specified buffering strategy.
     * If <code>isDoubleBuffered</code> is true, the <code>JPanel</code>
     * will use a double buffer.
     *
     * @param isDoubleBuffered a boolean, true for double-buffering, which
     *         uses additional memory space to achieve fast, flicker-free
     *         updates
     */
    public AbstractPanel(boolean isDoubleBuffered) {
        super(new BorderLayout(), isDoubleBuffered);
        setup();
    }

    /**
     * Gets the color to use as background-color for titles.
     *
     * @return The color. The default is {@link Color#WHITE}.
     */
    public static Color getTitleBackground() {
        return titleBackground;
    }

    /**
     * Sets the color to use as background for titles.
     *
     * @param color The color to set.
     *
     * @throws NullPointerException {@code color} is {@code null}.
     */
    public static void setTitleBackground(Color color) {
        Objects.requireNonNull(color, "color cannot be null.");
        titleBackground = color;
    }

    /**
     * Gets the action that indicates that a panel wants to close the owning
     * window.
     * <p>
     * A Window that is derived from {@link AbstractDialog} or from
     * {@link AbstractFrame} does automatically add a listener to this event
     * to close the window.
     * <p>
     * A derived panel needs to call {@link #fireQueryClose(boolean)} to fire
     * this event.
     *
     * @return The action.
     */
    public GAction getQueryCloseAction() {
        return queryCloseAction;
    }

    /**
     * Sets the layout manager for this container.
     * <p>
     * This always throws an {@link UnsupportedOperationException} if
     * {@code layout} is other than {@link BorderLayout}.
     */
    @Override
    public final void setLayout(LayoutManager layout) {
        if (layout instanceof BorderLayout)
            super.setLayout(layout);
        else
            throw new UnsupportedOperationException("Changing the layout to other than BorderLayout is not supported in AbstractVPContentPane.");
    }

    /**
     * Inheritors should override this if they want to free used resources.
     * <p>
     * This implementation calls {@code dispose()} of all subpanels that
     * implement {@link Disposable}.
     */
    @Override
    public void dispose() {
        for (Component component : getComponents())
            if (component instanceof Disposable)
                ((Disposable) component).dispose();
    }

    /**
     * Fires the event that queries the owning window to close itself.
     * <p>
     * The window is not closed if there is any modified data that cannot be
     * saved, except the window is not a dialog or {@code applyData} is
     * {@code false}.
     *
     * @param applyData Indicates whether a dialog shall set its "applyData"
     *        indicator. This is ignored in frames.
     */
    @SuppressWarnings("unused")
    protected void fireQueryClose(boolean applyData) {
        queryCloseAction.actionPerformed(new QueryCloseEvent(this, applyData));
    }

    /**
     * Called to create the panel with the main controls of the frame or dialog.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * my not been fully initialized when this is invoked.
     *
     * @return The panel that shall be inserted into the center of the frame or
     *         dialog. This must not be {@code null}.
     */
    protected abstract JComponent createCenterPanel();

    /**
     * Called to create the caption for the title of the dialog or frame.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * my not been fully initialized when this is invoked.
     *
     * @return The title to set or {@code null} if no caption shall be displayed.
     */
    protected abstract JComponent createTitleLabel();

    /**
     * Called to create the image icon of the dialog or frame.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * my not been fully initialized when this is invoked.
     *
     * @return The image icon to display or {@code null} if no image shall be
     *         displayed.
     */
    protected abstract JComponent createTitleIcon();

    /**
     * Called to create a component that contains further controls that shall be
     * displayed in the center of the title panel.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * my not been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The panel with further controls to display in the title or
     *         {@code null} if no further controls shall be inserted.
     */
    protected JComponent createCenteredTitleExtension() {
        return null;
    }

    /**
     * Called to create a component that contains further controls that shall be
     * displayed on the right side of the title panel.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * my not been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The panel with further controls to display in the title or
     *         {@code null} if no further controls shall be inserted.
     */
    protected JComponent createRightTitleExtension() {
        return null;
    }

    /**
     * Called to create the panel that contains further controls that shall be
     * displayed the south of the frame or dialog.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * my not been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The panel with further controls or {@code null} if no controls
     *         are needed.
     */
    protected JComponent createSouthPanel() {
        return null;
    }

    /**
     * Called to create the panel that contains further controls that shall be
     * displayed the east of the frame or dialog.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * my not been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The panel with further controls or {@code null} if no controls
     *         are needed.
     */
    protected JComponent createEastPanel() {
        return null;
    }

    /**
     * Called to create the panel that contains further controls that shall be
     * displayed the west of the frame or dialog.
     * <p>
     * Note that this is called during construction. Therefor derived classes
     * my not been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The panel with further controls or {@code null} if no controls
     *         are needed.
     */
    protected JComponent createWestPanel() {
        return null;
    }

    /**
     * Called to create the panel that shall be inserted into the north.
     * <p>
     * Inheritors may override this if they want a more complex north panel,
     * maybe a toolbar above or below the title. For example:
     * <p>
     * <pre>{@code
     * @Override
     * protected JPanel createTitlePanel() {
     *     var northPanel = new JPanel(new BorderLayout());
     *     northPanel.add(super.createTitlePanel(), BorderLayout.CENTER);
     *     northPanel.add(createToolBar(), BorderLayout.SOUTH);
     *
     *     return northPanel;
     * }
     * }</pre>
     * <p>
     * Note that this is called during construction. Therefor derived classes
     * my not been fully initialized when this is invoked.
     *
     * @return The created panel. Inheritors may return {@code null} if no title
     *          panel is wanted.
     */
    protected JPanel createTitlePanel() {
        JPanel title = new JPanel(null);
        title.setLayout(new BoxLayout(title, BoxLayout.X_AXIS));
        title.setBackground(titleBackground);
        title.setOpaque(true);
        title.setPreferredSize(new Dimension(100, 50));

        fillTitlePanel(title);

        return title;
    }

    /**
     * Called by {@link #createTitlePanel()} to fill the panel with the required
     * controls.
     * <p>
     * Note that this is called during construction. Therefor derived classes
     * my not been fully initialized when this is invoked.
     *
     * @param titlePanel The panel to fill.
     */
    protected void fillTitlePanel(JPanel titlePanel) {
        JComponent icon = createTitleIcon();
        JComponent text = createTitleLabel();

        titlePanel.add(Box.createHorizontalStrut(10));

        if (icon != null) {
            titlePanel.add(icon);
            titlePanel.add(Box.createHorizontalStrut(10));
        }

        if (text != null) {
            text.setFont(new Font("Arial", Font.BOLD, 18));
            text.setForeground(Color.BLACK);

            titlePanel.add(text);
        }

        titlePanel.add(Box.createHorizontalGlue());

        JComponent further = createCenteredTitleExtension();

        if (further != null) {
            titlePanel.add(further);
            titlePanel.add(Box.createHorizontalGlue());
        }

        further = createRightTitleExtension();

        if (further != null) {
            titlePanel.add(further);
            titlePanel.add(Box.createHorizontalStrut(10));
        }

    }

    private void setup() {
        JComponent center = createCenterPanel();
        JComponent title = createTitlePanel();
        JComponent south = createSouthPanel();
        JComponent eastPanel = createEastPanel();
        JComponent westPanel = createWestPanel();

        add(center, BorderLayout.CENTER);

        if (title != null)
            add(title, BorderLayout.NORTH);

        if (south != null)
            add(south, BorderLayout.SOUTH);

        if (eastPanel != null)
            add(eastPanel, BorderLayout.EAST);

        if (westPanel != null)
            add(westPanel, BorderLayout.WEST);
    }
}
