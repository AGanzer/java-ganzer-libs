package de.ganzer.fx.dialogs;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("input-dialog-view.fxml"));
        GDialog<InputDialogController, InputDialogData> dialog = new GDialog<>(fxmlLoader, new InputDialogData());

        if (dialog.showAndWait(findMyWindow()) != ModalResult.CANCEL)
            workWithInput(dialog.getData());
    }

    public void showDialog(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("input-dialog-view.fxml"));
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
    private final Data data;
    private final Stage dialog;
    private final GDialogController<Data> controller;
    private Modality modality;
    private StageStyle style;
    private Consumer<Data> applyDataConsumer;

    /**
     * Creates a new instance from the specified arguments.
     * <p>
     * The style and the modality are set to {@code null}.
     *
     * @param loader The loader to use to create the dialog to show from the
     *               resources.
     * @param data The data that initializes the dialog and that may be changed
     *             by the user. This can be {@code null} if the dialog does not
     *             require any data.
     *
     * @throws NullPointerException {@code loader} is {@code null}.
     * @throws IOException When the dialog root cannot be loaded from the
     * resources.
     *
     * @see #setModality(Modality)
     * @see #setStyle(StageStyle)
     */
    public GDialog(FXMLLoader loader, Data data) throws IOException {
        this(null, loader, data);
    }

    /**
     * Creates a new instance from the specified arguments.
     * <p>
     * The style and the modality are set to {@code null}.
     *
     * @param title The title to set for the dialog to show.
     * @param loader The loader to use to create the dialog to show from the
     *               resources.
     * @param data The data that initializes the dialog and that may be changed
     *             by the user. This can be {@code null} if the dialog does not
     *             require any data.
     *
     * @throws NullPointerException {@code loader} is {@code null}.
     * @throws IOException When the dialog root cannot be loaded from the
     * resources.
     *
     * @see #setModality(Modality)
     * @see #setStyle(StageStyle)
     */
    public GDialog(String title, FXMLLoader loader, Data data) throws IOException {
        Objects.requireNonNull(loader, "GDialog::GDialog: loader");

        Parent root = loader.load();

        this.data = data;
        this.controller = loader.getController();
        this.dialog = new Stage();
        this.dialog.setScene(new Scene(root));
    }

    /**
     * Creates a new instance from the specified arguments.
     * <p>
     * The style and the modality are set to {@code null}.
     *
     * @param dialogRoot The parent root of the dialog to show.
     * @param controller The controller to use.
     * @param data The data that initializes the dialog and that may be changed
     *             by the user. This can be {@code null} if the dialog does not
     *             require any data.
     *
     * @throws NullPointerException {@code dialogRoot} or {@code controller}
     * is {@code null}.
     *
     * @see #setModality(Modality)
     * @see #setStyle(StageStyle)
     */
    public GDialog(Parent dialogRoot, Controller controller, Data data) {
        this(null, dialogRoot, controller, data);
    }

    /**
     * Creates a new instance from the specified arguments.
     * <p>
     * The style and the modality are set to {@code null}.
     *
     * @param title The title to set for the dialog to show.
     * @param dialogRoot The parent root of the dialog to show.
     * @param controller The controller to use.
     * @param data The data that initializes the dialog and that may be changed
     *             by the user. This can be {@code null} if the dialog does not
     *             require any data.
     *
     * @throws NullPointerException {@code dialogRoot} or {@code controller}
     * is {@code null}.
     *
     * @see #setModality(Modality)
     * @see #setStyle(StageStyle)
     */
    public GDialog(String title, Parent dialogRoot, Controller controller, Data data) {
        Objects.requireNonNull(dialogRoot, "GDialog::GDialog: dialogRoot");
        Objects.requireNonNull(controller, "GDialog::GDialog: controller");

        this.data = data;
        this.controller = controller;
        this.dialog = new Stage();
        this.dialog.setScene(new Scene(dialogRoot));
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
     * Sets the title of the dialog to show.
     *
     * @param title The new title.
     */
    public void setTitle(String title) {
        dialog.setTitle(title);
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
     * Gets the icons of the underlying stage.
     *
     * @return The icons of the stage.
     */
    public ObservableList<Image> getIcons() {
        return dialog.getIcons();
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
        controller.setApplyDataConsumer(applyDataConsumer);
    }

    /**
     * This is the same as a call to {@link #show(Window)} with a parent
     * set to {@code null}.
     */
    public void show() {
        show(null, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    /**
     * This calls {@link #show(Window, double, double)} with the specified
     * parent to open the dialog centered to {@code parent}.
     *
     * @param parent The parent to set. This can be {@code null} if the dialog
     *               shall not hide other windows.
     */
    public void show(Window parent) {
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
     */
    public void show(Window parent, double xPos, double yPos) {
        prepareForShow(
                parent,
                xPos,
                yPos,
                modality != null ? modality : Modality.NONE,
                style != null ? style : StageStyle.DECORATED);

        dialog.show();
    }

    /**
     * This is the same as a call to {@link #showAndWait(Window)} with a parent
     * set to {@code null}.
     */
    public int showAndWait() {
        return showAndWait(null, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    /**
     * This calls {@link #showAndWait(Window, double, double)} with the specified
     * parent to open the dialog centered to {@code parent}.
     *
     * @param parent The parent to set.
     */
    public int showAndWait(Window parent) {
        return showAndWait(parent, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    /**
     * Shows the dialog modal.
     * <p>
     * This method does not return before the dialog is closed.
     * <p>
     * If the modality is {@code null}, {@link Modality#APPLICATION_MODAL} is
     * used.
     * <p>
     * If the style is {@code null}, {@link StageStyle#UTILITY} is used.
     *
     * @param parent The parent to set.
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
     */
    public int showAndWait(Window parent, double xPos, double yPos) {
        prepareForShow(
                parent,
                xPos,
                yPos,
                modality != null ? modality : Modality.APPLICATION_MODAL,
                style != null ? style : StageStyle.UTILITY);

        // toFront() is necessary because sometimes the parent window
        // gets the input focus after the dialog is shown:
        //
        Platform.runLater(dialog::toFront);
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

    private void prepareForShow(Window parent, double xPos, double yPos, Modality modality, StageStyle style) {
        dialog.initOwner(parent);
        dialog.initModality(modality);
        dialog.initStyle(style);
        dialog.onShowingProperty().addListener((p, o, n) -> adjustPosition(parent, xPos, yPos));
        dialog.setOnCloseRequest(e -> controller.saveSettings(dialog));

        controller.restoreSettings(dialog);
        controller.initControls(data);
    }

    @SuppressWarnings("DuplicatedCode")
    private void adjustPosition(Window parent, double xPos, double yPos) {
        if (parent == null)
            return;

        double adjustedX = xPos != Double.MIN_VALUE
                ? xPos
                : Math.max(0, parent.getX() + (parent.getWidth() - dialog.getWidth()) / 2);
        double adjustedY = yPos != Double.MIN_VALUE
                ? yPos
                : Math.max(0, parent.getY() + (parent.getHeight() - dialog.getHeight()) / 2);

        dialog.setX(adjustedX);
        dialog.setY(adjustedY);
    }
}
