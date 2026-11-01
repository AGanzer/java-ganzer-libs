package de.ganzer.swing.dlgfw;

import de.ganzer.swing.dialogs.DataSupport;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * The panel used together with a {@link MessageDialog}.
 */
public class MessageDialogPanel extends AbstractDataPanel<MessageDialogData> {
    private JLabel titleLabel;
    private JLabel titleIcon;
    private JLabel messageLabel;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     *
     * @param owner The owner window that supports data.
     *
     * @throws NullPointerException {@code owner} is {@code null}.
     */
    public MessageDialogPanel(DataSupport<MessageDialogData> owner) {
        super(owner);
    }

    /**
     * Called to create the panel with the main controls of the frame or dialog.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * may not have been fully initialized when this is invoked.
     *
     * @return The panel that shall be inserted into the center of the frame or
     *         dialog. This must not be {@code null}.
     */
    @Override
    protected JComponent createCenterPanel() {
        return messageLabel = new JLabel();
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
     */
    @Override
    protected JComponent createTitleLabel() {
        return titleLabel = new JLabel();
    }

    /**
     * Called to create the image icon of the dialog or frame.
     * <p>
     * The preferred size of the icon should be {@link #DEFAULT_TITLE_ICON_SIZE}
     * x {@link #DEFAULT_TITLE_ICON_SIZE}.
     * <p>
     * Note that this is called during construction. Therefore, derived classes
     * may not have been fully initialized when this is invoked.
     * <p>
     * This implementation does always return {@code null}.
     *
     * @return The image icon to display or {@code null} if no image shall be
     *         displayed.
     */
    @Override
    protected JComponent createTitleIcon() {
        return titleIcon = new JLabel();
    }

    /**
     * Called to initialize the controls from the specified data.
     *
     * @param messageDialogData The data where to initialize the controls with. This may be
     *         {@code null} if the controls shall be reset.
     */
    @Override
    public void initControls(MessageDialogData messageDialogData) {

    }
}
