package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.dlgfw.AbstractModifiableDataDialog;

import javax.swing.AbstractButton;
import javax.swing.JButton;
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

    protected GAction getNextAction() {
        return ((WizardPanel<?>) getCenterPanel()).getNextAction();
    }

    protected GAction getPrevAction() {
        return ((WizardPanel<?>) getCenterPanel()).getPrevAction();
    }

    protected GAction getCancelAction() {
        return ((WizardPanel<?>) getCenterPanel()).getCancelAction();
    }

    protected GAction getHelpAction() {
        return ((WizardPanel<?>) getCenterPanel()).getHelpAction();
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

    /**
     * Called to create the buttons that shall be inserted as the window's main
     * buttons.
     *
     * @return The buttons to insert or {@code null} to insert no buttons. This
     *         implementation returns the buttons OK and Cancel.
     */
    @Override
    protected AbstractButton[] createWindowButtons() {
        var buttons = new AbstractButton[] {
                getPrevAction().createButton(),
                getNextAction().createButton(),
                getCancelAction().createButton()
        };

        getCancelAction().addActionListener(e -> closeDialog(false));
        getRootPane().setDefaultButton((JButton) buttons[1]);

        return super.createWindowButtons();
    }

    /**
     * Called to get the justifications of the buttons within the window.
     * <p>
     * This is not called when {@link #getButtonPanelLocation()} returns
     * {@link BorderLayout#EAST} or {@link BorderLayout#WEST}. In these cases,
     * the buttons are always justified at the top edge of the window.
     *
     * @return {@code true} to center the buttons, {@code false} to justify it
     *         at the right edge of the window. This implementation does
     *         always return {@code false}.
     */
    @Override
    protected boolean shouldCenterButtons() {
        return false;
    }
}
