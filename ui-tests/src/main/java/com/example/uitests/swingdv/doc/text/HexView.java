package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.controls.HexEditor;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.util.concurrent.atomic.AtomicBoolean;

public class HexView extends MDISubView<TextDocument> {
    private final AtomicBoolean updating = new AtomicBoolean(false);
    private final HexEditor editor;

    public HexView(ViewCreationInfo<TextDocument, ? extends View<TextDocument>> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);

        editor = new HexEditor();
        editor.setText(getDocument().getText());
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!updating.get())
                    getDocument().setText(editor.getText(), HexView.this);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!updating.get())
                    getDocument().setText(editor.getText(), HexView.this);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!updating.get())
                    getDocument().setText(editor.getText(), HexView.this);
            }
        });

        add(editor, BorderLayout.CENTER);
    }

    @Override
    public void documentDataChanged(Object context) {
        updating.set(true);

        try {
            var pos = editor.getCaretPosition();
            editor.setText(getDocument().getText());
            editor.setCaretPosition(pos);
        } finally {
            updating.set(false);
        }
    }
}
