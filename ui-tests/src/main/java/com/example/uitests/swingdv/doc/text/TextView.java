package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.doc.MDISubView;
import com.example.uitests.swingdv.doc.SwingDocumentUndoable;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.dv.DVManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class TextView extends MDISubView<TextDocument> {
    private final AtomicBoolean updating = new AtomicBoolean(false);
    private final JTextArea editor;
    private final JPopupMenu popupMenu;

    public TextView(ViewCreationInfo<TextDocument, TextView> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);

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
                if (!updating.get())
                    getDocument().setText(editor.getText(), TextView.this);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!updating.get())
                    getDocument().setText(editor.getText(), TextView.this);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!updating.get())
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

        var hexItem = new JMenuItem("Open in Hex-Editor");
        hexItem.addActionListener(e -> {
            var tpl = getDocument().getTemplate().getViewTemplates().stream()
                    .filter(t -> t.getDisplayName().equals("Hex-Editor"))
                    .findFirst()
                    .orElse(null);
            DVManager.createView(getDocument(), tpl);
        });

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
        popupMenu.add(new JSeparator());
        popupMenu.add(hexItem);

        SwingUtilities.invokeLater(editor::requestFocus);
    }

    @Override
    public void documentDataChanged(Object context) {
        updating.set(true);

        try {
            var selStart = editor.getSelectionStart();
            var selEnd = editor.getSelectionEnd();

            editor.setText(getDocument().getText());
            editor.select(selStart, selEnd);
        } finally {
            updating.set(false);
        }
    }

    @Override
    public void updateTitle() {
        super.updateTitle();
        editor.setEditable(!getDocument().isReadOnly());
    }

    @Override
    protected void focusContent() {
        SwingUtilities.invokeLater(editor::requestFocusInWindow);
    }
}
