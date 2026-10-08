package com.example.uitests.swingdv.doc.ol;

import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;

public class OLView extends MDISubView<OLDocument> {
    public OLView(ViewCreationInfo<OLDocument, OLView> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);
    }

    @Override
    public void documentDataChanged(Object context) {
    }
}
