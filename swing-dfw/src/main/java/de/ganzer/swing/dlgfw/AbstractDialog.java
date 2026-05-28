package de.ganzer.swing.dlgfw;

import de.ganzer.swing.dlgfw.internals.SwingDialogsMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/**
 * The basic dialog for all modal dialogs.
 * <p>
 * Dialogs that are derived from this class are modal by default. If non-modal
 * dialogs are required that should not always be in front of its parent window,
 * they should be derived from {@link AbstractFrame}.
 * <p>
 * This dialog is closable by the Esc key. If the derived dialog should not be
 * closable by Esc, {@link #createRootPane()} should be overridden to create a
 * default root pane without keyboard handling.
 * <p>
 * The default close operation is set to {@link #DISPOSE_ON_CLOSE}.
 * <p>
 * The contentpane of this class does always have a {@link BorderLayout} layout
 * manager and implements a factory method that fills the center and the buttons.
 * These panels should be created in the derived classes by implementing
 * {@link #createCenterPanel()} and {@link #createButtonPanel()}.
 */
public abstract class AbstractDialog extends JDialog {
    private boolean accepted = true;
    private JPanel centerPanel;
    private JPanel buttonPanel;

    /**
     * Creates a modal dialog with the specified {@code Frame}
     * as its owner and an empty title. If {@code owner}
     * is {@code null}, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     * <p>
     * NOTE: This constructor does not allow you to create an unowned
     * {@code JDialog}. To create an unowned {@code JDialog}
     * you must use either the {@code JDialog(Window)} or
     * {@code JDialog(Dialog)} constructor with an argument of
     * {@code null}.
     *
     * @param owner the {@code Frame} from which the dialog is displayed
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDialog(Frame owner) {
        super(owner, true);
        setup();
    }

    /**
     * Creates a modal dialog with the specified title and
     * with the specified owner frame.
     * If {@code owner} is {@code null}, a shared, hidden frame will be set as
     * the owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     * <p>
     * NOTE: This constructor does not allow you to create an unowned
     * {@code JDialog}. To create an unowned {@code JDialog}
     * you must use either the {@code JDialog(Window)} or
     * {@code JDialog(Dialog)} constructor with an argument of
     * {@code null}.
     *
     * @param owner the {@code Frame} from which the dialog is displayed
     * @param title the {@code String} to display in the dialog's
     *         title bar
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDialog(Frame owner, String title) {
        super(owner, title, true);
        setup();
    }

    /**
     * Creates a modal dialog with the specified title, owner {@code Frame},
     * and {@code GraphicsConfiguration}.
     * If {@code owner} is {@code null},
     * a shared, hidden frame will be set as the owner of this dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     * <p>
     * NOTE: Any popup components ({@code JComboBox},
     * {@code JPopupMenu}, {@code JMenuBar})
     * created within a modal dialog will be forced to be lightweight.
     * <p>
     * NOTE: This constructor does not allow you to create an unowned
     * {@code JDialog}. To create an unowned {@code JDialog}
     * you must use either the {@code JDialog(Window)} or
     * {@code JDialog(Dialog)} constructor with an argument of
     * {@code null}.
     *
     * @param owner the {@code Frame} from which the dialog is displayed
     * @param title the {@code String} to display in the dialog's
     *         title bar
     * @param gc the {@code GraphicsConfiguration} of the target screen device;
     *         if {@code null}, the default system {@code GraphicsConfiguration}
     *         is assumed
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see ModalityType
     * @see ModalityType#MODELESS
     * @see Dialog#DEFAULT_MODALITY_TYPE
     * @see Dialog#setModal
     * @see Dialog#setModalityType
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     * @since 1.4
     */
    public AbstractDialog(Frame owner, String title, GraphicsConfiguration gc) {
        super(owner, title, true, gc);
        setup();
    }

    /**
     * Creates a modal dialog with the specified {@code Dialog}
     * as its owner and an empty title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the owner {@code Dialog} from which the dialog is displayed
     *         or {@code null} if this dialog has no owner
     *
     * @throws HeadlessException {@code if GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDialog(Dialog owner) {
        super(owner, true);
        setup();
    }

    /**
     * Creates a modal dialog with the specified title and
     * with the specified owner dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the owner {@code Dialog} from which the dialog is displayed
     *         or {@code null} if this dialog has no owner
     * @param title the {@code String} to display in the dialog's
     *         title bar
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDialog(Dialog owner, String title) {
        super(owner, title, true);
        setup();
    }

    /**
     * Creates a modal dialog with the specified title, owner {@code Dialog}
     * and {@code GraphicsConfiguration}.
     *
     * <p>
     * NOTE: Any popup components ({@code JComboBox},
     * {@code JPopupMenu}, {@code JMenuBar})
     * created within a modal dialog will be forced to be lightweight.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the owner {@code Dialog} from which the dialog is displayed
     *         or {@code null} if this dialog has no owner
     * @param title the {@code String} to display in the dialog's
     *         title bar
     * @param gc the {@code GraphicsConfiguration} of the target screen device;
     *         if {@code null}, the default system {@code GraphicsConfiguration}
     *         is assumed
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDialog(Dialog owner, String title, GraphicsConfiguration gc) {
        super(owner, title, true, gc);
        setup();
    }

    /**
     * Creates a modal dialog with the specified {@code Window}
     * as its owner and an empty title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner
     *
     * @throws IllegalArgumentException if the {@code owner} is not an instance
     *         of {@link Dialog Dialog} or {@link Frame Frame}
     * @throws IllegalArgumentException if the {@code owner}'s
     *         {@code GraphicsConfiguration} is not from a screen device
     * @throws HeadlessException when {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDialog(Window owner) {
        super(owner, DEFAULT_MODALITY_TYPE);
        setup();
    }

    /**
     * Creates a modal dialog with the specified title and owner
     * {@code Window}.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner
     * @param title the {@code String} to display in the dialog's
     *         title bar or {@code null} if the dialog has no title
     *
     * @throws IllegalArgumentException if the {@code owner} is not an instance
     *         of {@link Dialog Dialog} or {@link Frame Frame}
     * @throws IllegalArgumentException if the {@code owner}'s
     *         {@code GraphicsConfiguration} is not from a screen device
     * @throws HeadlessException when {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDialog(Window owner, String title) {
        super(owner, title, DEFAULT_MODALITY_TYPE);
        setup();
    }

    /**
     * Creates a modal dialog with the specified title, owner {@code Window}
     * and {@code GraphicsConfiguration}.
     * <p>
     * NOTE: Any popup components ({@code JComboBox},
     * {@code JPopupMenu}, {@code JMenuBar})
     * created within a modal dialog will be forced to be lightweight.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner
     * @param title the {@code String} to display in the dialog's
     *         title bar or {@code null} if the dialog has no title
     * @param gc the {@code GraphicsConfiguration} of the target screen device;
     *         if {@code null}, the default system {@code GraphicsConfiguration}
     *         is assumed
     *
     * @throws IllegalArgumentException if the {@code owner} is not an instance
     *         of {@link Dialog Dialog} or {@link Frame Frame}
     * @throws IllegalArgumentException if the {@code owner}'s
     *         {@code GraphicsConfiguration} is not from a screen device
     * @throws HeadlessException when {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDialog(Window owner, String title, GraphicsConfiguration gc) {
        super(owner, title, DEFAULT_MODALITY_TYPE, gc);
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
     * Sends a {@link WindowEvent#WINDOW_CLOSING} event to this dialog.
     *
     * @param applyData Indicates whether modified data shall be applied.
     *         {@link #isAccepted()} returns {@code true} if this is {@code true}.
     */
    public void closeDialog(boolean applyData) {
        this.accepted = applyData;
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Gets a value that indicates whether the dialog is closed to accept
     * changed data.
     *
     * @return {@code true} if the dialog is closed by an OK button and
     *         {@code false} if the dialog is closed in any other way.
     */
    public final boolean isAccepted() {
        return accepted;
    }

    /**
     * Resets the accepted flag to its origin value.
     * <p>
     * Inheritors can use this to reset the flag when a dialog cannot be closed
     * by the OK button because of invalid data.
     *
     * @see #isAccepted()
     */
    protected void resetAccepted() {
        accepted = false;
    }

    private static final String ACTION_MAP_KEY = "EscKeyClick-de.velocom.veloport.dialog";

    /**
     * Calls {@link JDialog#createRootPane()} and adds an Esc key to the action
     * map of the root pane to dispatch a {@link WindowEvent#WINDOW_CLOSING}
     * event.
     *
     * @return The created root pane.
     */
    @Override
    protected JRootPane createRootPane() {
        JRootPane root = super.createRootPane();

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                ACTION_MAP_KEY);
        root.getActionMap().put(ACTION_MAP_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog(true);
            }
        });

        return root;
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
     * Called to create the panel that contains the main buttons of the frame
     * or dialog.
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
     *          implementation returns a Close button.
     */
    protected AbstractButton[] createWindowButtons() {
        JButton closeButton = new JButton(getMainButtonText());
        closeButton.addActionListener(e -> closeDialog(false));

        getRootPane().setDefaultButton(closeButton);

        return new AbstractButton[] {closeButton};
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

    private void setup() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        centerPanel = createCenterPanel();
        buttonPanel = createButtonPanel();

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        if (centerPanel instanceof AbstractPanel panel) {
            panel.getQueryCloseAction().addActionListener(
                    e -> closeDialog(QueryCloseEvent.APPLY_DATA_COMMAND.equals(e.getActionCommand())));
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
