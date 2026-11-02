package de.ganzer.swing.dlgfw.messages;

import de.ganzer.swing.dialogs.DataSupport;
import de.ganzer.swing.dlgfw.AbstractDataPanel;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * The panel used together with a {@link MessageDialog}.
 *
 * @since 6.3.0
 */
public class MessageDialogPanel extends AbstractDataPanel<MessageDialogData> {
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
        messageLabel = new JLabel();
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return messageLabel;
    }

    @Override
    protected JComponent createTitlePanel() {
        return null;
    }

    /**
     * Called to initialize the controls from the specified data.
     *
     * @param data The data where to initialize the controls with. This may be
     *         {@code null} if the controls shall be reset.
     */
    @Override
    public void initControls(MessageDialogData data) {
        if (data.message.startsWith("<html>"))
            messageLabel.setText(data.message);
        else
            messageLabel.setText("<html>" + data.message.replace("\n", "<br>") + "<html>");

        if (data.title == null && data.icon == null)
            remove(getTitlePanel());
    }
}
