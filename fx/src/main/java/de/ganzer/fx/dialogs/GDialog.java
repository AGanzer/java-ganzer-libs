package de.ganzer.fx.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * This utility class simplifies the way to load and show a dialog that is
 * created by an FXML file.
 * <p>
 * The following example shows how the {@code GDialog} class can be used:
 * <p>
 * {@code
public class MainWindowController {
    // Other code here ...

    public void showDialogModal(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("my-dialog-view.fxml"));
        GDialog<InputDialogController, InputDialogData> dialog = new GDialog<>(fxmlLoader, new InputDialogData());

        if (dialog.showAndWait(findMyWindow()) != ModalResult.CANCEL)
            workWithInput(dialog.getData());
    }

    public void showDialog(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("my-dialog-view.fxml"));
        GDialog<InputDialogController, InputDialogData> dialog = new GDialog<>(fxmlLoader, new InputDialogData());

        dialog.setApplyDataConsumer(input -> workWithInput(input));
        dialog.show(null);
    }
}

 * }
 *
 * @param <Controller> The Controller class that is created by
 *                     the {@link FXMLLoader} that is given to the constructor.
 * @param <Data> The type of the data the dialog that to show created is
 *               working with.
 */
@SuppressWarnings("unused")
public class GDialog<Controller extends GDialogController<Data>, Data> {
    private final FXMLLoader loader;
    private final Data data;
    private StageStyle style;
    private Modality modality;
    private GDialogController<Data> controller;
    private Consumer<Data> applyDataConsumer;

    /**
     * Creates a new instance from the specified arguments.
     * <p>
     * The style and the modality are set to {@code null}.
     *
     * @param loader The loader to create the dialog to show from the resources.
     * @param data The data that initializes the dialog and that may be changed
     *             by the user. This can be {@code null} if the dialog does not
     *             require any data.
     *
     * @throws NullPointerException {@code loader} is {@code null}.
     *
     * @see #setModality(Modality)
     * @see #setStyle(StageStyle)
     */
    public GDialog(FXMLLoader loader, Data data) {
        Objects.requireNonNull(loader, "GDialog::GDialog: loader");

        this.loader = loader;
        this.data = data;
    }

    /**
     * Gets the data that was given to the constructor.
     *
     * @return The data that was given to the constructor.
     */
    public Data getData() {
        return data;
    }

    /**
     * Changes the style of the dialog to show.
     *
     * @param style The new style. If this is {@code null} (the default), the
     *              used style depends on the used method to show the dialog.
     *
     * @see #show(Window)
     * @see #showAndWait(Window)
     */
    public void setStyle(StageStyle style) {
        this.style = style;
    }

    /**
     * Changes the modality of the dialog to show.
     * 
     * @param modality The modality to set. If this is {@code null} (the
     *                 default), the used modality depends on the used method
     *                 to show the dialog.
     *                 
     * @see #show(Window) 
     * @see #showAndWait(Window) 
     */
    public void setModality(Modality modality) {
        this.modality = modality;
    }

    /**
     * Sets the consumer that shall be called when an "Apply" button of the
     * dialog to show is clicked.
     *
     * @param applyDataConsumer The consumer to call. This can be {@code null}
     *                          if the dialog does not provide an "Apply" action.
     */
    public void setApplyDataConsumer(Consumer<Data> applyDataConsumer) {
        this.applyDataConsumer = applyDataConsumer;
    }

    /**
     * This calls {@link #show(Window, double, double)} with the specified
     * parent to open the dialog centered to {@code parent}.
     *
     * @param parent The parent to set. This can be {@code null} if the dialog
     *               shall not hide other windows.
     *
     * @throws IOException If the dialog cannot be created from the resources.
     */
    public void show(Window parent) throws IOException {
        show(parent, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    /**
     * Shows the dialog non-modal.
     * <p>
     * This method returns immediately and does not wait until the dialog is
     * closed.
     * <p>
     * If the modality is {@code null}, {@link Modality#NONE} is used.
     * <p>
     * If the style is {@code null}, {@link StageStyle#DECORATED} is used.
     *
     * @param parent The parent to set. This can be {@code null} if the dialog
     *               shall not hide other windows.
     * @param xPos   The X-position of the dialog's top-left corner when it is
     *               shown. If this is {@link Double#MIN_VALUE}, the dialog is
     *               horizontally centered to its parent.
     * @param yPos   The Y-position of the dialog's top-left corner when it is
     *               shown. If this is {@link Double#MIN_VALUE}, the dialog is
     *               vertically centered to its parent.
     *
     * @see #setModality(Modality)
     * @see #setStyle(StageStyle)
     * @see #showAndWait(Window)
     * @see #showAndWait(Window, double, double)
     *
     * @throws IOException If the dialog cannot be created from the resources.
     */
    public void show(Window parent, double xPos, double yPos) throws IOException {
        Stage dialog = createDialog(
                parent,
                xPos,
                yPos,
                modality != null ? modality : Modality.NONE,
                style != null ? style : StageStyle.DECORATED);
        dialog.show();
    }

    /**
     * This calls {@link #showAndWait(Window, double, double)} with the specified
     * parent to open the dialog centered to {@code parent}.
     *
     * @param parent The parent to set. This should be better not {@code null}
     *               for modal dialogs.
     *
     * @throws IOException If the dialog cannot be created from the resources.
     */
    public int showAndWait(Window parent) throws IOException {
        return showAndWait(parent, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    /**
     * Shows the dialog modal.
     * <p>
     * This method does not return before the dialog is closed.
     * <p>
     * If the modality is {@code null}, {@link Modality#WINDOW_MODAL} is used.
     * <p>
     * If the style is {@code null}, {@link StageStyle#UTILITY} is used.
     *
     * @param parent The parent to set. This should be better not {@code null}
     *               for modal dialogs.
     * @param xPos   The X-position of the dialog's top-left corner when it is
     *               shown. If this is {@link Double#MIN_VALUE}, the dialog is
     *               horizontally centered to its parent.
     * @param yPos   The Y-position of the dialog's top-left corner when it is
     *               shown. If this is {@link Double#MIN_VALUE}, the dialog is
     *               vertically centered to its parent.
     *
     * @return The modal result of the dialog. This can be every integer, but
     * for better readability it can be one of the {@link ModalResult} constants.
     *
     * @see #setModality(Modality)
     * @see #setStyle(StageStyle)
     * @see #show(Window)
     * @see #show(Window, double, double)
     *
     * @throws IOException If the dialog cannot be created from the resources.
     */
    public int showAndWait(Window parent, double xPos, double yPos) throws IOException {
        Stage dialog = createDialog(
                parent,
                xPos,
                yPos,
                modality != null ? modality : Modality.WINDOW_MODAL,
                style != null ? style : StageStyle.UTILITY);
        dialog.showAndWait();

        int result = controller.getModalResult();

        if (shouldApplyData(result))
            applyDataConsumer.accept(data);

        return result;
    }

    private boolean shouldApplyData(int result) {
        return result != ModalResult.CANCEL
                && applyDataConsumer != null
                && data != null
                && controller.autoApplyDataAfterClose();
    }

    private Stage createDialog(Window parent, double xPos, double yPos, Modality modality, StageStyle style) throws IOException {
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage dialog = new Stage();
        dialog.onShowingProperty().addListener((p, o, n) -> adjustPosition(dialog, parent, xPos, yPos));

        dialog.initOwner(parent);
        dialog.initModality(modality);
        dialog.initStyle(style);
        dialog.setScene(scene);

        controller = loader.getController();
        controller.initControls(data);
        controller.setApplyDataConsumer(applyDataConsumer);

        return dialog;
    }

    private static void adjustPosition(Stage dialog, Window parent, double xPos, double yPos) {
        if (parent == null)
            return;

        double adjustedX = xPos != Double.MIN_VALUE
                ? xPos
                : parent.getX() + (parent.getWidth() - dialog.getWidth()) / 2;
        double adjustedY = yPos != Double.MIN_VALUE
                ? yPos
                : parent.getY() + (parent.getHeight() - dialog.getHeight()) / 2;

        dialog.setX(adjustedX);
        dialog.setY(adjustedY);
    }
}
