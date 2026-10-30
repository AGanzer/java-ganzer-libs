package com.example.uitests.swingdv.doc.welcome;

import com.example.uitests.swingdv.SwingDVApp;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;

public class WelcomeDocument extends Document {
    public WelcomeDocument(DocumentCreationInfo<WelcomeDocument> info) throws DVLoadException {
        super(info);
    }

    public String getWelcomeText() {
        return "Welcome to\n" + SwingDVApp.TITLE;
    }

    @Override
    protected void doCreateData() {
    }

    @Override
    protected void doLoadData() {
    }

    @Override
    protected void doSaveData() {
    }

    @Override
    public boolean isSaveAsSupported() {
        return false;
    }
}
