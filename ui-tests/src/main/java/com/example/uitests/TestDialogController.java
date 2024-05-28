package com.example.uitests;

import de.ganzer.fx.dialogs.GDialogController;
import de.ganzer.fx.dialogs.ModalResult;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class TestDialogController implements GDialogController<String> {
    public TextField textField;
    public Button okButton;
    public Button applyButton;
    public Button cancelButton;
    public ButtonBar buttonBar;
    private int modalResult = 0;

    private Consumer<String> applyDataConsumer;

    @Override
    public void initControls(String data) {
        textField.setText(data == null ? "" : data);

        if (data != null && !data.isEmpty()) {
            applyButton.setVisible(true);
            okButton.setText("_Close");

            buttonBar.getButtons().remove(cancelButton);
        }

        Platform.runLater(() -> textField.requestFocus());
    }

    @Override
    public void setApplyDataConsumer(Consumer<String> applyDataConsumer) {
        this.applyDataConsumer = applyDataConsumer;
    }

    @Override
    public int getModalResult() {
        return modalResult;
    }

    public void okClicked(ActionEvent ignored) {
        modalResult = ModalResult.OK;
        textField.getScene().getWindow().hide();
    }

    public void cancelClicked(ActionEvent ignored) {
        modalResult = ModalResult.CANCEL;
        textField.getScene().getWindow().hide();
    }

    public void applyClicked(ActionEvent ignored) {
        applyDataConsumer.accept(textField.getText());
    }
}
