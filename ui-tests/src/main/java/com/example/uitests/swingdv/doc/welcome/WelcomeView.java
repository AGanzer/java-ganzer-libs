package com.example.uitests.swingdv.doc.welcome;

import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.dv.ViewTemplate;
import de.ganzer.swing.controls.ClosableTabsPane;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;

public class WelcomeView extends JPanel implements View<WelcomeDocument> {
    private final ViewTemplate<WelcomeDocument, WelcomeView> template;
    private final WelcomeDocument document;
    private final ClosableTabsPane tabPane;

    public WelcomeView(ViewCreationInfo<WelcomeDocument, WelcomeView> info, ClosableTabsPane tabPane) {
        super(new BorderLayout());

        this.template = info.getTemplate();
        this.document = info.getDocument();
        this.tabPane = tabPane;

        JLabel welcomeLabel = new JLabel(document.getWelcomeText());

        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 40));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);

        add(welcomeLabel, BorderLayout.CENTER);
    }

    @Override
    public ViewTemplate<WelcomeDocument, ? extends View<WelcomeDocument>> getTemplate() {
        return template;
    }

    @Override
    public WelcomeDocument getDocument() {
        return document;
    }

    @Override
    public void toFront() {
        tabPane.setSelectedComponent(this);
    }

    @Override
    public void forceClose() {
        tabPane.remove(this);
    }
}
