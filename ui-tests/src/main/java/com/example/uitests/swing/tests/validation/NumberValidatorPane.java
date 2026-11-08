package com.example.uitests.swing.tests.validation;

import de.ganzer.core.validation.NumberValidator;
import de.ganzer.core.validation.ValidatorOptions;
import de.ganzer.swing.controls.GComboBox;
import de.ganzer.swing.controls.GTextField;
import de.ganzer.swing.dlgfw.messages.MessageDialog;
import de.ganzer.swing.dlgfw.messages.MessageType;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;
import de.ganzer.swing.validaton.ValidationFilterList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class NumberValidatorPane extends JPanel implements ValidatorTestPane {
    private final GTextField input;
    private final GTextField numDecimals;
    private final GTextField minValue;
    private final GTextField maxValue;
    private final JCheckBox needsInput;
    private final GComboBox<Locale> locales;
    private final ValidationFilter inputFilter;
    private final ValidationFilterList settingsFilter;
    private final NumberValidator minMaxValidator;
    private final NumberValidator numDecimalsValidator;

    public NumberValidatorPane() {
        super(new GridBagLayout());

        var format = NumberFormat.getNumberInstance();
        var validator = new NumberValidator();

        minMaxValidator = new NumberValidator();
        numDecimalsValidator = new NumberValidator(0, 6);

        input = new GTextField();
        inputFilter = new ValidationFilter(validator, input);
        settingsFilter = new ValidationFilterList();

        numDecimals = new GTextField(String.valueOf(validator.getNumDecimals()));
        settingsFilter.addFilter(new ValidationFilter(numDecimalsValidator, numDecimals));

        minValue = new GTextField(format.format(validator.getMinValue()));
        settingsFilter.addFilter(new ValidationFilter(minMaxValidator, minValue));

        maxValue = new GTextField(format.format(validator.getMaxValue()));
        settingsFilter.addFilter(new ValidationFilter(minMaxValidator, maxValue));

        needsInput = new JCheckBox("Needs Input");
        needsInput.setSelected(validator.hasOption(ValidatorOptions.NEEDS_INPUT));

        locales = new GComboBox<>();

        for (var loc : Arrays.stream(Locale.getAvailableLocales()).sorted(Comparator.comparing(Locale::toString)).toList())
            locales.addItem(loc);

        locales.setSelectedItem(Locale.getDefault());
        locales.addActionListener(e -> input.setText(""));

        layoutControls();
    }

    @Override
    public boolean validateInput() {
        return updateInputFilter() && inputFilter.validate(ValidationBehavior.SHOW_MESSAGE_BOX);
    }

    private boolean updateInputFilter() {
        if (!settingsFilter.validate(ValidationBehavior.SHOW_MESSAGE_BOX))
            return false;

        int options = ValidatorOptions.NONE;

        if (needsInput.isSelected())
            options |= ValidatorOptions.NEEDS_INPUT;

        NumberValidator validator = null;

        try {
            validator = new NumberValidator(options,
                                              minMaxValidator.asDouble(minValue.getText()),
                                              minMaxValidator.asDouble(maxValue.getText()),
                                              numDecimalsValidator.asInt(numDecimals.getText()));
            validator.setLocale(locales.getSelectedItem());
        } catch (ParseException e) {
            MessageDialog.show(SwingUtilities.getWindowAncestor(this), e.getLocalizedMessage(), MessageType.ERROR);
        }

        inputFilter.setValidator(validator);

        return true;
    }

    private void layoutControls() {
        setBorder(new EmptyBorder(7, 7, 7, 7));

        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);
        add(new JLabel("Text Input:"), c);

        c.gridx++;
        c.weightx = 1;
        add(input, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        add(new JLabel("Locale:"), c);

        c.gridx++;
        c.weightx = 1;
        add(locales, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        add(new JLabel("No. of decimals:"), c);

        c.gridx++;
        c.weightx = 1;
        add(numDecimals, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        add(new JLabel("Min. Value:"), c);

        c.gridx++;
        c.weightx = 1;
        add(minValue, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        add(new JLabel("Max. Value:"), c);

        c.gridx++;
        c.weightx = 1;
        add(maxValue, c);

        c.gridy++;
        c.weighty = 1;
        add(needsInput, c);
    }
}
