package com.example.uitests.swing.tests;

import com.example.uitests.swing.SVGProvider;
import de.ganzer.core.validation.CharCountValidator;
import de.ganzer.swing.controls.GIconPasswordField;
import de.ganzer.swing.dialogs.AbstractModifiableDialog;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;
import de.ganzer.swing.validaton.ValidationFilterList;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dialog;
import java.awt.Window;

public class LoginDialog extends AbstractModifiableDialog<LoginDialog.Data> {
    private final ValidationFilterList validationFilterList = new ValidationFilterList();

    private char echoChar;

    public static class Data {
        public String name;
        public char[] password;
    }

    private final JTextField nameField = new JTextField(20);
    private final GIconPasswordField passwordField = new GIconPasswordField(20);

    private LoginDialog(Window owner, Dialog.ModalityType modalityType, Data data) {
        super(owner, modalityType, data);

        init();
        pack();

        validationFilterList.validate(ValidationBehavior.SET_VISUAL_HINTS);
    }

    public static boolean showModal(Window owner, Data data) {
        var dialog = new LoginDialog(owner, DEFAULT_MODALITY_TYPE, data);
        dialog.setVisible(true);

        return !dialog.isEscaped();
    }

    @Override
    protected boolean validateModifiedData() {
        return validationFilterList.validate(ValidationBehavior.SHOW_MESSAGE_BOX);
    }

    @Override
    protected void updateData(Data data) {
        data.name = nameField.getText();
        data.password = passwordField.getPassword();
    }

    private void init() {
        setTitle("Test Dialog");
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
        passwordField.setText(getData().password != null ? new String(getData().password) : null);
        passwordField.setIcon(SVGProvider.get("close", 16));
        passwordField.getIconClickedAction().addActionListener(e -> {
            passwordField.setEchoChar(passwordField.echoCharIsSet() ? '\0' : echoChar);
            passwordField.setIcon(passwordField.echoCharIsSet()
                                          ? SVGProvider.get("close", 16)
                                          : SVGProvider.get("checks", 16));
        });
        echoChar = passwordField.getEchoChar();

        nameField.getDocument().addDocumentListener(new TextFieldListener());
        passwordField.getDocument().addDocumentListener(new TextFieldListener());

        var nameValidator = new CharCountValidator();
        nameValidator.setMinLength(5);
        nameValidator.setMaxLength(12);
        validationFilterList.addFilter(new ValidationFilter(nameValidator, nameField, false));

        var pwValidator = new CharCountValidator();
        pwValidator.setMinLength(8);
        pwValidator.setMaxLength(12);
        validationFilterList.addFilter(new ValidationFilter(pwValidator, passwordField, false));

        initLayout();
    }

    private void initLayout() {
        var nameLabel = new JLabel("Name:");
        var passwordLabel = new JLabel("Password:");
        var buttonPanel = initButtons();
        var layout = new GroupLayout(getContentPane());

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
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
        layout.setVerticalGroup(
                layout.createSequentialGroup()
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
        var panel = new JPanel(null);

        var okButton = new JButton("OK");
        okButton.addActionListener(e -> closeWindow(false));
        getRootPane().setDefaultButton(okButton);

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> closeWindow(true));

        var toggleLiveButton = new JButton(SVGProvider.get("checks", 16));
        toggleLiveButton.setToolTipText("Toggles live validation feature.");
        toggleLiveButton.setFocusable(false);
        toggleLiveButton.setBorderPainted(false);
        toggleLiveButton.setBackground(panel.getBackground());
        toggleLiveButton.addActionListener(e -> {
            for (var filter : validationFilterList) {
                filter.setLiveValidation(!filter.isLiveValidation());
                filter.setValidateOnFocusLost(!filter.isValidateOnFocusLost());
            }
        });

        var layout = new GroupLayout(panel);

        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addComponent(cancelButton)
                        .addComponent(toggleLiveButton)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup()
                                          .addComponent(okButton)
                                          .addComponent(cancelButton)
                                          .addComponent(toggleLiveButton))
        );
        layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);

        panel.setLayout(layout);

        return panel;
    }
}
