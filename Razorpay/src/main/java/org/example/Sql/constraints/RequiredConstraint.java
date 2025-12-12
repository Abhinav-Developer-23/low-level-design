package org.example.Sql.constraints;

import org.example.Sql.exception.ConstraintViolationException;
import org.example.Sql.interfaces.Constraint;

/**
 * Constraint implementation that enforces non-null values.
 * Strategy Pattern: Concrete strategy for required field validation.
 */
public class RequiredConstraint implements Constraint {
    
    @Override
    public void validate(Object value) {
        if (value == null) {
            throw new ConstraintViolationException("Value is required but was null");
        }
    }
    
    @Override
    public String getDescription() {
        return "REQUIRED";
    }
}



