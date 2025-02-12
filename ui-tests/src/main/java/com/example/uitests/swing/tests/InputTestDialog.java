package com.example.uitests.swing.tests;

import de.ganzer.core.validation.PxPicValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class InputTestDialog extends JDialog {
    public static class Data {
        public String input;
    }

    public static boolean showModal(Frame owner, Data data) {
        InputTestDialog dialog = new InputTestDialog(owner, data);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);

        return dialog.okClicked;
    }

    private final Data data;
    private ValidationFilter inputFilter;
    private boolean okClicked;

    private InputTestDialog(Frame owner, Data data) {
        super(owner, "Input Test", true);
        this.data = data;
        updateControls();
    }

    @Override
    protected void dialogInit() {
        super.dialogInit();
        init();
    }

    private void updateControls() {
        inputFilter.getTextField().setText(data.input);
    }

    private void init() {
        getContentPane().setLayout(new BorderLayout());
        initTextField();
        initButtons();

        pack();
    }

    private void initTextField() {
        JTextField inputField = new JTextField(30);

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pane.add(inputField);
        getContentPane().add(pane, BorderLayout.CENTER);

        inputFilter = new ValidationFilter(initValidator(), inputField);
    }

    private Validator initValidator() {
//        var val = new NumberValidator(-9999999.99, 9999999.99);
//        val.setNumDecimals(2);
//        return val;

//        return new PxPicValidator("{White,Gr{ay,een},B{l{ack,ue},rown},Red}");
        return new PxPicValidator("*&");
    }

    private void initButtons() {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(this::onOk);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this::onCancel);
        okButton.setPreferredSize(cancelButton.getPreferredSize());

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(okButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(cancelButton);

        getContentPane().add(buttonPane, BorderLayout.PAGE_END);

        getRootPane().setDefaultButton(okButton);
        getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CANCEL");
        getRootPane().getActionMap().put("CANCEL", new GAction().onAction(e -> onCancel(null)));
    }

    private void onOk(ActionEvent event) {
//        if (!inputFilter.validate(ValidationBehavior.SET_VISUAL_HINTS))
        if (!inputFilter.validate(ValidationBehavior.SHOW_MESSAGE_BOX))
            return;

        data.input = inputFilter.getText();
        okClicked = true;

        dispose();
    }

    private void onCancel(ActionEvent event) {
        dispose();
    }
}
