package com.example.uitests.swingdv.doc.ol;

import de.ganzer.dv.*;

import java.io.IOException;
import java.util.Objects;

public class OLDocument extends Document {
    public enum ChangeContext {
        NAME,
        AXIOM,
        ANGLE,
        CYCLES,
        REPLACEMENTS
    }

    private String olName;
    private String olAxiom;
    private int olAngle;
    private int olCycles;
    private String olReplacements;

    public OLDocument(DocumentCreationInfo<OLDocument> info) throws DVLoadException {
        super(info);
    }

    public void setOLSystem(OLSystem system, View<?> originator) {
        Objects.requireNonNull(system, "system must not be null.");
        addUndoable(new UndoableOLSystem(system, originator));
    }

    public String getOLName() {
        return olName;
    }

    public void setOLName(String olName, View<?> originator) {
        Objects.requireNonNull(olName, "olName must not be null.");

        if (!this.olName.equals(olName))
            addUndoable(new UndoableOLContext(ChangeContext.NAME, this.olName, olName, originator));
    }

    public String getOLAxiom() {
        return olAxiom;
    }

    public void setOLAxiom(String olAxiom, View<?> originator) {
        Objects.requireNonNull(olAxiom, "olAxiom must not be null.");

        if (!this.olAxiom.equals(olAxiom))
            addUndoable(new UndoableOLContext(ChangeContext.AXIOM, this.olAxiom, olAxiom, originator));
    }

    public int getOLAngle() {
        return olAngle;
    }

    public void setOLAngle(int olAngle, View<?> originator) {
        if (this.olAngle != olAngle)
            addUndoable(new UndoableOLContext(ChangeContext.ANGLE, this.olAngle, olAngle, originator));
    }

    public int getOLCycles() {
        return olCycles;
    }

    public void setOLCycles(int olCycles, View<?> originator) {
        if (this.olCycles != olCycles)
            addUndoable(new UndoableOLContext(ChangeContext.CYCLES, this.olCycles, olCycles, originator));
    }

    public String getOLReplacements() {
        return olReplacements;
    }

    public void setOLReplacements(String olReplacements, View<?> originator) {
        Objects.requireNonNull(olReplacements, "olReplacements must not be null.");

        if (!this.olReplacements.equals(olReplacements))
            addUndoable(new UndoableOLContext(ChangeContext.REPLACEMENTS, this.olReplacements, olReplacements, originator));
    }

    @Override
    protected void doCreateData() {
        olName = "";
        olAxiom = "";
        olAngle = 60;
        olCycles = 2;
        olReplacements = "";
    }

    @Override
    protected void doLoadData() throws IOException {
        // TODO: Load data from file
        doCreateData();
    }

    @Override
    protected void doSaveData() throws IOException {
    }

    private static abstract class AbstractUndoableOL implements Undoable {
        private final Object oldValue;
        private final Object newValue;

        public AbstractUndoableOL(Object oldValue, Object newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public Object getOldValue() {
            return oldValue;
        }

        public Object getNewValue() {
            return newValue;
        }
    }

    private class UndoableOLSystem extends AbstractUndoableOL {
        public UndoableOLSystem(Object newValue, View<?> originator) {
            super(new OLSystem(olName, olAxiom, olAngle, olCycles, olReplacements), newValue);
            execute(originator);
        }

        @Override
        public String getTitle() {
            return "Change OL-System";
        }

        @Override
        public void execute() {
            execute(null);
        }

        @Override
        public void undo() {
            var system = (OLSystem) getOldValue();

            olName = system.getName();
            olAxiom = system.getAxiom();
            olAngle = system.getAngle();
            olCycles = system.getPreferredCycles();
            olReplacements = String.join("\n", system.getReplacementsList());

            setModified(!system.isPredefined());
            notifyDataChange(null, system);
        }

        public void execute(View<?> originator) {
            var system = (OLSystem) getNewValue();

            olName = system.getName();
            olAxiom = system.getAxiom();
            olAngle = system.getAngle();
            olCycles = system.getPreferredCycles();
            olReplacements = String.join("\n", system.getReplacementsList());

            setModified(!system.isPredefined());
            notifyDataChange(originator, system);
        }
    }

    private class UndoableOLContext extends AbstractUndoableOL {
        private final ChangeContext context;

        public UndoableOLContext(ChangeContext context, Object oldValue, Object newValue, View<?> originator) {
            super(oldValue, newValue);
            this.context = context;
            execute(originator);
        }

        @Override
        public String getTitle() {
            return switch (context) {
                case NAME -> "Change OL Name";
                case AXIOM -> "Change OL Axiom";
                case ANGLE -> "Change OL Angle";
                case CYCLES -> "Change OL Cycles";
                case REPLACEMENTS -> "Change OL Replacements";
            };
        }

        @Override
        public void execute() {
            execute(null);
        }

        @Override
        public void undo() {
            switch (context) {
                case NAME -> olName = (String) getOldValue();
                case AXIOM -> olAxiom = (String) getOldValue();
                case ANGLE -> olAngle = (int) getOldValue();
                case CYCLES -> olCycles = (int) getOldValue();
                case REPLACEMENTS -> olReplacements = (String) getOldValue();
            };

            setModified(true);
            notifyDataChange(null, context);
        }

        public void execute(View<?> originator) {
            switch (context) {
                case NAME -> olName = (String) getNewValue();
                case AXIOM -> olAxiom = (String) getNewValue();
                case ANGLE -> olAngle = (int) getNewValue();
                case CYCLES -> olCycles = (int) getNewValue();
                case REPLACEMENTS -> olReplacements = (String) getNewValue();
            };

            setModified(true);
            notifyDataChange(originator, context);
        }
    }
}
