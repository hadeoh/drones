package com.usmanadio.drone.utils.validations;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
public class MedicationNameValidator implements ConstraintValidator<MedicationName, String> {

    private String regex = "[0-9a-zA-Z_-]";

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pat = Pattern.compile(regex);
        return pat.matcher(name).matches();
    }
}
