package com.example.uitests.swingdv.dialogs;

import de.ganzer.swing.dlgfw.AbstractDialog;

import javax.swing.JPanel;
import java.awt.Window;

public class AboutDialog extends AbstractDialog {
    public AboutDialog(Window owner) {
        super(owner);
        pack();
        setLocationRelativeTo(owner);
    }

    @Override
    protected JPanel createCenterPanel() {
        return new AboutPanel();
    }
}
