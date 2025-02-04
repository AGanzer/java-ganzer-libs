package de.ganzer.fx.dialogs;

import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * Each Controller that is used with {@link GDialog} should implement this
 * interface.
 * <p>
 * The following code shows an example how it may be implemented:
 * <p>
 * {@code
public class InputDialogData {
    public String input;
}

public class InputDialogController implements GDialogController<InputDialogData> {
    private int modalResult = 0;
    private InputDialogData data;
    private Consumer<InputDialogData> applyDataConsumer;

    public TextField textField;

    public void applyClicked(ActionEvent ignored) {
        data.input = textField.getText();
        applyDataConsumer.accept(data);
    }

    public void okClicked(ActionEvent ignored) {
        data.input = textField.getText();
        modalResult = ModalResult.OK;

        textField.getScene().getWindow().hide();
    }

    public void cancelClicked(ActionEvent ignored) {
        modalResult = ModalResult.CANCEL;
        textField.getScene().getWindow().hide();
    }

    @Override
    public void setApplyDataConsumer(Consumer<InputDialogData> applyDataConsumer) {
        this.applyDataConsumer = applyDataConsumer;
    }

    @Override
    public void initControls(InputDialogData data) {
        this.data = data;
        textField.setText(data.input);
    }

    @Override
    public int getModalResult() {
        return modalResult;
    }
}
 * }
 *
 * @param <Data> The type of the data the dialog works with.
 */
@SuppressWarnings("unused")
public interface GDialogController<Data> {
    /**
     * Called to initialize the view's controls.
     *
     * @param data The data where to initialize the controls with.
     */
    void initControls(Data data);

    /**
     * This should be implemented if the dialog contains an "Apply" button that
     * does not close the dialog.
     *
     * @param applyDataConsumer The function to call when the "Apply" button is
     *                          clicked. This may be {@code null}.
     */
    default void setApplyDataConsumer(Consumer<Data> applyDataConsumer) {
    }

    /**
     * This should be implemented if the dialog won't apply data after it is
     * closed.
     * <p>
     * The only reason to override this method is, to prevent applying data
     * after closing because the data is always applied within the dialog
     * before it is closed.
     *
     * @return {@code true} to force {@link GDialog} to apply modified data
     * after the dialog is closed. The default implementation returns
     * {@code true}.
     */
    default boolean autoApplyDataAfterClose() {
        return true;
    }

    /**
     * Implementors should override this if the dialog shall be shown modal to
     * return the modal result of the dialog.
     *
     * @return A value that identifies the modal result of the closed dialog.
     * This implementation returns {@link ModalResult#CANCEL}
     */
    default int getModalResult() {
        return ModalResult.CANCEL;
    }

    /**
     * Called to restore dialog UI settings.
     * <p>
     * Implementers have to override this if the dialog shall save and restore
     * settings.
     *
     * @param stage The stage where to apply the boundary settings to.
     *        {@link de.ganzer.fx.util.UISettings#apply(String, Stage)} may
     *        be used for this.
     */
    default void restoreSettings(Stage stage) {
    }

    /**
     * Called to save dialog UI settings.
     * <p>
     * Implementers have to override this if the dialog shall save and restore
     * settings.
     *
     * @param stage The stage where to write the boundary settings from.
     *        {@link de.ganzer.fx.util.UISettings#write(String, Stage)} may
     *        be used for this.
     */
    default void saveSettings(Stage stage) {
    }
}
