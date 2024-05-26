package com.example.uitests;

import de.ganzer.fx.dialogs.GDialogController;
import de.ganzer.fx.dialogs.ModalResult;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class TestDialogController implements GDialogController<Object> {
    private int modalResult = 0;

    public TextField textField;

    public void okClicked(ActionEvent ignored) {
        modalResult = ModalResult.OK;
        textField.getScene().getWindow().hide();
    }

    public void cancelClicked(ActionEvent ignored) {
        modalResult = ModalResult.CANCEL;
        textField.getScene().getWindow().hide();
    }

    @Override
    public void initControls(Object ignored) {
    }

    @Override
    public int getModalResult() {
        return modalResult;
    }
}
