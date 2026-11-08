package com.example.uitests.swing.tests.validation;

import de.ganzer.core.validation.DateValidator;
import de.ganzer.core.validation.ValidatorOptions;
import de.ganzer.swing.controls.GComboBox;
import de.ganzer.swing.controls.GTextField;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;
import de.ganzer.swing.validaton.ValidationFilterList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class DateValidatorPane extends JPanel implements ValidatorTestPane {
    private final GTextField input;
    private final GTextField pattern;
    private final GTextField minValue;
    private final GTextField maxValue;
    private final GComboBox<FormatStyle> formats;
    private final JCheckBox needsInput;
    private final JCheckBox autoFill;
    private final GComboBox<Locale> locales;
    private final ValidationFilter inputFilter;
    private final ValidationFilterList settingsFilter;
    private final DateValidator minMaxValidator;

    public DateValidatorPane() {
        super(new GridBagLayout());

        var format = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        var validator = new DateValidator(LocalDate.of(1000, 1, 1),
                                          LocalDate.of(9999, 12, 31));

        minMaxValidator = new DateValidator(validator.getMinDate(),
                                            validator.getMaxDate(),
                                            FormatStyle.MEDIUM);

        input = new GTextField();
        inputFilter = new ValidationFilter(validator, input);
        settingsFilter = new ValidationFilterList();

        var focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateInputFilter();
            }
        };

        pattern = new GTextField();
        pattern.addFocusListener(focusListener);
        pattern.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                input.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                input.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                input.setText("");
            }
        });

        minValue = new GTextField(format.format(minMaxValidator.getMinDate()));
        minValue.addFocusListener(focusListener);
        settingsFilter.addFilter(new ValidationFilter(minMaxValidator, minValue));

        maxValue = new GTextField(format.format(minMaxValidator.getMaxDate()));
        maxValue.addFocusListener(focusListener);
        settingsFilter.addFilter(new ValidationFilter(minMaxValidator, maxValue));

        needsInput = new JCheckBox("Needs Input");
        needsInput.setSelected(validator.hasOption(ValidatorOptions.NEEDS_INPUT));
        needsInput.addActionListener(e -> updateInputFilter());

        autoFill = new JCheckBox("Fill Separators Automatically");
        autoFill.setSelected(validator.hasOption(ValidatorOptions.AUTO_FILL));
        autoFill.addActionListener(e -> updateInputFilter());

        this.formats = new GComboBox<>();
        this.formats.addItem(FormatStyle.SHORT);
        this.formats.addItem(FormatStyle.MEDIUM);
        this.formats.setSelectedItem(validator.getFormatStyle());
        this.formats.addActionListener(e -> {
            input.setText("");
            updateInputFilter();
        });

        locales = new GComboBox<>();

        for (var loc : Arrays.stream(Locale.getAvailableLocales()).sorted(Comparator.comparing(Locale::toString)).toList())
            locales.addItem(loc);

        locales.setSelectedItem(Locale.getDefault());
        locales.addActionListener(e -> {
            input.setText("");
            updateInputFilter();
        });

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

        if (autoFill.isSelected())
            options |= ValidatorOptions.AUTO_FILL;

        var validator = new DateValidator(options,
                                      minMaxValidator.asLocalDate(minValue.getText()),
                                      minMaxValidator.asLocalDate(maxValue.getText()),
                                      formats.getSelectedItem());
        validator.setLocale(locales.getSelectedItem());
        validator.setPatternString(pattern.getText().isEmpty() ? null : pattern.getText());

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
        c.anchor = GridBagConstraints.LINE_START;
        add(new JLabel("Locale:"), c);

        c.gridx++;
        c.weightx = 1;
        add(locales, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        add(new JLabel("Format Style:"), c);

        c.gridx++;
        c.weightx = 1;
        add(formats, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 0;
        add(new JLabel("Pattern String:"), c);

        c.gridx++;
        c.weightx = 1;
        add(pattern, c);

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
        add(needsInput, c);

        c.gridy++;
        c.weighty = 1;
        add(autoFill, c);
    }
}
