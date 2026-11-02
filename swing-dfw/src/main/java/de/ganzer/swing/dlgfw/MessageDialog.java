package de.ganzer.swing.dlgfw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * A dialog that displays a message.
 *
 * @since 6.3.0
 */
public class MessageDialog extends AbstractModifiableDataDialog<MessageDialogData> {
    private JLabel titleLabel;
    private JLabel titleIcon;
    private String result;

    /**
     * Shows the dialog with the given data.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param data The data to use.
     *
     * @return {@code data.getResultIndex()}.
     */
    public static int show(Window owner, MessageDialogData data) {
        new MessageDialog(owner, data).setVisible(true);
        return data.getResultIndex();
    }

    /**
     * Shows the dialog with an information title and icon and an "OK" button.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     */
    public static void show(Window owner, String message) {
        show(owner, new MessageDialogData(message));
    }

    /**
     * Shows the dialog with a default title and icon.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     */
    public static void show(Window owner, String title, String message) {
        show(owner, new MessageDialogData(title, message));
    }

    /**
     * Shows the dialog with a default title and icon.
     * <p>
     * This shown buttons depends on {@code type}. If this is {@link MessageType#QUESTION},
     * a "YES" and "NO" button is shown. In all other cases a single "OK" button.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown.
     */
    public static void show(Window owner, String message, MessageType type) {
        show(owner, new MessageDialogData(message, type));
    }

    /**
     * Shows the dialog with a default title and icon.
     * <p>
     * This shown buttons depends on {@code type}. If this is {@link MessageType#QUESTION},
     * a "YES" and "NO" button is shown. In all other cases a single "OK" button.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown.
     */
    public static void show(Window owner, String title, String message, MessageType type) {
        show(owner, new MessageDialogData(title, message, type));
    }

    /**
     * Shows the dialog with a question icon and "Yes" and "No" buttons.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     *
     * @return {@code true} if the user clicked "Yes", {@code false} otherwise.
     */
    public static boolean queryYesNo(Window owner, String message) {
        return queryYesNo(owner, null, message);
    }

    /**
     * Shows the dialog with a question icon and "Yes" and "No" buttons.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     *
     * @return {@code true} if the user clicked "Yes", {@code false} otherwise.
     */
    public static boolean queryYesNo(Window owner, String title, String message) {
        var data = new MessageDialogData(title, message, MessageType.QUESTION);
        show(owner, data);

        return MessageButton.YES.equals(data.result);
    }

    /**
     * Shows the dialog with a question icon and "Yes", "No" and "Cancel" buttons.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     *
     * @return {@code true} if the user clicked "Yes", {@code false} if the user
     *          clicked "No", or {@code null} if the user clicked "Cancel".
     */
    public static Boolean queryYesNoCancel(Window owner, String message) {
        return queryYesNoCancel(owner, null, message);
    }

    /**
     * Shows the dialog with a question icon and "Yes", "No" and "Cancel" buttons.
     *
     * @param owner The {@code Window} from which the dialog is displayed or
     *         {@code null} if this dialog has no owner.
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     *
     * @return {@code true} if the user clicked "Yes", {@code false} if the user
     *          clicked "No", or {@code null} if the user clicked "Cancel".
     */
    public static Boolean queryYesNoCancel(Window owner, String title, String message) {
        var data = new MessageDialogData(title,
                                         message,
                                         MessageButtons.YES_NO_CANCEL,
                                         null,
                                         MessageType.QUESTION);
        show(owner, data);

        if (MessageButton.YES.equals(data.result))
            return true;

        if (MessageButton.NO.equals(data.result))
            return false;

        return null;
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
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws IllegalArgumentException if the {@code owner} is not an instance
     *         of {@link Dialog} or {@link Frame}
     * @throws IllegalArgumentException if the {@code owner}'s
     *         {@code GraphicsConfiguration} is not from a screen device
     * @throws HeadlessException when {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public MessageDialog(Window owner, MessageDialogData data) {
        super(owner, true, data);
    }

    /**
     * Sets up the controls of the dialog.
     * <p>
     * Inheritors must call the base method to ensure that all controls are
     * created.
     *
     * @throws IllegalStateException If {@code setup()} was already invoked.
     */
    @Override
    protected void setup() {
        super.setup();
        pack();
        setLocationRelativeTo(getOwner());
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
    @Override
    protected JPanel createCenterPanel() {
        return new MessageDialogPanel(this);
    }

    @Override
    protected boolean shouldCreateTitle() {
        return true;
    }

    /**
     * Called to create the caption for the title of the dialog or frame.
     *
     * @return The title to set.
     */
    @Override
    protected JComponent createTitleLabel() {
        return titleLabel = new JLabel();
    }

    /**
     * Called to create the image icon of the dialog or frame.
     *
     * @return The image icon to display.
     */
    @Override
    protected JComponent createTitleIcon() {
        return titleIcon = new JLabel();
    }

    /**
     * Called to create the buttons that shall be inserted as the window's main
     * buttons.
     *
     * @return The buttons to insert or {@code null} to insert no buttons. This
     *         implementation returns a Close button.
     */
    @Override
    protected AbstractButton[] createWindowButtons() {
        AbstractButton[] buttons = new AbstractButton[getData().buttons.length];
        JButton defButton = null;

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(getData().buttons[i]);
            buttons[i].setActionCommand(getData().buttons[i]);
            buttons[i].addActionListener(e -> {
                result = e.getActionCommand();
                closeDialog(true);
            });

            if (getData().buttons[i].equals(getData().defaultButton))
                defButton = (JButton) buttons[i];
        }

        if (defButton == null)
            defButton = (JButton) buttons[0];

        getRootPane().setDefaultButton(defButton);

        SwingUtilities.invokeLater(defButton::requestFocusInWindow);

        return buttons;
    }

    /**
     * Gets the location of the button panel if any is created.
     * <p>
     * Inheritors should return either  {@link BorderLayout#SOUTH} or
     * {@link BorderLayout#EAST}.
     *
     * @return This implementation does always return {@link BorderLayout#SOUTH}.
     */
    @Override
    protected String getButtonPanelLocation() {
        return getData().buttonLocation;
    }

    /**
     * Called to initialize the controls from the specified data.
     *
     * @param data The data where to initialize the controls with. This may be
     *         {@code null} if the controls shall be reset.
     */
    @Override
    public void initControls(MessageDialogData data) {
        super.initControls(data);

        if (titleLabel != null) {
            titleLabel.setText(data.title);
            titleIcon.setIcon(data.icon);
        }
    }

    /**
     * Called to update the data that was given at construction with the users
     * input.
     * <p>
     * This implementation tests whether the set center panel implements
     *
     * @param data The data to update. This is identical to {@link #getData()}.
     */
    @Override
    protected void updateData(MessageDialogData data) {
        super.updateData(data);
        data.result = result;
    }

    /**
     * Handles the {@link WindowEvent#WINDOW_CLOSING} event.
     * <p>
     * If the dialog is modal, {@link #applyChangedData()} is invoked if
     * {@link #isAccepted()} is {@code true}.
     * <p>
     * If the dialog is not modal, the {@link #queryUserToSave()} is invoked
     * if {@link #isDataModified()} is {@code true}.
     * <p>
     * This implementation calls {@link #queryUserToSave()}. The recognized
     * answers are:
     * <ul>
     *     <li>{@code true}: {@link #applyChangedData()} is called. On success,
     *          the window is closed; otherwise, the event is consumed and the
     *          window is not closed.</li>
     *     <li>{@code false}: The window is closed without any further action.
     *          </li>
     *     <li>{@code null}: The event is consumed and the window is not closed.
     *         </li>
     * </ul>
     * If the dialog is not closed {@link #resetAccepted()} is invoked.
     *
     * @param e The window event.
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            setDataModified(true);

            if (result == null) {
                if (getData().buttons.length == 1) {
                    result = getData().buttons[0];
                } else {
                    for (int i = 0; i < getData().buttons.length; i++) {
                        if (getData().buttons[i].equals(MessageButton.CANCEL)) {
                            result = MessageButton.CANCEL;
                            break;
                        }
                    }
                }

                if (result == null)
                    return;
            }
        }

        super.processWindowEvent(e);
    }
}
