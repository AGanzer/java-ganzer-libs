package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.controls.HexEditor;

import java.awt.BorderLayout;

public class HexView extends MDISubView<TextDocument> {
    private final HexEditor hexEditor;

    public HexView(ViewCreationInfo<TextDocument, ? extends View<TextDocument>> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);

        hexEditor = new HexEditor();
        add(hexEditor, BorderLayout.CENTER);

        hexEditor.setText(getDocument().getText());
    }

    @Override
    public void documentDataChanged(Object context) {
        hexEditor.setText(getDocument().getText());
    }
}
