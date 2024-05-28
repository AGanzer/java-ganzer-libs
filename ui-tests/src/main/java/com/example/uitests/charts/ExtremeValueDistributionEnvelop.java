package com.example.uitests.charts;

import de.ganzer.core.random.Distribution;
import de.ganzer.core.random.ExtremeValueDistribution;
import de.ganzer.core.validation.NumberValidator;
import javafx.fxml.FXMLLoader;

public class ExtremeValueDistributionEnvelop extends DistributionEnvelop<Double> {
    @Override
    protected FXMLLoader createLoader() {
        return new FXMLLoader(ChartsController.class.getResource("two-value-settings-view.fxml"));
    }

    @Override
    protected void initController(DistributionSettingsController controller) {
        var settingsController = (TwoValueSettingsController)controller;

        settingsController.setValue1LabelText("_Location:");
        settingsController.setValue2LabelText("_Scale:");

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
        return new ExtremeValueDistribution(settingsController.getValue1(), settingsController.getValue2());
    }
}
