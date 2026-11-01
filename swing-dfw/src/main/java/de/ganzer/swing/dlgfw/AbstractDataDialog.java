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
 *
 * @since 6.0.0
 */
public abstract class AbstractDataDialog<Data> extends AbstractDialog implements DataSupport<Data>, Initializer<Data> {
    private Data data;

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
    public AbstractDataDialog(Window owner) {
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
    public AbstractDataDialog(Window owner, String title) {
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
    public AbstractDataDialog(Window owner, String title, GraphicsConfiguration gc) {
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
    public AbstractDataDialog(Window owner, String title, GraphicsConfiguration gc, Data data) {
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
     *         {@code null} if this dialog has no owner.
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
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
    public AbstractDataDialog(Window owner, boolean suppressSetup) {
        super(owner, suppressSetup);
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
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
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
    public AbstractDataDialog(Window owner, String title, boolean suppressSetup) {
        super(owner, title, suppressSetup);
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
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
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
    public AbstractDataDialog(Window owner, String title, GraphicsConfiguration gc, boolean suppressSetup) {
        super(owner, title, gc, suppressSetup);
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
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
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
    public AbstractDataDialog(Window owner, boolean suppressSetup, Data data) {
        super(owner, suppressSetup);

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
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
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
    public AbstractDataDialog(Window owner, String title, boolean suppressSetup, Data data) {
        super(owner, title, suppressSetup);

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
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
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
    public AbstractDataDialog(Window owner, String title, GraphicsConfiguration gc, boolean suppressSetup, Data data) {
        super(owner, title, gc, suppressSetup);

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
     * Called at construction if {@code data} is not {@code null} after the
     * controls are created to initialize the controls with the data given at
     * construction.
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

        if (getCenterPanel() == null)
            return;

        if (getCenterPanel() instanceof Initializer)
            ((Initializer<Data>) getCenterPanel()).initControls(data);

        if (getButtonPanel() instanceof Initializer)
            ((Initializer<Data>) getButtonPanel()).initControls(data);
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

        if (data != null)
            initControls(data);
    }
}
