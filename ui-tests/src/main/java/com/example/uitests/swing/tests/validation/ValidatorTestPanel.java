package com.example.uitests.swing.tests.validation;

import de.ganzer.swing.dialogs.ModifiableDataSupport;
import de.ganzer.swing.dlgfw.AbstractModifiableDataPanel;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public class ValidatorTestPanel extends AbstractModifiableDataPanel<Void> {
    private JTabbedPane tabbedPane;

    public ValidatorTestPanel(ModifiableDataSupport<Void> owner) {
        super(owner);
    }

    @Override
    protected JComponent createCenterPanel() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Currency Validator", new CurrencyValidatorPane());
        tabbedPane.addTab("Number Validator", new NumberValidatorPane());

        return tabbedPane;
    }

    @Override
    protected JComponent createTitleLabel() {
        return new JLabel("Validator Tests");
    }

    @Override
    public boolean validateInput() {
        return ((ValidatorTestPane) tabbedPane.getSelectedComponent()).validateInput();
    }

    @Override
    public void updateData(Void unused) {
    }

    @Override
    public void initControls(Void unused) {
    }
}
