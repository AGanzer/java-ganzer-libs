package com.example.uitests.swingdv.doc.ol;

import de.ganzer.swing.controls.GComboBox;
import de.ganzer.swing.controls.GTextArea;
import de.ganzer.swing.controls.GTextField;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class OLEditor extends JPanel {
    private final OLView parentView;
    private final InputContainer predefinedOLSystem;
    private final InputContainer olName;
    private final InputContainer olAxiom;
    private final InputContainer olAngle;
    private final InputContainer olCycles;
    private final InputContainer olReplacements;

    public OLEditor(OLView parentView) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        this.parentView = parentView;

        predefinedOLSystem = new InputContainer(new JLabel("Predefined OL-Systems:"),
                                                new GComboBox<>());
        olName = new InputContainer(new JLabel("Name:"),
                                    new GTextField());
        olAxiom = new InputContainer(new JLabel("Axiom:"),
                                     new GTextField());
        olAngle = new InputContainer(new JLabel("Angle:"),
                                     new JSpinner());
        olCycles = new InputContainer(new JLabel("Cycles:"),
                                      new JSpinner());
        olReplacements = new InputContainer(new JLabel("Replacements:"),
                                            new GTextArea());

        setupControls();
        layoutControls();
    }

    public void setOLName(String olName) {
        this.olName.getTextField().setText(olName);
    }

    public void setOLAxiom(String olAxiom) {
        this.olAxiom.getTextField().setText(olAxiom);
    }

    public void setOLAngle(int olAngle) {
        this.olAngle.getSpinner().setValue(olAngle);
    }

    public void setOLCycles(int olCycles) {
        this.olCycles.getSpinner().setValue(olCycles);
    }

    public void setOLReplacements(String olReplacements) {
        this.olReplacements.getTextArea().setText(olReplacements);
    }

    public void setOLSystem(OLSystem system) {
        predefinedOLSystem.getComboBox().setSelectedItem(system);
    }

    private void setupControls() {
        setupPredefinedSystems();

        olAngle.getSpinner().setModel(new SpinnerNumberModel(0, 0, 360, 10));
        ((NumberFormatter) ((JSpinner.DefaultEditor) olAngle.getSpinner().getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);

        olCycles.getSpinner().setModel(new SpinnerNumberModel(1, 1, 12, 1));
        ((NumberFormatter) ((JSpinner.DefaultEditor) olCycles.getSpinner().getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
    }

    private void setupPredefinedSystems() {
        //noinspection unchecked
        var combo = (GComboBox<OLSystem>) predefinedOLSystem.getComboBox();

        combo.addItem(null);

        for (var item : OLSystem.getPredefinedSystems())
            combo.addItem(item);

        combo.addActionListener(e -> {
            var system = combo.getSelectedItem();

            if (system == null)
                system = new OLSystem("", "F", 0, 1, "F:F+");

            parentView.getDocument().setOLSystem(system, parentView);

            setOLName(parentView.getDocument().getOLName());
            setOLAxiom(parentView.getDocument().getOLAxiom());
            setOLAngle(parentView.getDocument().getOLAngle());
            setOLCycles(parentView.getDocument().getOLCycles());
            setOLReplacements(parentView.getDocument().getOLReplacements());
        });
    }

    private void layoutControls() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 6, 0);
        c.gridx = 0;
        c.gridy = 0;
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
        c.insets = new Insets(0, 0, 0, 0);
        add(olCycles, c);
    }

    private static class InputContainer extends JPanel {
        private final JLabel label;
        private final JComponent component;

        public InputContainer(JLabel label, JComponent component) {
            super(new BorderLayout(0, 3));

            this.label = label;
            this.component = component;

            add(label, BorderLayout.NORTH);
            add(component, BorderLayout.CENTER);
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
}
