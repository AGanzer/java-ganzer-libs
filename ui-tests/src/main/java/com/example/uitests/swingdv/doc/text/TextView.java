package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.doc.MDISubView;
import com.example.uitests.swingdv.doc.SwingDocumentUndoable;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.dv.swing.DVManager;
import de.ganzer.swing.controls.ClosableTabsPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextView extends MDISubView<TextDocument> {
    private final JTextArea editor;
    private final JPopupMenu popupMenu;

    public TextView(ViewCreationInfo<TextDocument, TextView> info, ClosableTabsPane tabPane) {
        super(info, tabPane);

        editor = new JTextArea();
        editor.setWrapStyleWord(true);
        editor.setLineWrap(true);
        editor.setText(getDocument().getText());
        editor.setEditable(!getDocument().isReadOnly());
        editor.getDocument().addUndoableEditListener(
                e -> getDocument().addUndoable(new SwingDocumentUndoable(e.getEdit())));
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                getDocument().setText(editor.getText(), TextView.this);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                getDocument().setText(editor.getText(), TextView.this);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                getDocument().setText(editor.getText(), TextView.this);
            }
        });
        editor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 || e.isPopupTrigger())
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                else
                    super.mouseClicked(e);
            }
        });

        JScrollPane scrollPane = new JScrollPane(editor);
        add(scrollPane, BorderLayout.CENTER);

        popupMenu = new JPopupMenu();
        popupMenu.add(DVManager.undoAction.createMenuItem());
        popupMenu.add(DVManager.redoAction.createMenuItem());
        popupMenu.add(new JSeparator());
        popupMenu.add(DVManager.cutAction.createMenuItem());
        popupMenu.add(DVManager.copyAction.createMenuItem());
        popupMenu.add(DVManager.pasteAction.createMenuItem());
        popupMenu.add(DVManager.deleteAction.createMenuItem());
        popupMenu.add(new JSeparator());
        popupMenu.add(DVManager.saveAction.createMenuItem());
        popupMenu.add(DVManager.saveAsAction.createMenuItem());

        SwingUtilities.invokeLater(editor::requestFocus);
    }

    @Override
    public void documentDataChanged(Object context) {
        var selStart = editor.getSelectionStart();
        var selEnd = editor.getSelectionEnd();

        editor.setText(getDocument().getText());

        editor.select(selStart, selEnd);
    }

    @Override
    public void updateTitle() {
        super.updateTitle();
        editor.setEditable(!getDocument().isReadOnly());
    }

    @Override
    public void toFront() {
        super.toFront();
        SwingUtilities.invokeLater(editor::requestFocusInWindow);
    }
}
