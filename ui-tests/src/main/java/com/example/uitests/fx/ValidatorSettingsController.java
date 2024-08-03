package com.example.uitests.fx;

import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorExceptionRef;

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

    @Override
    public boolean validateSettings(ValidatorExceptionRef ref) {
        return true;
    }
}
