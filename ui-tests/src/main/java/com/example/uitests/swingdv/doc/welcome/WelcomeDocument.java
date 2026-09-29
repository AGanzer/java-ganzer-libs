package com.example.uitests.swingdv.doc.welcome;

import com.example.uitests.swingdv.SwingDVApp;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;

import java.io.IOException;

public class WelcomeDocument extends Document {
    public WelcomeDocument(DocumentCreationInfo<WelcomeDocument> info) throws DVLoadException {
        super(info);
    }

    public String getWelcomeText() {
        return "Welcome to " + SwingDVApp.TITLE;
    }

    @Override
    protected void doCreateData() {
    }

    @Override
    protected void doLoadData() throws RuntimeException {
    }

    @Override
    protected void doSaveData() throws RuntimeException {
    }

    @Override
    public boolean isSaveAsSupported() {
        return false;
    }
}
