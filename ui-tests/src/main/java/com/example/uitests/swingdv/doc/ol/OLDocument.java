package com.example.uitests.swingdv.doc.ol;

import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;
import de.ganzer.dv.View;

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
        olName = system.getName();
        olAxiom = system.getAxiom();
        olAngle = system.getAngle();
        olCycles = system.getPreferredCycles();
        olReplacements = String.join("\n", system.getReplacementsList());

        if (system.isPredefined())
            setModified(false);

        notifyDataChange(originator, system);
    }

    public String getOLName() {
        return olName;
    }

    public void setOLName(String olName, View<?> originator) {
        Objects.requireNonNull(olName, "olName must not be null.");

        if (this.olName.equals(olName))
            return;

        this.olName = olName;

        setModified(true);
        notifyDataChange(originator, ChangeContext.NAME);
    }

    public String getOLAxiom() {
        return olAxiom;
    }

    public void setOLAxiom(String olAxiom, View<?> originator) {
        Objects.requireNonNull(olAxiom, "olAxiom must not be null.");

        if (this.olAxiom.equals(olAxiom))
            return;

        this.olAxiom = olAxiom;

        setModified(true);
        notifyDataChange(originator, ChangeContext.AXIOM);
    }

    public int getOLAngle() {
        return olAngle;
    }

    public void setOLAngle(int olAngle, View<?> originator) {
        if (this.olAngle == olAngle)
            return;

        this.olAngle = olAngle;

        setModified(true);
        notifyDataChange(originator, ChangeContext.ANGLE);
    }

    public int getOLCycles() {
        return olCycles;
    }

    public void setOLCycles(int olCycles, View<?> originator) {
        if (this.olCycles == olCycles)
            return;

        this.olCycles = olCycles;

        setModified(true);
        notifyDataChange(originator, ChangeContext.CYCLES);
    }

    public String getOLReplacements() {
        return olReplacements;
    }

    public void setOLReplacements(String olReplacements, View<?> originator) {
        Objects.requireNonNull(olReplacements, "olReplacements must not be null.");

        if (this.olReplacements.equals(olReplacements))
            return;

        this.olReplacements = olReplacements;

        setModified(true);
        notifyDataChange(originator, ChangeContext.REPLACEMENTS);
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
}
