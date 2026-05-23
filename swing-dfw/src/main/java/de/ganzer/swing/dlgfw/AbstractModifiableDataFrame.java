package de.ganzer.swing.dlgfw;

import de.ganzer.swing.dlgfw.services.ServiceProvider;
import de.ganzer.swing.dialogs.ModifiableDataSupport;
import de.ganzer.swing.dlgfw.internals.SwingDialogsMessages;
import de.ganzer.swing.dlgfw.services.ApplicationService;
import de.ganzer.swing.dlgfw.services.NavigationService;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

/**
 * The basic frame for all non-modal dialogs that modifies external data.
 *
 * @param <Data> The type of the accepted data.
 */
@SuppressWarnings("unused")
public abstract class AbstractModifiableDataFrame<Data> extends AbstractDataFrame<Data> implements ModifiableDataSupport<Data> {
    private Consumer<Data> dataConsumer;
    private boolean dataModified = false;
    private JButton applyButton;

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
    public AbstractModifiableDataFrame() throws HeadlessException {
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
    public AbstractModifiableDataFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    /**
     * Creates a new, initially invisible <code>Frame</code> with the
     * specified title.
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
    public AbstractModifiableDataFrame(String title) throws HeadlessException {
        super(title);
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
    public AbstractModifiableDataFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }

    /**
     * Invokes the set data consumer if the data is valid and modified.
     * <p>
     * This implementation firstly calls {@link #validateModifiedData()}. If the
     * data is not valid, the method returns {@code false}; otherwise,
     * {@link #} is called and the set data consumer is invoked and the
     * modification flag is set to {@code false}.
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
     * Sets the consumer that is invoked when the frame is closed by the Save
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
     * Gets a value that indicates whether the data is modified by the user.
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

        if (applyButton != null)
            applyButton.setEnabled(modified);
    }

    /**
     * Called to create the buttons that shall be inserted as the window's main
     * buttons.
     *
     * @return The buttons to insert or {@code null} to insert no buttons. This
     *         implementation returns the buttons Apply and Close.
     */
    @Override
    protected AbstractButton[] createWindowButtons() {
        applyButton = new JButton(getMainButtonText());
        applyButton.setEnabled(false);
        applyButton.addActionListener(e -> applyChangedData());
        getRootPane().setDefaultButton(applyButton);

        JButton closeButton = new JButton(SwingDialogsMessages.get("commons.buttons.close"));
        closeButton.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        return new  AbstractButton[] {applyButton, closeButton};
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
     * Queries the user what to do with modified data if {@link #isDataModified()}
     * is {@code true} and the event's ID is {@link WindowEvent#WINDOW_CLOSING}.
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
     *
     * @param e The window event.
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING && isDataModified()) {
            var confirmed = queryUserToSave();

            if (confirmed == null)
                return;

            if (confirmed && !applyChangedData())
                return;
        }

        super.processWindowEvent(e);
    }

    /**
     * Called within {@link #processWindowEvent(WindowEvent)} when the window
     * is closed but has modified data to query the user what to do.
     * <p>
     * This implementation uses {@link NavigationService#getConfirmation} if
     * a {@code NavigationService} is available; otherwise, {@link JOptionPane}
     * is used to show a question whether the data shall be saved and Yes, No
     * and Cancel buttons.
     * <p>
     * The title to show in the confirmation dialog is gotten from
     * {@link ApplicationService#getAppDisplayName()} if available; otherwise,
     * {@code null} is used as title.
     *
     * @return The result of the user's choice. {@code true} to accept,
     *         {@code false} to deny or {@code null} to cancel.
     */
    @SuppressWarnings("DuplicatedCode")
    protected Boolean queryUserToSave() {
        String appName = null;

        if (ServiceProvider.has(ApplicationService.class)) {
            ApplicationService appService = ServiceProvider.get(ApplicationService.class);
            appName = appService.getAppDisplayName();
        }

        if (ServiceProvider.has(NavigationService.class)) {
            NavigationService service = ServiceProvider.get(NavigationService.class);
            return service.getConfirmation(this, SwingDialogsMessages.get("data.query.save"), appName);
        }

        var result = JOptionPane.showConfirmDialog(this,
                                                   SwingDialogsMessages.get("data.query.save"),
                                                   appName,
                                                   JOptionPane.YES_NO_CANCEL_OPTION);

        return switch (result) {
            case JOptionPane.YES_OPTION -> true;
            case JOptionPane.NO_OPTION -> false;
            default -> null;
        };
    }
}
