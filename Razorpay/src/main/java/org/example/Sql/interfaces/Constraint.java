package org.example.Sql.interfaces;

/**
 * Strategy Pattern: Defines the contract for validation constraints.
 * Each constraint implements its own validation logic independently.
 * This follows the Single Responsibility Principle - each constraint has one reason to change.
 */
public interface Constraint {
    /**
     * Validates a value against the constraint.
     * 
     * @param value the value to validate
     * @throws org.example.Sql.exception.ConstraintViolationException if validation fails
     */
    void validate(Object value);
    
    /**
     * Returns a description of the constraint for error messages.
     * 
     * @return constraint description
     */
    String getDescription();
}




