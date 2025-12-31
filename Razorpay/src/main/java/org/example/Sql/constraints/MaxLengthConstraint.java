package org.example.Sql.constraints;

import org.example.Sql.exception.ConstraintViolationException;
import org.example.Sql.interfaces.Constraint;

/**
 * Constraint implementation that enforces maximum string length.
 * Strategy Pattern: Concrete strategy for string length validation.
 */
public class MaxLengthConstraint implements Constraint {
    private final int maxLength;
    
    public MaxLengthConstraint(int maxLength) {
        this.maxLength = maxLength;
    }
    
    @Override
    public void validate(Object value) {
        if (value == null) {
            return; // Null checks are handled by RequiredConstraint
        }
        
        if (!(value instanceof String)) {
            throw new ConstraintViolationException(
                "MaxLengthConstraint can only be applied to String values");
        }
        
        String strValue = (String) value;
        if (strValue.length() > maxLength) {
            throw new ConstraintViolationException(
                String.format("String length %d exceeds maximum allowed length %d", 
                    strValue.length(), maxLength));
        }
    }
    
    @Override
    public String getDescription() {
        return "MAX_LENGTH(" + maxLength + ")";
    }
    
    public int getMaxLength() {
        return maxLength;
    }
}







