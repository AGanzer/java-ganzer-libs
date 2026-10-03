package com.example.uitests.swingdv.dialogs;

import de.ganzer.swing.dlgfw.AbstractDataDialog;

import javax.swing.JPanel;
import java.awt.Window;
import java.util.List;

public class ChooseFromListDialog extends AbstractDataDialog<List<?>> {
    public ChooseFromListDialog(Window owner, String title, List<?> data) {
        super(owner, title, data);
    }

    @Override
    protected JPanel createCenterPanel() {
        return null;
    }
}
