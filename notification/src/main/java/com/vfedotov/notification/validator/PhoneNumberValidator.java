package com.vfedotov.notification.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        String PHONE_NUMBER_REGEX = "\\+?7[0-9]{10}";
        return value.matches(PHONE_NUMBER_REGEX);
    }
}
