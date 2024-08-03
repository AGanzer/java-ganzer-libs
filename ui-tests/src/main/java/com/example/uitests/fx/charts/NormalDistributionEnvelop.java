package com.example.uitests.fx.charts;

import de.ganzer.core.random.Distribution;
import de.ganzer.core.random.NormalDistribution;
import de.ganzer.core.validation.NumberValidator;
import javafx.fxml.FXMLLoader;

public class NormalDistributionEnvelop extends DistributionEnvelop<Double> {
    @Override
    protected FXMLLoader createLoader() {
        return new FXMLLoader(ChartsController.class.getResource("two-value-settings-view.fxml"));
    }

    @Override
    protected void initController(DistributionSettingsController controller) {
        var settingsController = (TwoValueSettingsController)controller;

        settingsController.setValue1LabelText("_Mean:");
        settingsController.setValue2LabelText("D_eviation:");

        var validator = new NumberValidator();
        validator.setNumDecimals(3);

        settingsController.setValue1Validator(validator);
        settingsController.setValue2Validator(validator);

        settingsController.setValue1(0.0);
        settingsController.setValue2(1.0);
    }

    @Override
    protected Distribution<Double> createDistribution(DistributionSettingsController controller) {
        var settingsController = (TwoValueSettingsController)controller;
        return new NormalDistribution(settingsController.getValue1(), settingsController.getValue2());
    }
}
