package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.dlgfw.AbstractModifiableDataDialog;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.*;

public class WizardDialog<Data> extends AbstractModifiableDataDialog<Data> {
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
     *         of {@link Dialog} or {@link Frame}
     * @throws IllegalArgumentException if the {@code owner}'s
     *         {@code GraphicsConfiguration} is not from a screen device
     * @throws HeadlessException when {@code GraphicsEnvironment.isHeadless()}
     *         returns {@code true}
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public WizardDialog(Window owner) {
        super(owner);
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
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public WizardDialog(Window owner, Data data) {
        super(owner, data);
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
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     * @since 6.3.0
     */
    public WizardDialog(Window owner, boolean suppressSetup, Data data) {
        super(owner, suppressSetup, data);
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
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     * @since 6.3.0
     */
    public WizardDialog(Window owner, String title, boolean suppressSetup, Data data) {
        super(owner, title, suppressSetup, data);
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
        return new WizardPanel<>(false, isLazySetup(), this);
    }
}
