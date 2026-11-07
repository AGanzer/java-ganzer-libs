package com.example.uitests.swing.tests.validation;

import de.ganzer.swing.dlgfw.AbstractDialog;
import de.ganzer.swing.dlgfw.AbstractModifiableDataDialog;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Window;

public class ValidatorTestDialog extends AbstractModifiableDataDialog<Void> {
    public ValidatorTestDialog(Window owner) {
        super(owner);
        pack();
        setLocationRelativeTo(owner);
        super.setDataModified(true);
    }

    public static boolean showModel(Window owner) {
        ValidatorTestDialog dialog = new ValidatorTestDialog(owner);
        dialog.setVisible(true);

        return dialog.isAccepted();
    }

    @Override
    protected JPanel createCenterPanel() {
        return new ValidatorTestPanel(this);
    }

    @Override
    protected AbstractButton[] createWindowButtons() {
        var buttons = super.createWindowButtons();

        buttons[0] = new JButton("Validate Input");
        buttons[1].setText("Close");

        getRootPane().setDefaultButton((JButton) buttons[0]);

        buttons[0].addActionListener(e -> ((ValidatorTestPanel) getCenterPanel()).validateInput());

        return buttons;
    }
}
