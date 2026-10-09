package com.example.uitests.swingdv.doc.ol;

import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;

import java.awt.BorderLayout;

public class OLView extends MDISubView<OLDocument> {
    private final OLEditor editor;
    private final OLCanvas canvas;

    public OLView(ViewCreationInfo<OLDocument, OLView> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);

        editor = new OLEditor(this);
        add(editor, BorderLayout.WEST);

        canvas = new OLCanvas();
        add(canvas, BorderLayout.CENTER);
    }

    @Override
    public void documentDataChanged(Object context) {
        if (context instanceof OLDocument.ChangeContext ctx) {
            updateEditor(ctx);
        } else if (context instanceof OLSystem sys) {
            editor.setOLSystem(sys);
        }
    }

    private void updateEditor(OLDocument.ChangeContext ctx) {
        switch (ctx) {
            case NAME -> editor.setOLName(getDocument().getOLName());
            case AXIOM -> editor.setOLAxiom(getDocument().getOLAxiom());
            case ANGLE -> editor.setOLAngle(getDocument().getOLAngle());
            case CYCLES -> editor.setOLCycles(getDocument().getOLCycles());
            case REPLACEMENTS -> editor.setOLReplacements(getDocument().getOLReplacements());
        }
    }
}
