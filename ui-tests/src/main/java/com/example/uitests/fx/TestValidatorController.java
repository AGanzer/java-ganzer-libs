package com.example.uitests.fx;

import de.ganzer.core.validation.Validator;

@SuppressWarnings("unused")
public interface TestValidatorController {
    Validator getTestValidator();
    void setTestValidator(Validator validator);
    boolean validateSettings();
}
