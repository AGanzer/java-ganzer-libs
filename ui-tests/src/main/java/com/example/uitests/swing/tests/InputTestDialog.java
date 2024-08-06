package com.example.uitests.swing.tests;

import de.ganzer.core.validation.NumberValidator;
import de.ganzer.core.validation.PxPicValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.swing.actions.CreateOptions;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.actions.GActionEvent;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;

import javax.swing.*;
import java.awt.*;

public class InputTestDialog extends JDialog {
    public static class Data {
        public String input;
    }

    public static boolean showModal(Frame owner, Data data) {
        var dialog = new InputTestDialog(owner, data);
        dialog.setVisible(true);

        return dialog.okClicked;
    }

    private final Data data;
    private ValidationFilter inputFilter;
    private boolean okClicked;

    private InputTestDialog(Frame owner, Data data) {
        super(owner, "Input Test", true);
        this.data = data;

        init();
    }

    private void init() {
        getContentPane().setLayout(new BorderLayout());
        initTextField();
        initButtons();

        pack();
    }

    private void initTextField() {
        var inputField = new JTextField(data.input, 30);

        var pane = new JPanel();
        pane.setLayout(new FlowLayout());

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
        GAction ok = new GAction("OK")
                .onAction(this::onOk);
        GAction cancel = new GAction("Cancel")
                .onAction(this::onCancel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(ok.createButton(CreateOptions.FOCUSABLE));
        buttonPane.add(cancel.createButton(CreateOptions.FOCUSABLE));

        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
    }

    private void onOk(GActionEvent event) {
        if (!inputFilter.validate(ValidationBehavior.SET_VISUAL_HINTS))
            return;

        data.input = inputFilter.getText();
        okClicked = true;

        setVisible(false);
    }

    private void onCancel(GActionEvent event) {
        setVisible(false);
    }
}
