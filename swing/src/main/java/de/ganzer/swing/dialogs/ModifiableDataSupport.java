package de.ganzer.swing.dialogs;

import java.util.function.Consumer;

/**
 * An interface that should be implemented by dialogs or frames that supports
 * the modification of data by the user.
 * <p>
 * The following example shows how the interface may be implemented:
 * <p>
 * <pre>{@code
 * public class LoginDialog extends EscapableDialog implements ModifiableDataSupport<LoginDialog.Data> {
 *     public static class Data {
 *         public String name;
 *         public String password;
 *     }
 *
 *     private class MyDocumentListener implements DocumentListener {
 *         @Override
 *         public void insertUpdate(DocumentEvent e) {
 *             setDataModified(true);
 *         }
 *
 *         @Override
 *         public void removeUpdate(DocumentEvent e) {
 *             setDataModified(true);
 *         }
 *
 *         @Override
 *         public void changedUpdate(DocumentEvent e) {
 *             setDataModified(true);
 *         }
 *     }
 *
 *     private Data data;
 *     private Consumer<Data> dataConsumer;
 *     private boolean dataModified = false;
 *
 *     private final JTextField nameField = new JTextField(20);
 *     private final JTextField passwordField = new JPasswordField(20);
 *
 *     public LoginDialog(Window owner, Data data) {
 *         super(owner, DEFAULT_MODALITY_TYPE);
 *
 *         Objects.requireNonNull(data, "data must not be null.");
 *         this.data = data;
 *
 *         init();
 *         pack();
 *     }
 *
 *     @Override
 *     public Data getData() {
 *         return data;
 *     }
 *
 *     @Override
 *     public void setDataConsumer(Consumer<Data> dataConsumer) {
 *         this.dataConsumer = dataConsumer;
 *     }
 *
 *     @Override
 *     public boolean isDataModified() {
 *         return dataModified;
 *     }
 *
 *     @Override
 *     public void setDataModified(boolean modified) {
 *         this.dataModified = modified;
 *     }
 *
 *     @Override
 *     public boolean applyChangedData() {
 *         if (!isDataModified())
 *             return true;
 *
 *         if (!isDataValid())
 *             return false;
 *
 *         updateData();
 *
 *         if (dataConsumer != null)
 *             dataConsumer.accept(data);
 *
 *         setDataModified(false);
 *
 *         return true;
 *     }
 *
 *     private void init() {
 *         setTitle("Login");
 *         setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 *         setLocationRelativeTo(getParent());
 *
 *         nameField.setText(data.name);
 *         passwordField.setText(data.password);
 *
 *         nameField.getDocument().addDocumentListener(new MyDocumentListener());
 *         passwordField.getDocument().addDocumentListener(new MyDocumentListener());
 *
 *         initLayout();
 *     }
 *
 *     private void initLayout() {
 *         var nameLabel = new JLabel("Name:");
 *         var passwordLabel = new JLabel("Password:");
 *         var buttonPanel = initButtons();
 *         var layout = new GroupLayout(getContentPane());
 *
 *         layout.setAutoCreateGaps(true);
 *         layout.setAutoCreateContainerGaps(true);
 *
 *         layout.setHorizontalGroup(layout.createSequentialGroup()
 *                                           .addGroup(layout.createParallelGroup()
 *                                                             .addComponent(nameLabel)
 *                                                             .addComponent(passwordLabel)
 *                                           )
 *                                           .addGroup(layout.createParallelGroup()
 *                                                             .addComponent(nameField)
 *                                                             .addComponent(passwordField)
 *                                                             .addComponent(buttonPanel)
 *                                           )
 *         );
 *         layout.setVerticalGroup(layout.createSequentialGroup()
 *                                         .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
 *                                                           .addComponent(nameLabel)
 *                                                           .addComponent(nameField)
 *                                         )
 *                                         .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
 *                                                           .addComponent(passwordLabel)
 *                                                           .addComponent(passwordField)
 *                                         )
 *                                         .addComponent(buttonPanel)
 *         );
 *
 *         getContentPane().setLayout(layout);
 *     }
 *
 *     private JPanel initButtons() {
 *         var okButton = new JButton("OK");
 *         okButton.addActionListener(e -> updateDataAndClose());
 *         getRootPane().setDefaultButton(okButton);
 *
 *         var cancelButton = new JButton("Cancel");
 *         cancelButton.addActionListener(e -> closeWindow(true));
 *
 *         var panel = new JPanel();
 *         var layout = new GroupLayout(panel);
 *
 *         layout.setAutoCreateGaps(true);
 *
 *         layout.setHorizontalGroup(layout.createSequentialGroup()
 *                                           .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
 *                                           .addComponent(okButton)
 *                                           .addComponent(cancelButton)
 *         );
 *         layout.setVerticalGroup(layout.createSequentialGroup()
 *                                         .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
 *                                         .addGroup(layout.createParallelGroup()
 *                                                           .addComponent(okButton)
 *                                                           .addComponent(cancelButton))
 *         );
 *         layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
 *
 *         panel.setLayout(layout);
 *
 *         return panel;
 *     }
 *
 *     private void updateDataAndClose() {
 *         applyChangedData();
 *         closeWindow(false);
 *     }
 *
 *     private boolean isDataValid() {
 *         // Validate input here.
 *         return performLogin();
 *     }
 *
 *     private void updateData() {
 *         data.name = nameField.getText();
 *         data.password = passwordField.getText();
 *     }
 *
 *     private boolean performLogin() {
 *         // Perform login here.
 *         return true;
 *     }
 * }
 * }</pre>
 * <p>
 * This dialog may then be shown in this way:
 * <p>
 * <pre>{@code
 * public class MyAppFrame extends JFrame {
 *     // ...
 *     private boolean login() {
 *         var data = new LoginDialog.Data();
 *         // Initialize data here if wanted.
 *
 *         var dialog = new LoginDialog(this);
 *         dialog.initControls(data);
 *         dialog.setDataConsumer(d -> System.out.printf("User logged in: %s\n", d.name));
 *         dialog.setVisible(true);
 *
 *         if (dialog.isEscaped())
 *             exit(1);
 *     }
 *     // ...
 * }
 * }</pre>
 *
 * @param <Data> The type of the supported modifiable data.
 *
 * @see EscapableDialog
 */
public interface ModifiableDataSupport<Data> extends DataSupport<Data> {
    /**
     * Invokes the set data consumer if the data is valid.
     *
     * @return {@code true} if the data is valid and the consumer is invoked;
     *         otherwise, {@code false}.
     */
    boolean applyChangedData();

    /**
     * Sets the consumer to be invoked when the dialog is closed by the OK
     * button or whenever an Apply button is clicked within the dialog to
     * notify the client about changed data.
     *
     * @param dataConsumer The consumer to set.
     */
    @SuppressWarnings("unused")
    void setDataConsumer(Consumer<Data> dataConsumer);

    /**
     * Gets a value that indicates whether the data is modified by the user.
     *
     * @return {@code true} if the data is modified; otherwise, {@code false}.
     */
    boolean isDataModified();

    /**
     * Sets the modification flag of the data.
     *
     * @param modified {@code true} to mark the data as modified.
     */
    void setDataModified(boolean modified);
}
