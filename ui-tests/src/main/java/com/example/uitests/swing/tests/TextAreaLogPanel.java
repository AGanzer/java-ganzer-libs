package com.example.uitests.swing.tests;

import de.ganzer.swing.logging.TextComponentLogTarget;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import java.awt.Font;

public class TextAreaLogPanel extends LogPanel {
    private final int messageWaitTimeOut;

    private JTextArea textArea;
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
        if (textArea == null) {
            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        }

        return textArea;
    }
}
