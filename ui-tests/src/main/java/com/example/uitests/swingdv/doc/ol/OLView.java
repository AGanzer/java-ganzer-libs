package com.example.uitests.swingdv.doc.ol;

import com.example.uitests.swingdv.LocalSettings;
import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;

import javax.swing.JSplitPane;
import java.awt.BorderLayout;

public class OLView extends MDISubView<OLDocument> {
    private final OLEditor editor;
    private final OLCanvas canvas;

    public OLView(ViewCreationInfo<OLDocument, OLView> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);

        editor = new OLEditor(this);
        canvas = new OLCanvas();

        var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editor, canvas);
        splitPane.setDividerLocation(LocalSettings.ui.read(getClass().getSimpleName() + "splitter", 200));
        splitPane.setOneTouchExpandable(false);
        splitPane.setResizeWeight(0);

        add(splitPane, BorderLayout.CENTER);
    }

    public void generate(int numCycles) {
        if (!getDocument().getOLSystem().isPredefined() && !editor.isInputValid())
            return;

        var figure = getDocument().getOLSystem().createFigure(numCycles);
        canvas.setTurnAngle(-getDocument().getOLAngle());
        canvas.setMovements(figure);

        getDocument().getViews().stream().filter(v -> v != this).forEach(v -> v.documentDataChanged(null));
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
