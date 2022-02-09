package com.usmanadio.drone.utils.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = MedicationNameValidator.class)
@Documented
public @interface MedicationName {
    String message() default "only letters, numbers, ‘-‘, ‘_’ are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
