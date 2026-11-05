package com.example.uitests.swing.tests.wizard;

import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.controls.DefaultWizardModel;
import de.ganzer.swing.controls.WizardPage;
import de.ganzer.swing.controls.WizardPane;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

public class ExtendedWizardDialog extends JDialog {
    private boolean finished;

    public static boolean showModel(Window owner, WizardData data) {
        var dialog = new ExtendedWizardDialog(owner, data);
        dialog.setVisible(true);

        return dialog.finished;
    }

    public ExtendedWizardDialog(Window owner, WizardData data) {
        super(owner, "Extended Wizard Test", ModalityType.APPLICATION_MODAL);

        var wizard = new WizardPane(new Model(data));
        wizard.setBorder(new EmptyBorder(10, 10, 10, 10));

        var model = (Model) wizard.getModel();
        model.addPage(Model.WELCOME, new WelcomePage());
        model.addPage(Model.NAME, new NamePage(data));
        model.addPage(Model.PRIVATE, new PrivatePage(data));
        model.addPage(Model.EMPLOYEE, new EmployeePage(data));
        model.addPage(Model.SUMMARY, new SummaryPage(data));

        var prevButton = wizard.getPrevAction().createButton();
        var nextButton = wizard.getNextAction().createButton();
        var finishButton = wizard.getFinishAction().onAction(e -> {
            finished = true;
            setVisible(false);
        }).createButton();
        var cancelButton = new GAction("Cancel").onAction(e -> setVisible(false)).createButton();

        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(finishButton);
        buttonPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(wizard, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private static class Model extends DefaultWizardModel {
        public static final int WELCOME = 0;
        public static final int NAME = 1;
        public static final int PRIVATE = 2;
        public static final int EMPLOYEE = 3;
        public static final int SUMMARY = 4;

        private final WizardData data;

        private Model(WizardData data) {
            this.data = data;
        }

        public void addPage(Object pageId, WizardPage page) {
            super.addPage(pageId, page);
        }

        @Override
        public Object getNextPage(Object pageId) {
            return switch (Integer.parseInt(pageId.toString())) {
                case WELCOME -> NAME;
                case NAME -> data.role == Role.PRIVATE ? PRIVATE : EMPLOYEE;
                case PRIVATE, EMPLOYEE -> SUMMARY;
                default -> null;
            };
        }
    }
}
