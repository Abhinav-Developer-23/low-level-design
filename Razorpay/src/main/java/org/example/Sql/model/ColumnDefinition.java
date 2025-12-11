package org.example.Sql.model;

import org.example.Sql.constraints.MaxLengthConstraint;
import org.example.Sql.constraints.MinValueConstraint;
import org.example.Sql.constraints.RequiredConstraint;
import org.example.Sql.enums.ColumnType;
import org.example.Sql.interfaces.Constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a column definition with its type and constraints.
 * Builder Pattern: Provides fluent API for constructing column definitions.
 * Encapsulation: Hides internal constraint management.
 */
public class ColumnDefinition {
    private final String name;
    private final ColumnType type;
    private final List<Constraint> constraints;
    
    private ColumnDefinition(String name, ColumnType type, List<Constraint> constraints) {
        this.name = name;
        this.type = type;
        this.constraints = new ArrayList<>(constraints);
    }
    
    /**
     * Factory method to create an integer column builder.
     * 
     * @param name column name
     * @return builder for integer column
     */
    public static ColumnBuilder intColumn(String name) {
        return new ColumnBuilder(name, ColumnType.INT);
    }
    
    /**
     * Factory method to create a string column builder.
     * 
     * @param name column name
     * @return builder for string column
     */
    public static ColumnBuilder stringColumn(String name) {
        return new ColumnBuilder(name, ColumnType.STRING);
    }
    
    /**
     * Validates a value against all constraints for this column.
     * 
     * @param value the value to validate
     */
    public void validate(Object value) {
        for (Constraint constraint : constraints) {
            constraint.validate(value);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public ColumnType getType() {
        return type;
    }
    
    public List<Constraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }
    
    public boolean isRequired() {
        return constraints.stream()
            .anyMatch(c -> c instanceof RequiredConstraint);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(type);
        if (!constraints.isEmpty()) {
            sb.append(" [");
            for (int i = 0; i < constraints.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(constraints.get(i).getDescription());
            }
            sb.append("]");
        }
        return sb.toString();
    }
    
    /**
     * Builder class for ColumnDefinition.
     * Builder Pattern: Allows step-by-step construction of column definitions.
     */
    public static class ColumnBuilder {
        private final String name;
        private final ColumnType type;
        private final List<Constraint> constraints = new ArrayList<>();
        
        private ColumnBuilder(String name, ColumnType type) {
            this.name = name;
            this.type = type;
        }
        
        /**
         * Marks the column as required (NOT NULL).
         * 
         * @return this builder for chaining
         */
        public ColumnBuilder required() {
            constraints.add(new RequiredConstraint());
            return this;
        }
        
        /**
         * Marks the column as optional (can be NULL).
         * 
         * @return this builder for chaining
         */
        public ColumnBuilder optional() {
            // Do nothing - columns are optional by default
            return this;
        }
        
        /**
         * Adds a minimum value constraint (for INT columns).
         * 
         * @param minValue minimum allowed value
         * @return this builder for chaining
         */
        public ColumnBuilder withMinValue(int minValue) {
            if (type != ColumnType.INT) {
                throw new IllegalArgumentException("MinValue constraint can only be applied to INT columns");
            }
            constraints.add(new MinValueConstraint(minValue));
            return this;
        }
        
        /**
         * Adds a maximum length constraint (for STRING columns).
         * 
         * @param maxLength maximum allowed string length
         * @return this builder for chaining
         */
        public ColumnBuilder withMaxLength(int maxLength) {
            if (type != ColumnType.STRING) {
                throw new IllegalArgumentException("MaxLength constraint can only be applied to STRING columns");
            }
            constraints.add(new MaxLengthConstraint(maxLength));
            return this;
        }
        
        /**
         * Adds a custom constraint.
         * 
         * @param constraint the constraint to add
         * @return this builder for chaining
         */
        public ColumnBuilder withConstraint(Constraint constraint) {
            constraints.add(constraint);
            return this;
        }
        
        /**
         * Builds the final ColumnDefinition.
         * 
         * @return the constructed ColumnDefinition
         */
        public ColumnDefinition build() {
            return new ColumnDefinition(name, type, constraints);
        }
    }
}

