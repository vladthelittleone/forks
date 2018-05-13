package com.savik.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FlashscoreIdValidator implements ConstraintValidator<FlashscoreId, String> {
    @Override
    public void initialize(FlashscoreId constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("[a-zA-Z0-9]{8}");
    }
}
