package de.ganzer.fx.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class GDialog<Controller extends GDialogController<Data>, Data> {
    private final FXMLLoader loader;
    private final Data data;
    private StageStyle style;
    private Modality modality;
    private GDialogController<Data> controller;
    private Consumer<Data> applyDataConsumer;

    public GDialog(FXMLLoader loader, Data data) {
        this.loader = loader;
        this.data = data;
        this.style = StageStyle.DECORATED;
    }

    public void setStyle(StageStyle style) {
        this.style = style == null ? StageStyle.DECORATED : style;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public void setApplyDataConsumer(Consumer<Data> applyDataConsumer) {
        this.applyDataConsumer = applyDataConsumer;
    }

    public void show(Window parent) throws IOException {
        show(parent, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    public void show(Window parent, double xPos, double yPos) throws IOException {
        Stage dialog = createDialog(parent, xPos, yPos, modality != null ? modality : Modality.NONE);
        dialog.show();
    }

    public int showAndWait(Window parent) throws IOException {
        return showAndWait(parent, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    public int showAndWait(Window parent, double xPos, double yPos) throws IOException {
        Stage dialog = createDialog(parent, xPos, yPos, modality != null ? modality : Modality.WINDOW_MODAL);
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

    private Stage createDialog(Window parent, double xPos, double yPos, Modality modality) throws IOException {
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
