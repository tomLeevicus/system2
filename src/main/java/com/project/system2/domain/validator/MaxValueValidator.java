package com.project.system2.domain.validator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MaxValueValidator implements VariableValidator {
    private final double maxValue;
    
    @Override
    public void validate(String name, Object value) {
        if (value instanceof Number) {
            double numValue = ((Number) value).doubleValue();
            if (numValue > maxValue) {
                throw new RuntimeException(String.format("参数[%s]不能大于%s", name, maxValue));
            }
        }
    }
} 