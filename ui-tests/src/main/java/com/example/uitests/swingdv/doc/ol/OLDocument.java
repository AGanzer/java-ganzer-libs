package com.example.uitests.swingdv.doc.ol;

import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OLDocument extends Document {
    private String olName;
    private String olAxiom;
    private int olAngle;
    private int olCycles;
    private List<String> olReplacements;

    public OLDocument(DocumentCreationInfo<OLDocument> info) throws DVLoadException {
        super(info);
    }

    public String getOLName() {
        return olName;
    }

    public void setOLName(String olName) {
        this.olName = olName;
        setModified(true);
    }

    public String getOLAxiom() {
        return olAxiom;
    }

    public void setOLAxiom(String olAxiom) {
        this.olAxiom = olAxiom;
        setModified(true);
    }

    public int getOLAngle() {
        return olAngle;
    }

    public void setOLAngle(int olAngle) {
        this.olAngle = olAngle;
        setModified(true);
    }

    public int getOLCycles() {
        return olCycles;
    }

    public void setOLCycles(int olCycles) {
        this.olCycles = olCycles;
        setModified(true);
    }

    public List<String> getOLReplacements() {
        return Collections.unmodifiableList(olReplacements);
    }

    public void setOLReplacements(List<String> olReplacements) {
        this.olReplacements = olReplacements;
        setModified(true);
    }

    @Override
    protected void doCreateData() {
        olName = "";
        olAxiom = "";
        olAngle = 60;
        olCycles = 2;
        olReplacements = new ArrayList<>();
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
