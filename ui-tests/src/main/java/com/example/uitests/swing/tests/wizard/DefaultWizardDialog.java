package com.example.uitests.swing.tests.wizard;

import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.controls.DefaultWizardModel;
import de.ganzer.swing.controls.WizardController;
import de.ganzer.swing.controls.WizardPane;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;

public class DefaultWizardDialog extends JDialog {
    private boolean finished;

    public static boolean showModel(Window owner, WizardData data) {
        var dialog = new DefaultWizardDialog(owner, data);
        dialog.setVisible(true);

        return dialog.finished;
    }

    public DefaultWizardDialog(Window owner, WizardData data) {
        super(owner, "Default Wizard Test", Dialog.ModalityType.APPLICATION_MODAL);

        var wizard = new WizardPane();
        wizard.setBorder(new EmptyBorder(10, 10, 10, 10));

        var model = (DefaultWizardModel) wizard.getModel();
        model.addPage(new WelcomePage());
        model.addPage(new NamePage(data));
        model.addPage(new PrivatePage(data));
        model.addPage(new EmployeePage(data));
        model.addPage(new SummaryPage(data));

        var prevButton = wizard.getPrevAction().createButton();
        var nextButton = wizard.getNextAction().onAction(e -> {
            if (WizardController.FINISH_COMMAND.equals(e.getActionCommand())) {
                finished = true;
                setVisible(false);
            }
        }).createButton();
        var cancelButton = new GAction("Cancel").onAction(e -> setVisible(false)).createButton();

        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(wizard, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }
}
