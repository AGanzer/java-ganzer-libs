package com.example.uitests.swing.tests.wizard;

import de.ganzer.swing.controls.WizardPage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

public class WelcomePage extends JPanel implements WizardPage {
    public WelcomePage() {
        super(new BorderLayout());

        var textLabel = new JLabel("Welcome to the wizard!");
        textLabel.setFont(textLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);

        add(textLabel, BorderLayout.CENTER);
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void updatePageControls() {
    }

    @Override
    public void updateWizardData() {
    }

    @Override
    public boolean validateInput() {
        return true;
    }
}
