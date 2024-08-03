package com.example.uitests.fx.charts;

import de.ganzer.core.random.Distribution;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Random;

public abstract class DistributionEnvelop<T extends Number> {
    private final Random rand = new Random();
    private Distribution<T> distribution;
    private DistributionSettingsController controller;
    private Node node;

    public DistributionSettingsController getController() {
        return controller;
    }

    public final Node getEditor() {
        if (node != null)
            return node;

        try {
            FXMLLoader fxmlLoader = createLoader();
            node = fxmlLoader.load();
            controller = fxmlLoader.getController();

            initController(controller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    public Number getNext() {
        if (distribution == null || controller.isChanged()) {
            distribution = createDistribution(controller);
            controller.setChanged(false);
        }

        return distribution.next(rand);
    }

    protected abstract FXMLLoader createLoader();

    protected abstract void initController(DistributionSettingsController controller);

    protected abstract Distribution<T> createDistribution(DistributionSettingsController controller);
}
