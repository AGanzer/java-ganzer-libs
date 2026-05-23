package de.ganzer.swing.dlgfw;

import de.ganzer.swing.dialogs.DataSupport;

import javax.swing.JComponent;
import java.awt.*;

/**
 * The basic dialog for all modal dialogs that accept external data.
 * <p>
 * This dialog invokes {@link Initializer<Data>#initControls} on the center
 * panel (as well on the button panel if any is set) if the panel implements
 * {@link Initializer<Data>}.
 *
 * @param <Data> The type of the accepted data.
 */
public abstract class AbstractDataDialog<Data> extends AbstractDialog implements DataSupport<Data>, Initializer<Data> {
    private Data data;

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
    public AbstractDataDialog(Frame owner, Data data) {
        super(owner);

        if (data != null)
            initControls(data);
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
     * @param data The data to set.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataDialog(Frame owner, String title, Data data) {
        super(owner, title);

        if (data != null)
            initControls(data);
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
     * @param data The data to set.
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
    public AbstractDataDialog(Frame owner, String title, GraphicsConfiguration gc, Data data) {
        super(owner, title, gc);

        if (data != null)
            initControls(data);
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
     * @param data The data to set.
     *
     * @throws HeadlessException {@code if GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataDialog(Dialog owner, Data data) {
        super(owner);

        if (data != null)
            initControls(data);
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
     * @param data The data to set.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataDialog(Dialog owner, String title, Data data) {
        super(owner, title);

        if (data != null)
            initControls(data);
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
     * @param data The data to set.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataDialog(Dialog owner, String title, GraphicsConfiguration gc, Data data) {
        super(owner, title, gc);

        if (data != null)
            initControls(data);
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
     * @param data The data to set.
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
    public AbstractDataDialog(Window owner, Data data) {
        super(owner);

        if (data != null)
            initControls(data);
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
     * @param data The data to set.
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
    public AbstractDataDialog(Window owner, String title, Data data) {
        super(owner, title);

        if (data != null)
            initControls(data);
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
     * @param data The data to set.
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
    public AbstractDataDialog(Window owner, String title, GraphicsConfiguration gc, Data data) {
        super(owner, title, gc);

        if (data != null)
            initControls(data);
    }

    /**
     * Gets the data the frame was initialized with.
     *
     * @return The data or {@code null} if no data is set.
     *
     * @see #initControls
     */
    @Override
    public Data getData() {
        return data;
    }

    /**
     * Called at construction after the controls are created t initialize the
     * controls with the data given at construction.
     * <p>
     * This implementation invokes {@link Initializer<Data>#initControls} on
     * the center panel (as well on the button panel if any is set) if the
     * panel implements {@link Initializer<Data>}.
     *
     * @param data The data where to initialize the controls with. This may be
     *         {@code null} if the controls shall be reset.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initControls(Data data) {
        this.data = data;

        if (getCenterPanel() instanceof Initializer)
            ((Initializer<Data>) getCenterPanel()).initControls(data);

        if (getButtonPanel() instanceof Initializer)
            ((Initializer<Data>) getButtonPanel()).initControls(data);
    }
}
