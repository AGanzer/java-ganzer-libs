package com.example.uitests;

import de.ganzer.core.files.FileCopy;
import de.ganzer.core.files.FileError;
import de.ganzer.core.files.ProgressContinuation;
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

    private static boolean cancelCopy = false;
    private static boolean running = false;

    @Override
    public void test() {
        if (validate(sourcePath) && validate(targetPath)) {
            cancelCopy = false;
            bytesCopiedLabel.setText("");

            new Thread(() -> {
                synchronized (this) {
                    if (running)
                        return;

                    running = true;
                }

                FileCopy copy = new FileCopy(
                        progress -> {
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

                            synchronized (this) {
                                return cancelCopy
                                        ? ProgressContinuation.CANCEL
                                        : ProgressContinuation.CONTINUE;
                            }
                        },
                        new QueryError(),
                        new QueryOverwrite());

                copy.start(sourcePath.getText(), targetPath.getText(), suppressInit.isSelected());

                if (copy.getError() != FileError.NONE) {
                    Platform.runLater(() -> {
                        TestApplication.alert(copy.getErrorDescription());
                    });
                }

                synchronized (this) {
                    running = false;
                }
            }).start();
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

        for (Window w : windows)
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
        synchronized (this) {
            cancelCopy = true;
        }
    }
}
