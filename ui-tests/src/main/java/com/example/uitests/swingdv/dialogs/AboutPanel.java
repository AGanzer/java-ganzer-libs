package com.example.uitests.swingdv.dialogs;

import com.example.uitests.swing.SVGProvider;
import com.example.uitests.swingdv.SwingDVApp;
import de.ganzer.swing.dlgfw.AbstractPanel;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.Locale;

public class AboutPanel extends AbstractPanel {
    @Override
    protected JComponent createCenterPanel() {
        var text = String.format("<html>" +
                                         "<b>&nbsp;&nbsp;Version: %s</b><br><br>" +
                                         "<table>" +
                                         "  <tr><td>Java Version:</td><td>%s %s, %s</td></tr>" +
                                         "  <tr><td>Java VM Info:</td><td>%s</td></tr>" +
//                                         "  <tr><td>Java Home:</td><td>%s</td></tr>" +
                                         "  <tr><td>Locale:</td><td>%s</td></tr>" +
                                         "</table>" +
                                         "</html>",
                                 SwingDVApp.VERSION,
                                 System.getProperty("java.vendor"), System.getProperty("java.vm.version"), System.getProperty("java.version.date"),
                                 System.getProperty("java.vm.info"),
//                                 System.getProperty("java.home"),
                                 Locale.getDefault().getDisplayName());

        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(10, 7, 10, 10  ));

        return label;
    }

    @Override
    protected JComponent createTitleLabel() {
        return new JLabel(SwingDVApp.TITLE);
    }

    @Override
    protected JComponent createTitleIcon() {
        return new JLabel(SVGProvider.get("hamburger", DEFAULT_TITLE_ICON_SIZE));
    }
}
