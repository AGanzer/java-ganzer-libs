package com.example.uitests.charts;

import de.ganzer.core.random.Distribution;
import de.ganzer.core.random.StudentTDistribution;
import de.ganzer.core.validation.NumberValidator;
import javafx.fxml.FXMLLoader;

public class StudentTDistributionEnvelop extends DistributionEnvelop<Double> {
    @Override
    protected FXMLLoader createLoader() {
        return new FXMLLoader(ChartsController.class.getResource("one-value-settings-view.fxml"));
    }

    @Override
    protected void initController(DistributionSettingsController controller) {
        var settingsController = (OneValueSettingsController)controller;

        var validator = new NumberValidator();
        validator.setMinValue(0.001);
        validator.setNumDecimals(3);

        settingsController.setValueValidator(validator);
        settingsController.setValueLabelText("_Freedom:");
        settingsController.setValue(1.0);
    }

    @Override
    protected Distribution<Double> createDistribution(DistributionSettingsController controller) {
        var settingsController = (OneValueSettingsController)controller;
        return new StudentTDistribution(settingsController.getValue());
    }
}
