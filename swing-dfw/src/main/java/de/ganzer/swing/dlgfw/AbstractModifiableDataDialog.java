package de.ganzer.swing.dlgfw;

import de.ganzer.core.services.ServiceProvider;
import de.ganzer.swing.dialogs.ModifiableDataSupport;
import de.ganzer.swing.dlgfw.internals.SwingDialogsMessages;
import de.ganzer.swing.dlgfw.services.NavigationService;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

/**
 * The basic dialog for all modal dialogs that modifies external data.
 *
 * @param <Data> The type of the accepted data.
 */
@SuppressWarnings("unused")
public abstract class AbstractModifiableDataDialog<Data> extends AbstractDataDialog<Data> implements ModifiableDataSupport<Data> {
    private Consumer<Data> dataConsumer;
    private boolean dataModified;
    private JButton okButton;

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
    public AbstractModifiableDataDialog(Frame owner) {
        super(owner);
    }

    /**
     * Creates a modal dialog with the specified title and
     * with the specified owner frame.  If {@code owner}
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
     * @param title the {@code String} to display in the dialog's
     *         title bar
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractModifiableDataDialog(Frame owner, String title) {
        super(owner, title);
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
     */
    public AbstractModifiableDataDialog(Frame owner, String title, GraphicsConfiguration gc) {
        super(owner, title, gc);
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
    public AbstractModifiableDataDialog(Dialog owner) {
        super(owner);
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
    public AbstractModifiableDataDialog(Dialog owner, String title) {
        super(owner, title);
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
    public AbstractModifiableDataDialog(Dialog owner, String title, GraphicsConfiguration gc) {
        super(owner, title, gc);
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
    public AbstractModifiableDataDialog(Window owner) {
        super(owner);
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
    public AbstractModifiableDataDialog(Window owner, String title) {
        super(owner, title);
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
    public AbstractModifiableDataDialog(Window owner, String title, GraphicsConfiguration gc) {
        super(owner, title, gc);
    }

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
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractModifiableDataDialog(Frame owner, Data data) {
        super(owner, data);
    }

    /**
     * Creates a modal dialog with the specified title and
     * with the specified owner frame.  If {@code owner}
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
     * @param title the {@code String} to display in the dialog's
     *         title bar
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractModifiableDataDialog(Frame owner, String title, Data data) {
        super(owner, title, data);
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
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
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
     */
    public AbstractModifiableDataDialog(Frame owner, String title, GraphicsConfiguration gc, Data data) {
        super(owner, title, gc, data);
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
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws HeadlessException {@code if GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractModifiableDataDialog(Dialog owner, Data data) {
        super(owner, data);
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
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractModifiableDataDialog(Dialog owner, String title, Data data) {
        super(owner, title, data);
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
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractModifiableDataDialog(Dialog owner, String title, GraphicsConfiguration gc, Data data) {
        super(owner, title, gc, data);
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
     *         of {@link Dialog Dialog} or {@link Frame Frame}
     * @throws IllegalArgumentException if the {@code owner}'s
     *         {@code GraphicsConfiguration} is not from a screen device
     * @throws HeadlessException when {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractModifiableDataDialog(Window owner, Data data) {
        super(owner, data);
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
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
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
    public AbstractModifiableDataDialog(Window owner, String title, Data data) {
        super(owner, title, data);
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
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
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
    public AbstractModifiableDataDialog(Window owner, String title, GraphicsConfiguration gc, Data data) {
        super(owner, title, gc, data);
    }

    /**
     * Invokes the set data consumer if the data is valid and modified.
     * <p>
     * This implementation firstly calls {@link #validateModifiedData()}. If the
     * data is not valid, the method returns {@code false}; otherwise,
     * {@link #updateData(Object)} is called and the set data consumer is
     * invoked and the modification flag is set to {@code false}.
     * <p>
     * if {@link #isDataModified()} is {@code false} or {@link #getData()}
     * returns {@code null}, nothing is done and this implementation returns
     * {@code true}.
     *
     * @return {@code true} if the data is valid and the consumer is invoked;
     *         otherwise, {@code false}.
     *
     * @see #isDataModified()
     * @see #setDataModified(boolean)
     */
    @Override
    public boolean applyChangedData() {
        if (!isDataModified() || getData() == null)
            return true;

        if (!validateModifiedData())
            return false;

        updateData(getData());

        if (dataConsumer != null)
            dataConsumer.accept(getData());

        setDataModified(false);

        return true;
    }

    /**
     * Sets the consumer that is invoked when the dialog is closed by the OK
     * button or whenever an Apply button is clicked within the dialog to
     * notify the client about changed data.
     *
     * @param dataConsumer The consumer to set.
     */
    @Override
    public void setDataConsumer(Consumer<Data> dataConsumer) {
        this.dataConsumer = dataConsumer;
    }

    /**
     * Gets a value the indicates whether the data is modified by the user.
     *
     * @return {@code true} if the data is modified; otherwise, {@code false}.
     */
    @Override
    public boolean isDataModified() {
        return dataModified;
    }

    /**
     * Sets the modification flag of the data.
     *
     * @param modified {@code true} to mark the data as modified.
     */
    @Override
    public void setDataModified(boolean modified) {
        this.dataModified = modified;

        if (okButton != null)
            okButton.setEnabled(modified);
    }

    /**
     * Called to create the buttons that shall be inserted as the window's main
     * buttons.
     *
     * @return The buttons to insert or {@code null} to insert no buttons. This
     *         implementation returns the buttons OK and Cancel.
     */
    @Override
    protected AbstractButton[] createWindowButtons() {
        okButton = new JButton(getMainButtonText());
        okButton.setEnabled(false);
        okButton.addActionListener(e -> closeDialog(false));
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton(SwingDialogsMessages.get("commons.buttons.cancel"));
        cancelButton.addActionListener(e -> closeDialog(true));

        getRootPane().setDefaultButton(okButton);

        return new AbstractButton[] {okButton, cancelButton};
    }

    /**
     * Called to get the text of the dialogs main button.
     * <p>
     * This method can be overridden if no additional buttons should be shown
     * but the text "Save" is not applicable.
     *
     * @return The text to use for the main button. This implementation does
     *         always return "Save".
     */
    @Override
    protected String getMainButtonText() {
        return SwingDialogsMessages.get("commons.buttons.save");
    }

    /**
     * Called to validate the user's input.
     * <p>
     * This implementation tests whether the set center panel implements
     * {@link ModifiableDataPanel<Data>}. If so,
     * {@link ModifiableDataPanel<Data>#validateInput()} is invoked.
     *
     * @return {@code true} if the input is valid; otherwise, {@code false}.
     */
    @SuppressWarnings("unchecked")
    protected boolean validateModifiedData() {
        if (getCenterPanel() instanceof ModifiableDataPanel)
            return ((ModifiableDataPanel<Data>) getCenterPanel()).validateInput();

        return true;
    }

    /**
     * Called to update the data that was given at construction with the users
     * input.
     * <p>
     * This implementation tests whether the set center panel implements
     * {@link ModifiableDataPanel<Data>}. If so,
     * {@link ModifiableDataPanel<Data>#updateData} is invoked.
     *
     * @param data The data to update. This is identical to {@link #getData()}.
     */
    @SuppressWarnings("unchecked")
    protected void updateData(Data data) {
        if (getCenterPanel() instanceof ModifiableDataPanel)
            ((ModifiableDataPanel<Data>) getCenterPanel()).updateData(data);
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
     *     <li>{@code null}: The event is consumed and the window is not closed.
     *          </li>
     *     <li>{@code null}: The window is closed without any further action.
     *         </li>
     * </ul>
     * If the dialog is not closed {@link #resetAccepted()} is invoked.
     *
     * @param e The window event.
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (isModal()) {
                if (isAccepted() && isDataModified() && !applyChangedData()) {
                    resetAccepted();
                    return;
                }
            } else if (isDataModified()) {
                var confirmed = queryUserToSave();

                if (confirmed == null) {
                    resetAccepted();
                    return;
                }

                if (confirmed) {
                    if (!applyChangedData()) {
                        resetAccepted();
                        return;
                    }
                }
            }
        }

        super.processWindowEvent(e);
    }

    /**
     * Called within {@link #processWindowEvent(WindowEvent)} when the window
     * is closed but has modified data to query the user what to do.
     * <p>
     * This implementation uses {@link NavigationService#getConfirmation} if
     * a {@code NavigationService} is available; otherwise {@link JOptionPane}
     * is used to show a question whether the data shall be saved and Yes, No
     * and Cancel buttons.
     *
     * @return The result of the user's choice. {@code true} to accept,
     *         {@code false} to deny or {@code null} to cancel.
     */
    protected Boolean queryUserToSave() {
        if (ServiceProvider.has(NavigationService.class)) {
            NavigationService service = ServiceProvider.get(NavigationService.class);
            return service.getConfirmation(this, SwingDialogsMessages.get("data.query.save"), null);
        }

        var result = JOptionPane.showConfirmDialog(this,
                                                   SwingDialogsMessages.get("data.query.save"),
                                                   null,
                                                   JOptionPane.YES_NO_CANCEL_OPTION);

        return switch (result) {
            case JOptionPane.YES_OPTION -> true;
            case JOptionPane.NO_OPTION -> false;
            default -> null;
        };
    }
}
