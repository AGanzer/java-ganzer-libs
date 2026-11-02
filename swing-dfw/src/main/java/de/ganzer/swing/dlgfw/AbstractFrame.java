package de.ganzer.swing.dlgfw;

import de.ganzer.swing.dlgfw.internals.SwingDialogsMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * The basic frame for non-modal dialogs.
 * <p>
 * The default close operation is set to {@link #DISPOSE_ON_CLOSE}.
 * <p>
 * The contentpane of this class does always have a {@link BorderLayout} layout
 * manager and implements a factory method that fills the center and the buttons.
 * These panels should be created in the derived classes by implementing
 * {@link #createCenterPanel()} and {@link #createButtonPanel()}.
 *
 * @since 6.0.0
 */
public abstract class AbstractFrame extends JFrame {
    private final boolean lazySetup;

    private JPanel titlePanel;
    private JPanel centerPanel;
    private JPanel buttonPanel;

    /**
     * Constructs a new frame that is initially invisible.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *         returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public AbstractFrame() throws HeadlessException {
        lazySetup = false;
        setup();
    }

    /**
     * Creates a <code>Frame</code> in the specified
     * <code>GraphicsConfiguration</code> of
     * a screen device and a blank title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param gc the <code>GraphicsConfiguration</code> that is used
     *         to construct the new <code>Frame</code>;
     *         if <code>gc</code> is <code>null</code>, the system
     *         default <code>GraphicsConfiguration</code> is assumed
     *
     * @throws IllegalArgumentException if <code>gc</code> is not from
     *         a screen device.  This exception is always thrown when
     *         GraphicsEnvironment.isHeadless() returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractFrame(GraphicsConfiguration gc) {
        super(gc);
        lazySetup = false;
        setup();
    }

    /**
     * Creates an initially invisible <code>Frame</code> with the specified
     * title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title for the frame
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *         returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public AbstractFrame(String title) throws HeadlessException {
        super(title);
        lazySetup = false;
        setup();
    }

    /**
     * Creates a <code>JFrame</code> with the specified title and the
     * specified <code>GraphicsConfiguration</code> of a screen device.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title to be displayed in the
     *         frame's border. A <code>null</code> value is treated as
     *         an empty string, "".
     * @param gc the <code>GraphicsConfiguration</code> that is used
     *         to construct the new <code>JFrame</code> with;
     *         if <code>gc</code> is <code>null</code>, the system
     *         default <code>GraphicsConfiguration</code> is assumed
     *
     * @throws IllegalArgumentException if <code>gc</code> is not from
     *         a screen device.  This exception is always thrown when
     *         GraphicsEnvironment.isHeadless() returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
        lazySetup = false;
        setup();
    }

    /**
     * Constructs a new frame that is initially invisible.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *         returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     *
     * @since 6.3.0
     */
    public AbstractFrame(boolean suppressSetup) throws HeadlessException {
        lazySetup = suppressSetup;

        if (!suppressSetup)
            setup();
    }

    /**
     * Creates a <code>Frame</code> in the specified
     * <code>GraphicsConfiguration</code> of
     * a screen device and a blank title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param gc the <code>GraphicsConfiguration</code> that is used
     *         to construct the new <code>Frame</code>;
     *         if <code>gc</code> is <code>null</code>, the system
     *         default <code>GraphicsConfiguration</code> is assumed
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
     *
     * @throws IllegalArgumentException if <code>gc</code> is not from
     *         a screen device.  This exception is always thrown when
     *         GraphicsEnvironment.isHeadless() returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     *
     * @since 6.3.0
     */
    public AbstractFrame(GraphicsConfiguration gc, boolean suppressSetup) {
        super(gc);

        lazySetup = suppressSetup;

        if (!suppressSetup)
            setup();
    }

    /**
     * Creates an initially invisible <code>Frame</code> with the specified
     * title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title for the frame
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *         returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     *
     * @since 6.3.0
     */
    public AbstractFrame(String title, boolean suppressSetup) throws HeadlessException {
        super(title);

        lazySetup = suppressSetup;

        if (!suppressSetup)
            setup();
    }

    /**
     * Creates a <code>JFrame</code> with the specified title and the
     * specified <code>GraphicsConfiguration</code> of a screen device.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title to be displayed in the
     *         frame's border. A <code>null</code> value is treated as
     *         an empty string, "".
     * @param gc the <code>GraphicsConfiguration</code> that is used
     *         to construct the new <code>JFrame</code> with;
     *         if <code>gc</code> is <code>null</code>, the system
     *         default <code>GraphicsConfiguration</code> is assumed
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
     *
     * @throws IllegalArgumentException if <code>gc</code> is not from
     *         a screen device.  This exception is always thrown when
     *         GraphicsEnvironment.isHeadless() returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     *
     * @since 6.3.0
     */
    public AbstractFrame(String title, GraphicsConfiguration gc, boolean suppressSetup) {
        super(title, gc);

        lazySetup = suppressSetup;

        if (!suppressSetup)
            setup();
    }

    /**
     * Gets the title panel.
     *
     * @return The panel that was created by {@link #createTitlePanel()} or
     *         {@code null} if {@link #createTitlePanel()} did not create a
     *         panel.
     *
     * @see #shouldCreateTitle()
     *
     * @since 6.3.0
     */
    public JComponent getTitlePanel() {
        return titlePanel;
    }

    /**
     * Gets the center panel of this dialog.
     *
     * @return The center panel.
     */
    public JPanel getCenterPanel() {
        return centerPanel;
    }

    /**
     * Gets the button panel of this dialog.
     *
     * @return The button panel of {@code null} if no button panel is set.
     */
    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    /**
     * Shows or hides this {@code Dialog} depending on the value of parameter
     * {@code set}.
     * <p>
     * This implementation invokes {@link #setup()} if this is not already done
     * and {@code set} is {@code true}.
     *
     * @param set if {@code true}, makes the {@code Dialog} visible, otherwise
     *         hides the {@code Dialog}.
     *
     * @see Window#setVisible
     * @see Window#dispose
     * @see Component#isDisplayable
     * @see Component#validate
     * @see Dialog#isModal
     */
    @Override
    public void setVisible(boolean set) {
        if (set && centerPanel == null)
            setup();

        super.setVisible(set);
    }

    /**
     * Releases all of the native screen resources used by this
     * {@code Window}, its subcomponents, and all of its owned
     * children. That is, the resources for these {@code Component}s
     * will be destroyed, any memory they consume will be returned to the
     * OS, and they will be marked as undisplayable.
     * <p>
     * The {@code Window} and its subcomponents can be made displayable
     * again by rebuilding the native resources with a subsequent call to
     * {@code pack} or {@code show}. The states of the recreated
     * {@code Window} and its subcomponents will be identical to the
     * states of these objects at the point where the {@code Window}
     * was disposed (not accounting for additional modifications between
     * those actions).
     * <p>
     * <b>Note</b>: When the last displayable window
     * within the Java virtual machine (VM) is disposed of, the VM may
     * terminate.  See <a href="doc-files/AWTThreadIssues.html#Autoshutdown">
     * AWT Threading Issues</a> for more information.
     */
    @Override
    public void dispose() {
        super.dispose();

        if (centerPanel instanceof Disposable)
            ((Disposable) centerPanel).dispose();

        if (buttonPanel instanceof Disposable)
            ((Disposable) buttonPanel).dispose();
    }

    /**
     * Indicates whether the dialog was constructed with lazy setup.
     *
     * @return {@code true} if the dialog was constructed with lazy setup.
     *
     * @see #AbstractFrame(boolean)
     * @see #AbstractFrame(String, boolean)
     * @see #AbstractFrame(String, GraphicsConfiguration, boolean)
     *
     * @since 6.3.0
     */
    protected boolean isLazySetup() {
        return lazySetup;
    }

    /**
     * Called to create the panel with the main controls of the frame or dialog.
     * <p>
     * Note that this is called during construction. Therefor derived classes
     * my not been fully initialized when this is invoked.
     *
     * @return The panel that shall be inserted into the center of the frame or
     *         dialog. This must not be {@code null}.
     */
    protected abstract JPanel createCenterPanel();

    /**
     * Indicates whether a title panel shall be created.
     * <p>
     * inheritors can override this to create a title panel within the window
     * instead of within the center panel.
     *
     * @return {@code true} if a title shall be created. This implementation
     *         returns always {@code false}.
     *
     * @since 6.3.0
     */
    protected boolean shouldCreateTitle() {
        return false;
    }

    /**
     * Called to create the caption for the title of the dialog or frame.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * may not have been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The title to set or {@code null} if no caption shall be displayed.
     *
     * @since 6.3.0
     */
    protected JComponent createTitleLabel() {
        return null;
    }

    /**
     * Called to create the image icon of the dialog or frame.
     * <p>
     * The preferred size of the icon should be {@link AbstractPanel#DEFAULT_TITLE_ICON_SIZE}
     * x {@link AbstractPanel#DEFAULT_TITLE_ICON_SIZE}.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * may not have been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The image icon to display or {@code null} if no image shall be
     *         displayed.
     *
     * @since 6.3.0
     */
    protected JComponent createTitleIcon() {
        return null;
    }

    /**
     * Called to create a component that contains further controls that shall be
     * displayed in the center of the title panel.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * may not have been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The panel with further controls to display in the title or
     *         {@code null} if no further controls shall be inserted.
     *
     * @since 6.3.0
     */
    protected JComponent createCenteredTitleExtension() {
        return null;
    }

    /**
     * Called to create a component that contains further controls that shall be
     * displayed on the right side of the title panel.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * may not have been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The panel with further controls to display in the title or
     *         {@code null} if no further controls shall be inserted.
     *
     * @since 6.3.0
     */
    protected JComponent createRightTitleExtension() {
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
     * Note that this is called only if {@link #shouldCreateTitle()} returns
     * {@code true}.
     *
     * @return The created panel.
     *
     * @since 6.3.0
     */
    protected JPanel createTitlePanel() {
        JPanel title = new JPanel(null);
        title.setLayout(new BoxLayout(title, BoxLayout.X_AXIS));
        title.setBackground(AbstractPanel.getTitleBackground());
        title.setOpaque(true);

        fillTitlePanel(title);

        title.setPreferredSize(new Dimension(title.getPreferredSize().width, AbstractPanel.getPreferredTitleHeight()));

        return title;
    }

    /**
     * Called by {@link #createTitlePanel()} to fill the panel with the required
     * controls.
     * <p>
     * Note that this is called during construction. Therefor derived classes
     * may not have been fully initialized when this is invoked.
     *
     * @param titlePanel The panel to fill.
     *
     * @since 6.3.0
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
            text.setFont(AbstractPanel.getTitleFont());
            text.setForeground(AbstractPanel.getTitleForeground());

            titlePanel.add(text);
        }

        titlePanel.add(Box.createHorizontalGlue());

        JComponent further = createCenteredTitleExtension();

        if (further != null) {
            titlePanel.add(further);
            titlePanel.add(Box.createHorizontalGlue());
        }

        further = createRightTitleExtension();

        if (further != null)
            titlePanel.add(further);

        titlePanel.add(Box.createHorizontalStrut(10));
    }

    /**
     * Called to create the panel that contains the main buttons of the window.
     * <p>
     * This should only be overridden when a specialized layout is required.
     * In all other cases, {@link #createWindowButtons()} should be overridden.
     * <p>
     * Note that this is called during construction. Therefor derived classes
     * my not been fully initialized when this is invoked.
     * <p>
     * The created panel is inserted into the location given by
     * {@link #getButtonPanelLocation()}.
     *
     * @return The panel with the buttons or {@code null} if
     *          {@link #createWindowButtons()} returns {@code null}.
     *
     * @see #createWindowButtons()
     * @see #shouldCenterButtons()
     * @see #shouldResizeButtons()
     */
    @SuppressWarnings("DuplicatedCode")
    protected JPanel createButtonPanel() {
        AbstractButton[] buttons = createWindowButtons();

        if (buttons == null || buttons.length == 0)
            return null;

        JPanel panel = new JPanel(null);
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);

        boolean vertical = BorderLayout.EAST.equals(getButtonPanelLocation()) || BorderLayout.WEST.equals(getButtonPanelLocation());

        if (vertical)
            layoutVertically(layout, buttons);
        else
            layoutHorizontally(layout, buttons);

        panel.setLayout(layout);

        if (vertical)
            panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        else
            panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        return panel;
    }

    /**
     * Called to create the buttons that shall be inserted as the window's main
     * buttons.
     *
     * @return The buttons to insert or {@code null} to insert no buttons. This
     *         implementation does always return a Close button.
     */
    protected AbstractButton[] createWindowButtons() {
        JButton button = new  JButton(getMainButtonText());
        button.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        return new AbstractButton[] { button };
    }

    /**
     * Called to get the text of the dialog's main button.
     * <p>
     * This method can be overridden if no additional buttons should be shown,
     * but the text "Close" is not applicable.
     *
     * @return The text to use for the main button. This implementation does
     *         always return "Close".
     */
    protected String getMainButtonText() {
        return SwingDialogsMessages.get("commons.buttons.close");
    }

    /**
     * Called to get the justifications of the buttons within the window.
     * <p>
     * This is not called when {@link #getButtonPanelLocation()} returns
     * {@link BorderLayout#EAST} or {@link BorderLayout#WEST}. In these cases,
     * the buttons are always justified at the top edge of the window.
     *
     * @return {@code true} to center the buttons, {@code false} to justify it
     *          at the right edge of the window. This implementation does
     *          always return {@code true}.
     */
    protected boolean shouldCenterButtons() {
        return true;
    }

    /**
     * This is called to query whether the buttons shall all have the same
     * width.
     * <p>
     * This is not called when {@link #getButtonPanelLocation()} returns
     * {@link BorderLayout#EAST} or {@link BorderLayout#WEST}. In these cases,
     * the buttons are always resized to have the same width.
     *
     * @return {@code true} to make all buttons have the same width; otherwise,
     *          the buttons are not resized. This implementation does always
     *          return {@code true}.
     */
    protected boolean shouldResizeButtons() {
        return true;
    }

    /**
     * Gets the location of the button panel if any is created.
     * <p>
     * Inheritors should return either  {@link BorderLayout#SOUTH} or
     * {@link BorderLayout#EAST}.
     *
     * @return This implementation does always return {@link BorderLayout#SOUTH}.
     */
    protected String getButtonPanelLocation() {
        return BorderLayout.SOUTH;
    }

    /**
     * Sets up the controls of the frame.
     * <p>
     * Inheritors must call the base method to ensure that all controls are
     * created.
     *
     * @throws IllegalStateException If {@code setup()} was already invoked.
     */
    protected void setup() {
        if (centerPanel != null)
            throw new IllegalStateException("Controls are already created.");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());

        titlePanel = shouldCreateTitle() ? createTitlePanel() : null;
        centerPanel = createCenterPanel();
        buttonPanel = createButtonPanel();

        if (titlePanel != null)
            getContentPane().add(titlePanel, BorderLayout.NORTH);

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        if (centerPanel instanceof AbstractPanel panel) {
            panel.getQueryCloseAction().addActionListener(
                    e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        }

        if (buttonPanel != null)
            getContentPane().add(buttonPanel, getButtonPanelLocation());
    }

    @SuppressWarnings("DuplicatedCode")
    private void layoutVertically(GroupLayout layout, AbstractButton[] buttons) {
        GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup();

        for (AbstractButton button : buttons)
            parallelGroup.addComponent(button);

        horizontalGroup.addGroup(parallelGroup);

        GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();

        for (AbstractButton button : buttons)
            verticalGroup.addComponent(button);

        verticalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        layout.setHorizontalGroup(horizontalGroup);
        layout.setVerticalGroup(verticalGroup);

        layout.linkSize(SwingConstants.HORIZONTAL, buttons);
    }

    @SuppressWarnings("DuplicatedCode")
    private void layoutHorizontally(GroupLayout layout, AbstractButton[] buttons) {
        GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
        horizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        for (AbstractButton button : buttons)
            horizontalGroup.addComponent(button);

        if (shouldCenterButtons())
            horizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

        for (AbstractButton button : buttons)
            parallelGroup.addComponent(button);

        verticalGroup.addGroup(parallelGroup);

        layout.setHorizontalGroup(horizontalGroup);
        layout.setVerticalGroup(verticalGroup);

        if (shouldResizeButtons())
            layout.linkSize(SwingConstants.HORIZONTAL, buttons);
    }
}
