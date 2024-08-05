package com.example.uitests.swing.tests;

import de.ganzer.core.validation.NumberValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.swing.actions.CreateOptions;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.actions.GActionEvent;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
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
    private JTextField input;
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
        input = new JTextField(data.input, 30);
        ((AbstractDocument)input.getDocument()).setDocumentFilter(new InputFilter(initValidator()));

        var pane = new JPanel();
        pane.setLayout(new FlowLayout());

        pane.add(input);
        getContentPane().add(pane, BorderLayout.CENTER);
    }

    private Validator initValidator() {
        var val = new NumberValidator(-9999999.99, 9999999.99);
        val.setNumDecimals(2);

        return val;
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
        data.input = this.input.getText();
        okClicked = true;

        setVisible(false);
    }

    private void onCancel(GActionEvent event) {
        setVisible(false);
    }

    private static class InputFilter extends DocumentFilter {
        private final Validator validator;

        private InputFilter(Validator validator) {
            this.validator = validator;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            var input = new StringBuilder(string);

            if (validator.isValidInput(input, true))
                super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
