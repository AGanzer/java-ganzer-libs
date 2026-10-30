package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.controls.HexEditor;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class HexView extends MDISubView<TextDocument> {
    private final HexEditor editor;

    public HexView(ViewCreationInfo<TextDocument, ? extends View<TextDocument>> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);

        editor = new HexEditor();
        editor.setDocument(getDocument().getDocument());

        add(editor, BorderLayout.CENTER);
    }

    @Override
    public void documentDataChanged(Object context) {
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
