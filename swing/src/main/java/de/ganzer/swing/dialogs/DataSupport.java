package de.ganzer.swing.dialogs;

import java.util.function.Consumer;

/**
 * An interface that should be implemented by dialogs or frames that supports
 * the modification of data by the user.
 * <p>
 * The following example shows how the interface may be implemented:
 * <p>
 * {@code
public class LoginDialog extends EscapableDialog implements DataSupport<LoginDialog.Data> {
    public static class Data {
        public String name;
        public String password;
    }

    private Data data;
    private Consumer<Data> dataConsumer;

    private final JTextField nameField = new JTextField(20);
    private final JTextField passwordField = new JPasswordField(20);

    public LoginDialog(Window owner) {
        super(owner, DEFAULT_MODALITY_TYPE);

        init();
        pack();
    }

    @Override
    public void initControls(Data data) {
        Objects.requireNonNull(data, "data must not be null.");

        this.data = data;

        nameField.setText(data.name);
        passwordField.setText(data.password);
    }

    @Override
    public void setDataConsumer(Consumer<Data> dataConsumer) {
        this.dataConsumer = dataConsumer;
    }

    private void init() {
        setTitle("Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());

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
        okButton.addActionListener(e -> updateDataAndClose());
        getRootPane().setDefaultButton(okButton);

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> closeDialog());

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

    private void updateDataAndClose() {
        if (!dataValid())
            return;

        updateData();

        if (dataConsumer != null)
            dataConsumer.accept(data);

        closeDialog();
    }

    private boolean dataValid() {
        // Validate input here.
        return performLogin();
    }

    private boolean dataValid() {
        // Login here.
        return true;
    }

    private void updateData() {
        data.name = nameField.getText();
        data.password = passwordField.getText();
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
 *
 * @param <Data> The type of the supported modifiable data.
 *
 * @see EscapableDialog
 */
public interface DataSupport<Data> {
    /**
     * Called to apply the initial data to the controls.
     * <p>
     * Implementors should store this data in a local variable to modify it when
     * the dialog or frame is closed.
     *
     * @param data The data to apply.
     */
    void initControls(Data data);

    /**
     * Sets the consumer that is invoked when the dialog is closed by the OK
     * button or whenever an Apply button is clicked within the dialog to
     * notify the client about changed data.
     *
     * @param dataConsumer The consumer to set.
     */
    @SuppressWarnings("unused")
    void setDataConsumer(Consumer<Data> dataConsumer);
}
