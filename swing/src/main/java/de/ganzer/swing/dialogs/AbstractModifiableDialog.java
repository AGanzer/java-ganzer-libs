package de.ganzer.swing.dialogs;

import de.ganzer.swing.internals.SwingMessages;

import javax.swing.JOptionPane;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * An abstract dialog that implements the {@link ModifiableDataSupport}
 * interface to make implementation of dialogs that supports modifiable
 * data easier.
 * <p>
 * The following example shows how a derived dialog may be implemented:
 * <p>
 * {@code
public class LoginDialog extends AbstractModifiableDialog<LoginDialog.Data> {
    public static class Data {
        public String name;
        public String password;
    }

    private final JTextField nameField = new JTextField(20);
    private final JTextField passwordField = new JPasswordField(20);

    public LoginDialog(Window owner, ModalityType modalityType, Data data) {
        super(owner, modalityType, data);

        init();
        pack();
    }

    @Override
    protected boolean validateModifiedData() {
        // Validate input here.
        return performLogin();
    }

    @Override
    protected void updateData(Data data) {
        data.name = nameField.getText();
        data.password = passwordField.getText();
    }

    private void init() {
        setTitle("Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());

        class TextFieldListener implements DocumentListener {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setDataModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setDataModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setDataModified(true);
            }
        }

        nameField.setText(getData().name);
        passwordField.setText(getData().password);

        nameField.getDocument().addDocumentListener(new TextFieldListener());
        passwordField.getDocument().addDocumentListener(new TextFieldListener());

        initLayout();
    }

    private void initLayout() {
        var nameLabel = new JLabel("Name:");
        var passwordLabel = new JLabel("Password:");
        var buttonPanel = initButtons();
        var layout = new GroupLayout(getContentPane());

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                                          .addGroup(layout.createParallelGroup()
                                                            .addComponent(nameLabel)
                                                            .addComponent(passwordLabel)
                                          )
                                          .addGroup(layout.createParallelGroup()
                                                            .addComponent(nameField)
                                                            .addComponent(passwordField)
                                                            .addComponent(buttonPanel)
                                          )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                          .addComponent(nameLabel)
                                                          .addComponent(nameField)
                                        )
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                          .addComponent(passwordLabel)
                                                          .addComponent(passwordField)
                                        )
                                        .addComponent(buttonPanel)
        );

        getContentPane().setLayout(layout);
    }

    private JPanel initButtons() {
        var okButton = new JButton("OK");
        okButton.addActionListener(e -> closeWindow(false));
        getRootPane().setDefaultButton(okButton);

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> closeWindow(true));

        var panel = new JPanel();
        var layout = new GroupLayout(panel);

        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                                          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                          .addComponent(okButton)
                                          .addComponent(cancelButton)
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup()
                                                          .addComponent(okButton)
                                                          .addComponent(cancelButton))
        );
        layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);

        panel.setLayout(layout);

        return panel;
    }

    private boolean performLogin() {
        // Perform login here.
        return true;
    }
}
 * }
 * <p>
 * This dialog may then be shown in this way:
 * <p>
 * {@code
public class MyAppFrame extends JFrame {
    // ...
    private boolean login() {
        var data = new LoginDialog.Data();
        // Initialize data here if wanted.

        var dialog = new LoginDialog(this);
        dialog.initControls(data);
        dialog.setDataConsumer(d -> System.out.printf("User logged in: %s\n", d.name));
        dialog.setVisible(true);

        if (dialog.isEscaped())
            exit(1);
    }
    // ...
}
 * }
 * <p>
 * Or, because there is no Apply button in the dialog but just OK and Cancel:
 * <p>
 * {@code
public class MyAppFrame extends JFrame {
    // ...
    private boolean login() {
        var data = new LoginDialog.Data();
        // Initialize data here if wanted.

        var dialog = new LoginDialog(this);
        dialog.initControls(data);
        dialog.setVisible(true);

        if(dialog.isEscaped())
            exit(1);

        System.out.printf("User logged in: %s\n", data.name);
    }
    // ...
}
 * }
 * <p>
 * @param <Data> The type of the supported modifiable data.
 * <p>
 * @see EscapableDialog
 */
@SuppressWarnings("unused")
public abstract class AbstractModifiableDialog<Data> extends EscapableDialog implements ModifiableDataSupport<Data> {
    private final Data data;
    private Consumer<Data> dataConsumer;
    private boolean dataModified = false;

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Data data) {
        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Frame owner, Data data) {
        super(owner);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Frame owner, String title, Data data) {
        super(owner, title);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Frame owner, String title, GraphicsConfiguration gc, Data data) {
        super(owner, title, gc);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Dialog owner, Data data) {
        super(owner);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Dialog owner, String title, Data data) {
        super(owner, title);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Dialog owner, String title, GraphicsConfiguration gc, Data data) {
        super(owner, title, gc);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Window owner, Data data) {
        super(owner);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Window owner, ModalityType modalityType, Data data) {
        super(owner, modalityType);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Window owner, String title, Data data) {
        super(owner, title);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Window owner, String title, ModalityType modalityType, Data data) {
        super(owner, title, modalityType);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractModifiableDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc, Data data) {
        super(owner, title, modalityType, gc);

        Objects.requireNonNull(data, "data cannot be null.");
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Data getData() {
        return data;
    }

    /**
     * Invokes the set data consumer.
     * <p>
     * This implementation firstly calls {@link #validateModifiedData()}. If the
     * data is not valid, the method returns {@code false}; otherwise,
     * {@link #updateData} is called and the set data consumer is invoked and the
     * modification flag is set to {@code false}.
     *
     * @return {@code true} if the data is valid and the consumer is invoked;
     *         otherwise, {@code false}.
     *
     * @see #isDataModified()
     * @see #setDataModified(boolean)
     */
    @Override
    public boolean applyChangedData() {
        if (!validateModifiedData())
            return false;

        updateData(getData());

        if (dataConsumer != null)
            dataConsumer.accept(getData());

        setDataModified(false);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDataConsumer(Consumer<Data> dataConsumer) {
        this.dataConsumer = dataConsumer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDataModified() {
        return dataModified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDataModified(boolean modified) {
        this.dataModified = modified;
    }

    /**
     * Handles the {@link WindowEvent#WINDOW_CLOSING} event.
     * <p>
     * If the dialog is modal, {@link #applyChangedData()} is invoked if
     * {@link #isEscaped()} is {@code false}.
     * <p>
     * If the dialog is not modal, the {@link #queryUserToSave()} is invoked
     * if {@link #isDataModified()} is {@code true}.
     * <p>
     * This implementation calls {@link #queryUserToSave()}. The only recognized
     * answers are:
     * <ul>
     *     <li>{@link JOptionPane#YES_OPTION}: {@link #applyChangedData()} is
     *          called. On success the window is closed; otherwise, the event
     *          is consumed and the window is not closed.</li>
     *     <li>{@link JOptionPane#CANCEL_OPTION}: The event is consumed and the
     *          window is not closed.</li>
     *     <li>All others: The window is closed without any further action.</li>
     * </ul>
     *
     * @param e The window event.
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (isModal()) {
                if (!isEscaped() && !applyChangedData())
                    return;
            } else if (isDataModified()) {
                switch (queryUserToSave()) {
                    case JOptionPane.YES_OPTION:
                        if (!applyChangedData())
                            return;

                        break;

                    case JOptionPane.CANCEL_OPTION:
                        return;
                }
            }
        }

        super.processWindowEvent(e);
    }

    /**
     * Called within {@link #processWindowEvent(WindowEvent)} when the window
     * is closed but has modified data to query the user what to do.
     * <p>
     * This implementation does simply show a {@link JOptionPane} with a
     * question whether the data shall be saved and Yes, No and Cancel buttons.
     *
     * @return The result of the user's choose.
     */
    protected int queryUserToSave() {
        return JOptionPane.showConfirmDialog(
                this,
                SwingMessages.get("modifiableDialog.dataHasChanged"),
                getTitle(),
                JOptionPane.YES_NO_CANCEL_OPTION);
    }

    /**
     * Called to validate the user's input.
     *
     * @return {@code true} if the input is valid; otherwise, {@code false}.
     */
    protected abstract boolean validateModifiedData();

    /**
     * Called to update the data with the users input.
     *
     * @param data The data to update. This is identical to {@link #getData()}.
     */
    protected abstract void updateData(Data data);
}
