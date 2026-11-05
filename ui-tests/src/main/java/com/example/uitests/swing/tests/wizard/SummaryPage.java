package com.example.uitests.swing.tests.wizard;

import de.ganzer.swing.controls.WizardPage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.text.NumberFormat;

public class SummaryPage extends JPanel implements WizardPage {
    private final WizardData data;
    private final JLabel summaryLabel;

    public SummaryPage(WizardData data) {
        super(new BorderLayout());
        this.data = data;
        this.summaryLabel = new JLabel();
        add(summaryLabel, BorderLayout.CENTER);
        updatePageControls();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void updatePageControls() {
        var formatter = NumberFormat.getCurrencyInstance();
        var skeleton = String.format("<html>" +
                                             "<table>" +
                                             "<tr><td>Forename:</td><td>%s</td></tr><tr>" +
                                             "<td>Surname:</td><td>%s</td></tr>" +
                                             "<tr><td>Birthday:</td><td>%s</td></tr>" +
                                             "<tr><td>Role:</td><td>%s</td></tr>" +
                                             "%%s" +
                                             "</table>" +
                                             "</html>",
                                     data.forename,
                                     data.surname,
                                     data.birthday,
                                     data.role);
        var additions = data.role == Role.PRIVATE
                ? String.format("<tr><td>Phone:</td><td>%s</td></tr><tr>" +
                                        "<td>Email:</td><td>%s</td></tr>",
                                data.privatePhone,
                                data.privateEmail)
                : String.format("<tr><td>Phone:</td><td>%s</td></tr><tr>" +
                                        "<td>Email:</td><td>%s</td></tr>" +
                                        "<tr><td>Salary:</td><td>%s</td></tr>",
                                data.companyPhone,
                                data.companyEmail,
                                formatter.format(data.salary));

        summaryLabel.setText(String.format(skeleton, additions));
    }

    @Override
    public void updateWizardData() {
    }

    @Override
    public boolean validateInput() {
        return true;
    }
}
