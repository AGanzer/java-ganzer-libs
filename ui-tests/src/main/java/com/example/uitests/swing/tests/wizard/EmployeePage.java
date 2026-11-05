package com.example.uitests.swing.tests.wizard;

import de.ganzer.swing.controls.WizardPage;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagLayout;

public class EmployeePage extends JPanel implements WizardPage {
    private final WizardData data;

    public EmployeePage(WizardData data) {
        super(new GridBagLayout());
        this.data = data;
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
