package com.example.uitests.swing.tests;

import de.ganzer.swing.dialogs.AbstractModifiableDialog;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Dialog;
import java.awt.Window;
import java.util.Objects;

public class GroupLayoutLoginDialog extends AbstractModifiableDialog<GroupLayoutLoginDialog.Data> {
    public static class Data {
        public String name;
        public String password;
    }

    private final JTextField nameField = new JTextField(20);
    private final JTextField passwordField = new JPasswordField(20);

    private GroupLayoutLoginDialog(Window owner, Dialog.ModalityType modalityType) {
        super(owner, modalityType);

        init();
        pack();
    }

    public static boolean showModal(Window owner, Data data) {
        var dialog = new GroupLayoutLoginDialog(owner, DEFAULT_MODALITY_TYPE);
        dialog.initControls(data);
        dialog.setVisible(true);

        return !dialog.isEscaped();
    }

    @Override
    public void initControls(Data data) {
        Objects.requireNonNull(data, "data must not be null.");

        super.initControls(data);

        nameField.setText(data.name);
        passwordField.setText(data.password);
    }

    @Override
    protected boolean validateModifiedData() {
        // Validate input here.
        return true;
    }

    @Override
    protected void updateData(Data data) {
        data.name = nameField.getText();
        data.password = passwordField.getText();
    }

    private void init() {
        setTitle("Test Dialog");
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
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameField)
                )
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
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
        cancelButton.addActionListener(e -> closeDialog(true));

        var panel = new JPanel();
        var layout = new GroupLayout(panel);

        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addComponent(cancelButton)
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup()
                        .addComponent(okButton)
                        .addComponent(cancelButton))
        );
        layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);

        panel.setLayout(layout);

        return panel;
    }

    private void updateDataAndClose() {
        if (applyChangedData())
            closeDialog(false);
    }
}
