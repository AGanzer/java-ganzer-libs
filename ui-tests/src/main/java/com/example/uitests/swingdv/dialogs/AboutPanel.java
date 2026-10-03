package com.example.uitests.swingdv.dialogs;

import com.example.uitests.swing.SVGProvider;
import com.example.uitests.swingdv.SwingDVApp;
import de.ganzer.swing.dlgfw.AbstractPanel;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class AboutPanel extends AbstractPanel {
    @Override
    protected JComponent createCenterPanel() {
        var text = String.format("Version: %s", SwingDVApp.VERSION);

        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10  ));

        return label;
    }

    @Override
    protected JComponent createTitleLabel() {
        return new JLabel("About " + SwingDVApp.TITLE);
    }

    @Override
    protected JComponent createTitleIcon() {
        return new JLabel(SVGProvider.get("about", DEFAULT_TITLE_ICON_SIZE));
    }
}
