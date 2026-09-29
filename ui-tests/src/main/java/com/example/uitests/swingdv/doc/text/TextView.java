package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.Actions;
import com.example.uitests.swingdv.doc.SwingDocumentUndoable;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.dv.ViewTemplate;
import de.ganzer.swing.controls.ClosableTabsPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextView extends JPanel implements View<TextDocument> {
    private final ViewTemplate<TextDocument, TextView> template;
    private final TextDocument document;
    private final ClosableTabsPane tabPane;
    private final JTextArea editor;
    private final JPopupMenu popupMenu;

    public TextView(ViewCreationInfo<TextDocument, TextView> info, ClosableTabsPane tabPane) {
        super(new BorderLayout());

        this.template = info.getTemplate();
        this.document = info.getDocument();
        this.tabPane = tabPane;

        editor = new JTextArea();
        editor.setWrapStyleWord(true);
        editor.setLineWrap(true);
        editor.getDocument().addUndoableEditListener(
                e -> document.addUndoable(new SwingDocumentUndoable(e.getEdit())));
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                document.setText(editor.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                document.setText(editor.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                document.setText(editor.getText());
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
        popupMenu.add(Actions.undoAction.createMenuItem());
        popupMenu.add(Actions.redoAction.createMenuItem());
        popupMenu.add(new JSeparator());
        popupMenu.add(Actions.cutAction.createMenuItem());
        popupMenu.add(Actions.copyAction.createMenuItem());
        popupMenu.add(Actions.pasteAction.createMenuItem());
        popupMenu.add(Actions.deleteAction.createMenuItem());
        popupMenu.add(new JSeparator());
        popupMenu.add(Actions.saveAction.createMenuItem());
        popupMenu.add(Actions.saveAsAction.createMenuItem());

        SwingUtilities.invokeLater(editor::requestFocus);
    }

    @Override
    public ViewTemplate<TextDocument, ? extends View<TextDocument>> getTemplate() {
        return template;
    }

    @Override
    public TextDocument getDocument() {
        return document;
    }

    @Override
    public void toFront() {
        tabPane.setSelectedComponent(this);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    @Override
    public void forceClose() {
        tabPane.remove(this);
    }
}
