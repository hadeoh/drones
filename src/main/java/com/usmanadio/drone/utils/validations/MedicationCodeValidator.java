package com.usmanadio.drone.utils.validations;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
public class MedicationCodeValidator implements ConstraintValidator<MedicationCode, String> {

    private String regex = "^[0-9A-Z_]*$";

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pat = Pattern.compile(regex);
        return pat.matcher(name).matches();
    }
}
