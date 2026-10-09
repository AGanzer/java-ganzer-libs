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

    private OLSystem olSystem;

    public OLDocument(DocumentCreationInfo<OLDocument> info) throws DVLoadException {
        super(info);
    }

    public OLSystem getOLSystem() {
        return olSystem;
    }

    public void setOLSystem(OLSystem system, View<?> originator) {
        Objects.requireNonNull(system, "system must not be null.");
        addUndoable(new UndoableOLSystem(system, originator));
    }

    public String getOLName() {
        return olSystem.getName();
    }

    public void setOLName(String olName, View<?> originator) {
        if (!olSystem.getName().equals(olName))
            addUndoable(new UndoableOLContext(ChangeContext.NAME, olSystem.getName(), olName, originator));
    }

    public String getOLAxiom() {
        return olSystem.getAxiom();
    }

    public void setOLAxiom(String olAxiom, View<?> originator) {
        if (!olSystem.getAxiom().equals(olAxiom))
            addUndoable(new UndoableOLContext(ChangeContext.AXIOM, olSystem.getAxiom(), olAxiom, originator));
    }

    public int getOLAngle() {
        return olSystem.getAngle();
    }

    public void setOLAngle(int olAngle, View<?> originator) {
        if (olSystem.getAngle() != olAngle)
            addUndoable(new UndoableOLContext(ChangeContext.ANGLE, olSystem.getAngle(), olAngle, originator));
    }

    public int getOLCycles() {
        return olSystem.getPreferredCycles();
    }

    public void setOLCycles(int olCycles, View<?> originator) {
        if (olSystem.getPreferredCycles() != olCycles)
            addUndoable(new UndoableOLContext(ChangeContext.CYCLES, olSystem.getPreferredCycles(), olCycles, originator));
    }

    public String getOLReplacements() {
        return String.join("\n", olSystem.getReplacements());
    }

    public void setOLReplacements(String olReplacements, View<?> originator) {
        if (!getOLReplacements().equals(olReplacements))
            addUndoable(new UndoableOLContext(ChangeContext.REPLACEMENTS, getOLReplacements(), olReplacements, originator));
    }

    @Override
    protected void doCreateData() {
        olSystem = new OLSystem("", "", 0, 1, (String[]) null);
    }

    @Override
    protected void doLoadData() throws IOException {
        // TODO: Load data from file
        doCreateData();
    }

    @Override
    protected void doSaveData() throws IOException {
        // TODO: save data.
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
            super(olSystem, newValue);
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
            olSystem = (OLSystem) getOldValue();

            setModified(isModified() || !olSystem.isPredefined());
            notifyDataChange(null, olSystem);
        }

        public void execute(View<?> originator) {
            olSystem = (OLSystem) getNewValue();

            setModified(isModified() || !olSystem.isPredefined());
            notifyDataChange(originator, olSystem);
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
                case NAME -> olSystem.setName((String) getOldValue());
                case AXIOM -> olSystem.setAxiom((String) getOldValue());
                case ANGLE -> olSystem.setAngle((int) getOldValue());
                case CYCLES -> olSystem.setPreferredCycles((int) getOldValue());
                case REPLACEMENTS -> olSystem.setReplacements(((String) getOldValue()).split("\n"));
            };

            setModified(true);
            notifyDataChange(null, context);
        }

        public void execute(View<?> originator) {
            switch (context) {
                case NAME -> olSystem.setName((String) getNewValue());
                case AXIOM -> olSystem.setAxiom((String) getNewValue());
                case ANGLE -> olSystem.setAngle((int) getNewValue());
                case CYCLES -> olSystem.setPreferredCycles((int) getNewValue());
                case REPLACEMENTS -> olSystem.setReplacements(((String) getNewValue()).split("\n"));
            };

            setModified(true);
            notifyDataChange(originator, context);
        }
    }
}
