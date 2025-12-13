package org.example.Sql.constraints;

import org.example.Sql.exception.ConstraintViolationException;
import org.example.Sql.interfaces.Constraint;

/**
 * Constraint implementation that enforces minimum value for integers.
 * Strategy Pattern: Concrete strategy for minimum value validation.
 */
public class MinValueConstraint implements Constraint {
    private final int minValue;
    
    public MinValueConstraint(int minValue) {
        this.minValue = minValue;
    }
    
    @Override
    public void validate(Object value) {
        if (value == null) {
            return; // Null checks are handled by RequiredConstraint
        }
        
        if (!(value instanceof Integer)) {
            throw new ConstraintViolationException(
                "MinValueConstraint can only be applied to Integer values");
        }
        
        int intValue = (Integer) value;
        if (intValue < minValue) {
            throw new ConstraintViolationException(
                String.format("Value %d is less than minimum allowed value %d", intValue, minValue));
        }
    }
    
    @Override
    public String getDescription() {
        return "MIN_VALUE(" + minValue + ")";
    }
    
    public int getMinValue() {
        return minValue;
    }
}




