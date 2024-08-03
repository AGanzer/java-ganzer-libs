package com.example.uitests.fx;

import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorExceptionRef;

@SuppressWarnings("unused")
public interface TestValidatorController {
    Validator getTestValidator();
    void setTestValidator(Validator validator);
    boolean validateSettings(ValidatorExceptionRef ref);
}
