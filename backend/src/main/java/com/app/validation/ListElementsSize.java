package com.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ListElementsSizeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListElementsSize {
    String message() default "Each element in the list must not exceed {max} characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int max();
}
