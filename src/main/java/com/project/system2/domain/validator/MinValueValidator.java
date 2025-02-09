package com.project.system2.domain.validator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MinValueValidator implements VariableValidator {
    private final double minValue;
    
    @Override
    public void validate(String name, Object value) {
        if (value instanceof Number) {
            double numValue = ((Number) value).doubleValue();
            if (numValue < minValue) {
                throw new RuntimeException(String.format("参数[%s]不能小于%s", name, minValue));
            }
        }
    }
} 