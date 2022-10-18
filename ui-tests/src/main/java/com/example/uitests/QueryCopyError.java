package com.example.uitests;

import de.ganzer.core.files.CopyErrorAction;
import de.ganzer.core.files.FileCopy;
import de.ganzer.core.files.FileError;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;

import java.io.File;

public class QueryCopyError implements FileCopy.QueryErrorAction {
    private static final ButtonType RETRY = new ButtonType("Retry");
    private static final ButtonType IGNORE = new ButtonType("Ignore");
    private static final ButtonType IGNORE_ALL = new ButtonType("Ignore All");
    private static final ButtonType IGNORE_ALL_THE_SAME = new ButtonType("Ignore All The Same");
    private ButtonType result;

    @Override
    public synchronized CopyErrorAction query(FileError error, String errorDescription, File source, File target) {
        result = ButtonType.CANCEL;

        Platform.runLater(() -> alert(errorDescription));

        try {
            wait();
        } catch (InterruptedException e) {
            // Ignore.
        }

        if (result == null || result == ButtonType.CANCEL)
            return CopyErrorAction.ABORT;

        if (result == IGNORE)
            return CopyErrorAction.IGNORE;

        if (result == IGNORE_ALL)
            return CopyErrorAction.IGNORE_ALL;

        if (result == IGNORE_ALL_THE_SAME)
            return CopyErrorAction.IGNORE_ALL_THIS;

        return CopyErrorAction.RETRY;
    }

    private synchronized void alert(String errorDescription) {
        result = TestApplication.alert(
                errorDescription,
                IGNORE,
                IGNORE_ALL,
                IGNORE_ALL_THE_SAME,
                RETRY,
                ButtonType.CANCEL).orElse(ButtonType.CANCEL);

        notify();
    }
}
