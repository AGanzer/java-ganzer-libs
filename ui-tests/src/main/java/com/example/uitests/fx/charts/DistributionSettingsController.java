package com.example.uitests.fx.charts;

import de.ganzer.core.validation.ValidatorExceptionRef;
import javafx.scene.control.TextField;

public interface DistributionSettingsController {
    boolean isChanged();
    void setChanged(boolean changed);
    TextField validate(ValidatorExceptionRef ref);
}
