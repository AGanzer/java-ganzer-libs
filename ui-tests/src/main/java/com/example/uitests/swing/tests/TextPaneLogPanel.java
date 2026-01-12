package com.example.uitests.swing.tests;

import de.ganzer.swing.logging.TextComponentLogTarget;

import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;
import java.awt.Font;

public class TextPaneLogPanel extends LogPanel {
    private final String contentType;
    private final int messageWaitTimeOut;

    private JTextPane textPane;
    private TextComponentLogTarget logTarget;

    public TextPaneLogPanel(String contentType, int messageWaitTimeOut) {
        this.contentType = contentType;
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
            textPane = new JTextPane();
            textPane.setEditable(false);
            textPane.setContentType(contentType);

            if (contentType.contains("html")) {
                String css = "body { font-family: Monospace; font-size: 12pt; }";
                ((HTMLDocument) textPane.getDocument()).getStyleSheet().addRule(css);
            } else {
                textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            }
        }

        return textPane;
    }
}
