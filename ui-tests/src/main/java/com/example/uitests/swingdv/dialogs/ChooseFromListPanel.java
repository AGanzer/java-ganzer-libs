package com.example.uitests.swingdv.dialogs;

import de.ganzer.swing.dialogs.ModifiableDataSupport;
import de.ganzer.swing.dlgfw.AbstractModifiableDataPanel;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChooseFromListPanel<E> extends AbstractModifiableDataPanel<ChooseFromListData<E>> {
    private JList<E> list;
    private JLabel titleLabel;

    public ChooseFromListPanel(ModifiableDataSupport<ChooseFromListData<E>> owner) {
        super(owner);
    }

    @Override
    protected JComponent createCenterPanel() {
        list = new JList<>();
        JScrollPane scrollPane = new JScrollPane(list);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0  ));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    @Override
    protected JComponent createTitleLabel() {
        return titleLabel = new JLabel();
    }

    @Override
    protected JComponent createTitleIcon() {
        return null;
    }

    @Override
    public void initControls(ChooseFromListData<E> data) {
        titleLabel.setText(data.title);

        DefaultListModel<E> model = new DefaultListModel<>();
        data.items.forEach(model::addElement);

        list.setModel(model);
        list.addListSelectionListener(e -> getOwner().setDataModified(true));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
                    fireQueryClose(true);
            }
        });
    }

    @Override
    public boolean validateInput() {
        return true;
    }

    @Override
    public void updateData(ChooseFromListData<E> data) {
        data.chosen = list.getSelectedValue();
    }
}
