package com.example.uitests.swingdv.dialogs;

import de.ganzer.swing.dlgfw.AbstractModifiableDataDialog;

import javax.swing.JPanel;
import java.awt.Window;

public class ChooseFromListDialog<E> extends AbstractModifiableDataDialog<ChooseFromListData<E>> {
    public ChooseFromListDialog(Window owner, ChooseFromListData<E> data) {
        super(owner, data);
        setSize(300, 300);
        setLocationRelativeTo(owner);
    }

    @Override
    protected JPanel createCenterPanel() {
        return new ChooseFromListPanel<E>(this);
    }
}
