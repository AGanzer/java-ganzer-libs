package com.example.uitests.swingdv.doc.welcome;

import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;

public class WelcomeView extends MDISubView<WelcomeDocument> {
    public WelcomeView(ViewCreationInfo<WelcomeDocument, WelcomeView> info, ClosableTabsPane tabPane) {
        super(info, tabPane, false);

        JLabel welcomeLabel = new JLabel(getDocument().getWelcomeText());

        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 40));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);

        add(welcomeLabel, BorderLayout.CENTER);
    }

    @Override
    public void documentDataChanged(Object context) {
    }
}
