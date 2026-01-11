package com.example.uitests.swing.tests;

import de.ganzer.swing.logging.TextComponentLogTarget;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class TextAreaLogPanel extends LogPanel {
    private final int messageWaitTimeOut;

    private JTextArea textPane;
    private TextComponentLogTarget logTarget;

    public TextAreaLogPanel(int messageWaitTimeOut) {
        this.messageWaitTimeOut = messageWaitTimeOut;
    }

    @Override
    protected TextComponentLogTarget getLogTarget() {
        if (logTarget == null) {
            logTarget = new TextComponentLogTarget(6,
                                                   getTextComponent(),
                                                   messageWaitTimeOut);
        }

        return logTarget;
    }

    @Override
    protected JTextComponent getTextComponent() {
        if (textPane == null) {
            textPane = new JTextArea();
            textPane.setEditable(false);
        }

        return textPane;
    }
}
