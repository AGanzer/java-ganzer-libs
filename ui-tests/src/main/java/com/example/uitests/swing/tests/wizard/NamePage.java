package com.example.uitests.swing.tests.wizard;

import de.ganzer.core.validation.Validator;
import de.ganzer.swing.controls.GComboBox;
import de.ganzer.swing.controls.GTextField;
import de.ganzer.swing.controls.WizardPage;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;
import de.ganzer.swing.validaton.ValidationFilterList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class NamePage extends JPanel implements WizardPage {
    private final WizardData data;
    private final GTextField forename;
    private final GTextField surname;
    private final GComboBox<Role> role;
    private final ValidationFilterList validationFilters;

    public NamePage(WizardData data) {
        super(new GridBagLayout());

        this.data = data;
        this.role = new GComboBox<>(new Role[] {Role.PRIVATE, Role.EMPLOYEE});
        this.forename = new GTextField(20);
        this.surname = new GTextField(20);

        var nameValidator = new Validator();

        validationFilters = new ValidationFilterList();
        validationFilters.addFilter(new ValidationFilter(nameValidator, forename));
        validationFilters.addFilter(new ValidationFilter(nameValidator, surname));

        layoutControls();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void updatePageControls() {
        role.setSelectedItem(data.role);
        forename.setText(data.forename);
        surname.setText(data.surname);
    }

    @Override
    public void updateWizardData() {
        data.forename = forename.getText();
        data.surname = surname.getText();
        data.role = role.getSelectedItem();
    }

    @Override
    public boolean validateInput() {
        return validationFilters.validate(ValidationBehavior.SHOW_MESSAGE_BOX);
    }

    private void layoutControls() {
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.insets = new Insets(3, 3, 3, 3);
        add(new JLabel("Forename:"), c);

        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(forename, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        add(new JLabel("Surname:"), c);

        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(surname, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        add(new JLabel("Role:"), c);

        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(role, c);
    }
}
