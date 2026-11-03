package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.dialogs.ModifiableDataSupport;
import de.ganzer.swing.dlgfw.AbstractModifiableDataPanel;

import javax.swing.JComponent;

public class WizardPanel<Data> extends AbstractModifiableDataPanel<Data> {
    /**
     * Creates a new <code>JPanel</code> with <code>FlowLayout</code>
     * and the specified buffering strategy.
     * If <code>isDoubleBuffered</code> is true, the <code>JPanel</code>
     * will use a double buffer.
     *
     * @param isDoubleBuffered a boolean, true for double-buffering, which
     *         uses additional memory space to achieve fast, flicker-free
     *         updates
     * @param suppressSetup If {@code true}, the dialog's controls will not be
     *         created at construction but later if it is shown.
     * @param owner The owner window that supports data.
     *
     * @throws NullPointerException {@code owner} is {@code null}.
     * @since 6.3.0
     */
    public WizardPanel(boolean isDoubleBuffered, boolean suppressSetup, ModifiableDataSupport<Data> owner) {
        super(isDoubleBuffered, suppressSetup, owner);
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
        return null;
    }

    /**
     * Called to validate the user's input.
     *
     * @return {@code true} if the input is valid; otherwise, {@code false}.
     */
    @Override
    public boolean validateInput() {
        return false;
    }

    /**
     * Called to update the data with the user's input.
     *
     * @param data The data to update.
     */
    @Override
    public void updateData(Data data) {

    }

    /**
     * Called to initialize the controls from the specified data.
     *
     * @param data The data where to initialize the controls with. This may be
     *         {@code null} if the controls shall be reset.
     */
    @Override
    public void initControls(Data data) {

    }
}
