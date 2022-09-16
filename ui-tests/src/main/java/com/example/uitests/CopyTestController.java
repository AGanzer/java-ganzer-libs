package com.example.uitests;

import de.ganzer.core.files.FileCopy;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.fx.validation.ValidatorTextFormatter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class CopyTestController implements TestProvider {
    public TextField sourcePath;
    public TextField targetPath;
    public ProgressBar progressBar;
    public Label progressLabel;
    public CheckBox suppressInit;
    public Label bytesToCopyLabel;
    public Label bytesCopiedLabel;
    public Label workedFileLabel;
    public RadioButton copyDirectories;

    @FXML
    private void initialize() {
        new ValidatorTextFormatter(new Validator(), sourcePath);
        new ValidatorTextFormatter(new Validator(), targetPath);
    }

    private static final ButtonType YES_TO_ALL = new ButtonType("Yes To All", ButtonBar.ButtonData.YES);
    private static final ButtonType NO_TO_ALL = new ButtonType("No To All", ButtonBar.ButtonData.NO);
    private static final ButtonType RETRY = new ButtonType("Retry");
    private static final ButtonType IGNORE = new ButtonType("Ignore");
    private static final ButtonType IGNORE_ALL = new ButtonType("Ignore All");
    private static final ButtonType IGNORE_ALL_THE_SAME = new ButtonType("Ignore All The Same");

    private static boolean cancelCopy = false;

    @Override
    public void test() {
        if (validate(sourcePath) && validate(targetPath)) {
            cancelCopy = false;

            new Thread(() -> {
                FileCopy copy = new FileCopy(progress -> {
                    switch (progress.getStatus()) {
                        case INITIALIZING:
                            Platform.runLater(() -> bytesToCopyLabel.setText(String.format("%1$,d Bytes", progress.getTotalBytesAvail())));
                            break;

                        case START_DIRECTORY:
                        case FINISHED_DIRECTORY:
                            break;

                        case START_FILE:
                            Platform.runLater(() -> workedFileLabel.setText(progress.getSourcePath()));
                            break;

                        case FINISHED_FILE:
                        case COPYING_FILE:
                            Platform.runLater(() -> {
                                progressBar.setProgress(progress.getTotalPercentage() / 100);
                                progressLabel.setText(String.format("%1$,.2f %%", progress.getTotalPercentage()));
                                bytesCopiedLabel.setText(String.format("%1$,d Bytes", progress.getTotalBytesCopied()));
                            });

                            break;

                        case FINISHED:
                            Platform.runLater(() -> workedFileLabel.setText(""));
                            break;
                    }

                    synchronized(this) {
                        return cancelCopy
                                ? FileCopy.ProgressContinuation.CANCEL_COPY
                                : FileCopy.ProgressContinuation.CONTINUE_COPY;
                    }
                },
                (error, description, source, target) -> {
                    var result = TestApplication.alert(
                            description,
                            IGNORE,
                            IGNORE_ALL,
                            IGNORE_ALL_THE_SAME,
                            RETRY,
                            ButtonType.CANCEL);

                    if (result.isEmpty() || result.get() == ButtonType.CANCEL)
                        return FileCopy.ErrorAction.ABORT;

                    if (result.get() == IGNORE)
                        return FileCopy.ErrorAction.IGNORE;

                    if (result.get() == IGNORE_ALL)
                        return FileCopy.ErrorAction.IGNORE_ALL;

                    if (result.get() == IGNORE_ALL_THE_SAME)
                        return FileCopy.ErrorAction.IGNORE_ALL_THIS;

                    return FileCopy.ErrorAction.RETRY;
                },
                (source, target) -> {
                    var result = TestApplication.alert(
                            String.format("Overwrite \"%s\"?", target),
                            ButtonType.YES,
                            YES_TO_ALL,
                            ButtonType.NO,
                            NO_TO_ALL);

                    if (result.isEmpty() || result.get() == ButtonType.NO)
                        return FileCopy.OverwriteAction.OVERWRITE_NOT;

                    if (result.get() == ButtonType.YES)
                        return FileCopy.OverwriteAction.OVERWRITE_ONE;

                    if (result.get() == YES_TO_ALL)
                        return FileCopy.OverwriteAction.OVERWRITE_ALL;

                    return FileCopy.OverwriteAction.OVERWRITE_NONE;
                });

                copy.start(sourcePath.getText(), targetPath.getText(), suppressInit.isSelected());
            });
        }
    }

    public void chooseSourceClicked(ActionEvent actionEvent) {
        String path = copyDirectories.isSelected()
                ? chooseDirectory(sourcePath.getText(), "Choose Source Directory")
                : chooseFile(sourcePath.getText());

        if (path != null)
            sourcePath.setText(path);
    }

    public void chooseTargetClicked(ActionEvent actionEvent) {
        String path = chooseDirectory(targetPath.getText(), "Choose Target Directory");

        if (path != null)
            targetPath.setText(path);
    }

    private Window getActiveWindow() {
        List<Window> windows = Stage.getWindows();

        for (Window w: windows)
            if (w.isShowing())
                return w;

        return null;
    }

    private String chooseFile(String initPath) {
        FileChooser dlg = new FileChooser();
        File initFile = initPath.isEmpty()
                ? null
                : new File(initPath).getParentFile();

        dlg.setInitialDirectory(initFile);
        dlg.setTitle("Choose Source File");

        File chosen = dlg.showOpenDialog(getActiveWindow());

        return chosen != null
                ? chosen.getAbsolutePath()
                : null;
    }

    private String chooseDirectory(String initPath, String title) {
        DirectoryChooser dlg = new DirectoryChooser();
        File initFile = initPath.isEmpty()
                ? null
                : new File(initPath).getParentFile();

        dlg.setInitialDirectory(initFile);
        dlg.setTitle(title);

        File chosen = dlg.showDialog(getActiveWindow());

        return chosen != null
                ? chosen.getAbsolutePath()
                : null;
    }

    private boolean validate(TextField field) {
        try {
            ((ValidatorTextFormatter)field.getTextFormatter()).validate();
        } catch (ValidatorException e) {
            TestApplication.alert(e.getMessage());

            field.requestFocus();
            field.selectAll();

            return false;
        }

        return true;
    }

    public void cancelClicked(ActionEvent actionEvent) {
        synchronized(this) {
            cancelCopy = true;
        }
    }
}
