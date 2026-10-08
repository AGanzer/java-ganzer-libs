package com.example.uitests.swingdv.doc.ol;

import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;

import java.io.IOException;

public class OLDocument extends Document {
    public OLDocument(DocumentCreationInfo<OLDocument> info) throws DVLoadException {
        super(info);
    }

    @Override
    protected void doCreateData() {

    }

    @Override
    protected void doLoadData() throws IOException {
    }

    @Override
    protected void doSaveData() throws IOException {
    }
}
