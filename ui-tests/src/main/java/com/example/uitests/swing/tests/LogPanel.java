package com.example.uitests.swing.tests;

import de.ganzer.core.logging.Logger;
import de.ganzer.swing.logging.TextComponentLogTarget;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Random;

public abstract class LogPanel extends JPanel {
    private final Logger logger = new Logger();

    public LogPanel() {
        super(new BorderLayout());
    }

    public LogPanel init() {
        JScrollPane scrollPane = new JScrollPane(getTextComponent());

        add(scrollPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        logger.addTarget("1", getLogTarget());

        return this;
    }

    protected abstract TextComponentLogTarget getLogTarget();

    protected abstract JTextComponent getTextComponent();

    private Component createButtonPanel() {
        JButton scrollToEndButton = new JButton("Scroll To End");
        scrollToEndButton.addActionListener(e -> getTextComponent().setCaretPosition(getTextComponent().getDocument().getLength()));

        JButton startLoggingButton = new JButton("Start Logging");
        startLoggingButton.addActionListener(e -> startLogging());

        JPanel panel = new JPanel(null);
        var layout = new GroupLayout(panel);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        layoutHorizontally(layout, startLoggingButton, scrollToEndButton);

        return panel;
    }

    private static final String MESSAGE_FORMAT = "This is the %d. log message of level %d.";
    private int counter;
    private final Random random = new Random();

    private void startLogging() {
        for (int i = 0; i < 100; i++) {
            int level = random.nextInt(10);
            var message = String.format(MESSAGE_FORMAT, ++counter, level);

            logger.write(level, message);
        }
    }

    private void layoutHorizontally(GroupLayout layout, AbstractButton... buttons) {
        GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
        horizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        for (AbstractButton button : buttons)
            horizontalGroup.addComponent(button);

        horizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

        for (AbstractButton button : buttons)
            parallelGroup.addComponent(button);

        verticalGroup.addGroup(parallelGroup);

        layout.setHorizontalGroup(horizontalGroup);
        layout.setVerticalGroup(verticalGroup);

        layout.linkSize(SwingConstants.HORIZONTAL, buttons);
    }
}
