package com.jope.financetracker.validations.constraints.value_of_enum;

import java.util.List;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedNames;
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedNames = Stream.of(annotation.enumClass().getEnumConstants()).map(Enum::name).toList();
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants()).map(Enum::toString).toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString()) || acceptedNames.contains(value.toString());
    }
}
