package com.example.uitests.swing.tests.wizard;

import de.ganzer.core.validation.Validator;
import de.ganzer.swing.controls.GComboBox;
import de.ganzer.swing.controls.GTextField;
import de.ganzer.swing.controls.WizardPage;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;
import de.ganzer.swing.validaton.ValidationFilterList;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagLayout;

public class NamePage extends JPanel implements WizardPage {
    private final WizardData data;
    private final GTextField forename;
    private final GTextField surname;
    private final GComboBox<Role> role;
    private final ValidationFilterList validationFilters;

    public NamePage(WizardData data) {
        super(new GridBagLayout());

        this.data = data;
        this.role = new GComboBox<>();
        this.forename = new GTextField();
        this.surname = new GTextField();

        var nameValidator = new Validator();

        validationFilters = new ValidationFilterList();
        validationFilters.addFilter(new ValidationFilter(nameValidator, forename));
        validationFilters.addFilter(new ValidationFilter(nameValidator, surname));
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
}
