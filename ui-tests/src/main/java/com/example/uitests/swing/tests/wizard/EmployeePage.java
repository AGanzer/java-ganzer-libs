package com.example.uitests.swing.tests.wizard;

import de.ganzer.core.validation.NumberValidator;
import de.ganzer.swing.controls.GTextField;
import de.ganzer.swing.controls.WizardPage;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

public class EmployeePage extends JPanel implements WizardPage {
    private final WizardData data;
    private final GTextField phone;
    private final GTextField email;
    private final GTextField salary;
    private final ValidationFilter salaryValidationFilter;

    public EmployeePage(WizardData data) {
        super(new GridBagLayout());

        this.data = data;
        this.phone = new GTextField(20);
        this.email = new GTextField(20);
        this.salary = new GTextField(20);

        salaryValidationFilter = new ValidationFilter(new NumberValidator(10.0, 100000.0, 2), salary);

        layoutControls();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void updatePageControls() {
        var formatter = NumberFormat.getNumberInstance();

        phone.setText(data.companyPhone);
        email.setText(data.companyEmail);
        salary.setText(formatter.format(data.salary));
    }

    @Override
    public void updateWizardData() {
        var formatter = NumberFormat.getNumberInstance();

        data.companyPhone = phone.getText();
        data.companyEmail = email.getText();

        try {
            data.salary = BigDecimal.valueOf(formatter.parse(salary.getText()).doubleValue());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateInput() {
        return salaryValidationFilter.validate(ValidationBehavior.SHOW_MESSAGE_BOX);
    }

    private void layoutControls() {
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.insets = new Insets(3, 3, 3, 3);
        add(new JLabel("Company Phone:"), c);

        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(phone, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        add(new JLabel("Company Email:"), c);

        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(email, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        add(new JLabel("Salary:"), c);

        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(salary, c);
    }
}
