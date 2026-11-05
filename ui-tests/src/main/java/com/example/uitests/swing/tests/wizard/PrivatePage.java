package com.example.uitests.swing.tests.wizard;

import de.ganzer.swing.controls.GTextField;
import de.ganzer.swing.controls.WizardPage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class PrivatePage extends JPanel implements WizardPage {
    private final WizardData data;
    private final GTextField phone;
    private final GTextField email;

    public PrivatePage(WizardData data) {
        super(new GridBagLayout());

        this.data = data;
        this.phone = new GTextField(20);
        this.email = new GTextField(20);

        layoutControls();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void updatePageControls() {
        phone.setText(data.privatePhone);
        email.setText(data.privateEmail);
    }

    @Override
    public void updateWizardData() {
        data.privatePhone = phone.getText();
        data.privateEmail = email.getText();
    }

    @Override
    public boolean validateInput() {
        return true;
    }

    private void layoutControls() {
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.insets = new Insets(3, 3, 3, 3);
        add(new JLabel("Private Phone:"), c);

        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(phone, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        add(new JLabel("Private Email:"), c);

        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(email, c);
    }
}
