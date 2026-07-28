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
 */
public abstract class AbstractFrame extends JFrame {
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
        setup();
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
     * Called to get the text of the dialogs main button.
     * <p>
     * This method can be overridden if no additional buttons should be shown
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

    private void setup() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());

        centerPanel = createCenterPanel();
        buttonPanel = createButtonPanel();

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
