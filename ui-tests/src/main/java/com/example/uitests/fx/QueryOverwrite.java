package com.example.uitests.fx;

import de.ganzer.core.files.FileCopy;
import de.ganzer.core.files.OverwriteAction;
import javafx.application.Platform;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.io.File;

public class QueryOverwrite implements FileCopy.QueryOverwriteAction {
    private static final ButtonType YES_TO_ALL = new ButtonType("Yes To All", ButtonBar.ButtonData.YES);
    private static final ButtonType NO_TO_ALL = new ButtonType("No To All", ButtonBar.ButtonData.NO);
    private ButtonType result;

    @Override
    public synchronized OverwriteAction query(File source, File target) {
        result = NO_TO_ALL;

        Platform.runLater(() -> alert(target));

        try {
            wait();
        } catch (InterruptedException e) {
            // Ignore.
        }

        if (result == null || result == ButtonType.NO)
            return OverwriteAction.NOT;

        if (result == ButtonType.YES)
            return OverwriteAction.ONE;

        if (result == YES_TO_ALL)
            return OverwriteAction.ALL;

        return OverwriteAction.NONE;
    }

    private synchronized void alert(File target) {
        result = FxTestApp.alertConfirm(
                String.format("Overwrite \"%s\"?", target),
                ButtonType.YES,
                YES_TO_ALL,
                ButtonType.NO,
                NO_TO_ALL).orElse(NO_TO_ALL);

        notify();
    }
}
