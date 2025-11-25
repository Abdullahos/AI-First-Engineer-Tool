package com.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ListElementsSizeValidator implements ConstraintValidator<ListElementsSize, List<String>> {

    private int max;

    @Override
    public void initialize(ListElementsSize constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(List<String> list, ConstraintValidatorContext context) {
        if (list == null || list.isEmpty()) {
            return true; // @NotEmpty or @Size handles null/empty list itself
        }
        for (String element : list) {
            if (element != null && element.length() > max) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Each element in the list must not exceed " + max + " characters. Found element with length " + element.length() + "."
                        )
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
