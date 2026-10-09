package com.example.uitests.swingdv.doc.ol;

import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.core.validation.ValidatorExceptionRef;
import de.ganzer.swing.controls.GComboBox;
import de.ganzer.swing.controls.GTextArea;
import de.ganzer.swing.controls.GTextField;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;
import de.ganzer.swing.validaton.ValidationFilterList;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class OLEditor extends JPanel {
    private final ValidationFilterList validationFilters = new ValidationFilterList();
    private final AtomicBoolean updating = new AtomicBoolean(false);
    private final OLView parentView;

    private InputContainer predefinedOLSystem;
    private InputContainer olName;
    private InputContainer olAxiom;
    private InputContainer olAngle;
    private InputContainer olCycles;
    private InputContainer olReplacements;
    private OLSystem latestEditedOLSystem;

    public OLEditor(OLView parentView) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        this.parentView = parentView;
        latestEditedOLSystem = parentView.getDocument().getOLSystem();

        createControls();
        setupControls();
        layoutControls();
        updateControls();
    }

    public void setOLName(String olName) {
        updating.set(true);

        try {
            this.olName.getTextField().setText(olName);
        } finally {
            updating.set(false);
        }
    }

    public void setOLAxiom(String olAxiom) {
        updating.set(true);

        try {
            this.olAxiom.getTextField().setText(olAxiom);
        } finally {
            updating.set(false);
        }
    }

    public void setOLAngle(int olAngle) {
        updating.set(true);

        try {
            this.olAngle.getSpinner().setValue(olAngle);
        } finally {
            updating.set(false);
        }
    }

    public void setOLCycles(int olCycles) {
        updating.set(true);

        try {
            this.olCycles.getSpinner().setValue(olCycles);
        } finally {
            updating.set(false);
        }
    }

    public void setOLReplacements(String olReplacements) {
        updating.set(true);

        try {
            this.olReplacements.getTextArea().setText(olReplacements);
        } finally {
            updating.set(false);
        }
    }

    public void setOLSystem(OLSystem system) {
        updating.set(true);

        try {
            if (system.isPredefined())
                predefinedOLSystem.getComboBox().setSelectedItem(system);
            else
                predefinedOLSystem.getComboBox().setSelectedItem(null);

            updateControls();
        } finally {
            updating.set(false);
        }
    }

    public boolean isInputValid() {
        return validationFilters.validate(ValidationBehavior.SHOW_MESSAGE_BOX);
    }

    private void createControls() {
        predefinedOLSystem = new InputContainer(new JLabel("Predefined OL-Systems:"), new GComboBox<>(), null);
        olName = new InputContainer(new JLabel("Name:"), new GTextField(), OLDocument.ChangeContext.NAME);
        olAxiom = new InputContainer(new JLabel("Axiom:"), new GTextField(), OLDocument.ChangeContext.AXIOM);
        olAngle = new InputContainer(new JLabel("Angle:"), new JSpinner(), null);
        olCycles = new InputContainer(new JLabel("Cycles:"), new JSpinner(), null);
        olReplacements = new InputContainer(new JLabel("Replacements:"), new GTextArea(), OLDocument.ChangeContext.REPLACEMENTS);
    }

    private void setupControls() {
        setupPredefinedSystems();

        olAngle.getSpinner().setModel(new SpinnerNumberModel(0, 0, 360, 10));
        ((NumberFormatter) ((JSpinner.DefaultEditor) olAngle.getSpinner().getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
        olAngle.getSpinner().addChangeListener(e -> parentView.getDocument().setOLAngle((int) olAngle.getSpinner().getValue(), parentView));
        ((JSpinner.DefaultEditor) olAngle.getSpinner().getEditor()).getTextField().addFocusListener(new MyFocusListener(OLDocument.ChangeContext.ANGLE));

        olCycles.getSpinner().setModel(new SpinnerNumberModel(1, 1, 12, 1));
        ((NumberFormatter) ((JSpinner.DefaultEditor) olCycles.getSpinner().getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
        olCycles.getSpinner().addChangeListener(e -> {
            if (!parentView.getDocument().getOLSystem().isPredefined())
                parentView.getDocument().setOLCycles((int) olCycles.getSpinner().getValue(), parentView);
        });
        ((JSpinner.DefaultEditor) olCycles.getSpinner().getEditor()).getTextField().addFocusListener(new MyFocusListener(OLDocument.ChangeContext.CYCLES));

        validationFilters.addFilter(new ValidationFilter(new Validator(), olAxiom.getTextField()));
        validationFilters.addFilter(new ValidationFilter(new ReplacementsValidator(), olReplacements.getTextArea()));
    }

    private void setupPredefinedSystems() {
        //noinspection unchecked
        var combo = (GComboBox<OLSystem>) predefinedOLSystem.getComboBox();

        combo.addItem(null);

        for (var item : OLSystem.getPredefinedSystems())
            combo.addItem(item);

        combo.addActionListener(e -> {
            if (updating.get())
                return;

            var system = combo.getSelectedItem();

            if (system == null)
                system = latestEditedOLSystem;
            else if (!parentView.getDocument().getOLSystem().isPredefined())
                latestEditedOLSystem = parentView.getDocument().getOLSystem();

            parentView.getDocument().setOLSystem(system, parentView);
            updateControls();
        });
    }

    private void layoutControls() {
        var apply = new JButton("Apply");
        apply.addActionListener(e -> parentView.generate((int) olCycles.getSpinner().getValue()));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 6, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(predefinedOLSystem, c);

        c.gridy++;
        add(olName, c);

        c.gridy++;
        add(olAxiom, c);

        c.gridy++;
        add(olAngle, c);

        c.gridy++;
        c.weighty = 1;
        add(olReplacements, c);

        c.gridy++;
        c.weighty = 0;
        add(olCycles, c);

        c.gridy++;
        c.insets = new Insets(6, 0, 0, 0);
        add(apply, c);
    }

    private void updateControls() {
        olName.getTextField().setText(parentView.getDocument().getOLName());
        olAxiom.getTextField().setText(parentView.getDocument().getOLAxiom());
        olAngle.getSpinner().setValue(parentView.getDocument().getOLAngle());
        olCycles.getSpinner().setValue(parentView.getDocument().getOLCycles());
        olReplacements.getTextArea().setText(parentView.getDocument().getOLReplacements());

        var enable = !parentView.getDocument().getOLSystem().isPredefined();

        olName.setEnabled(enable);
        olAxiom.setEnabled(enable);
        olAngle.setEnabled(enable);
        olReplacements.setEnabled(enable);
    }

    private class InputContainer extends JPanel {
        private final JLabel label;
        private final JComponent component;

        public InputContainer(JLabel label, JComponent component, OLDocument.ChangeContext context) {
            super(new BorderLayout(0, 3));

            this.label = label;
            this.component = component;

            add(label, BorderLayout.NORTH);

            if (component instanceof JTextArea)
                add(new JScrollPane(component), BorderLayout.CENTER);
            else
                add(component, BorderLayout.CENTER);

            if (context != null)
                component.addFocusListener(new MyFocusListener(context));
        }

        public JTextField getTextField() {
            return (JTextField) component;
        }

        public JTextArea getTextArea() {
            return (JTextArea) component;
        }

        public JSpinner getSpinner() {
            return (JSpinner) component;
        }

        public JComboBox<?> getComboBox() {
            return (JComboBox<?>) component;
        }

        public void setEnabled(boolean enabled) {
            label.setEnabled(enabled);
            component.setEnabled(enabled);
        }
    }

    private static class ReplacementsValidator extends Validator {
        @Override
        protected boolean doValidate(String text, ValidatorExceptionRef er) {
            if (!super.doValidate(text, er))
                return false;

            if (text.isEmpty())
                return true;

            for (String replacement : text.split("\n")) {
                var parts = replacement.split(":");

                if (parts.length != 2 || parts[0].trim().length() != 1 || parts[1].trim().isEmpty()) {
                    er.setException(new ValidatorException("Invalid replacement format.", ReplacementsValidator.class, this));
                    return false;
                }

            }

            return true;
        }
    }

    private class MyFocusListener implements FocusListener {
        private final OLDocument.ChangeContext context;

        private MyFocusListener(OLDocument.ChangeContext context) {
            this.context = context;
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (parentView.getDocument().getOLSystem().isPredefined())
                return;

            switch (context) {
                case NAME -> parentView.getDocument().setOLName(olName.getTextField().getText(), parentView);
                case AXIOM -> parentView.getDocument().setOLAxiom(olAxiom.getTextField().getText(), parentView);
                case ANGLE -> parentView.getDocument().setOLAngle((int) olAngle.getSpinner().getValue(), parentView);
                case CYCLES -> parentView.getDocument().setOLCycles((int) olCycles.getSpinner().getValue(), parentView);
                case REPLACEMENTS -> parentView.getDocument().setOLReplacements(olReplacements.getTextArea().getText(), parentView);
            }
        }
    }
}
