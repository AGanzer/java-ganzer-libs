package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;

public class CSVView extends MDISubView<CSVDocument> {
    public CSVView(ViewCreationInfo<CSVDocument, CSVView> info, ClosableTabsPane tabPane) {
        super(info, tabPane);
    }

    @Override
    public void documentDataChanged(Object context) {
    }

    @Override
    public void toFront() {
    }
}
