package com.example.uitests;

import de.ganzer.core.validation.Validator;

public class ValidatorSettingsController implements TestValidatorController {
    //region fields
    private Validator testValidator;
    //endregion

    //region getter/setter
    public Validator getTestValidator() {
        return testValidator;
    }

    public void setTestValidator(Validator testValidator) {
        this.testValidator = testValidator;
    }
    //endregion
}
