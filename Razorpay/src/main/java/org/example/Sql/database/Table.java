package org.example.Sql.database;

import org.example.Sql.exception.ConstraintViolationException;
import org.example.Sql.model.ColumnDefinition;
import org.example.Sql.model.TableRow;
import org.example.Sql.model.TableSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Internal representation of a table in the database.
 * Encapsulation: Manages rows and schema internally, exposes operations via methods.
 * Single Responsibility: Handles table-level operations only.
 */
class Table {
    private TableSchema schema;
    private final List<TableRow> rows;
    
    public Table(TableSchema schema) {
        this.schema = schema;
        this.rows = new ArrayList<>();
    }
    
    /**
     * Updates the schema of this table.
     * Existing rows are preserved; new columns will have null values.
     * 
     * @param newSchema the new schema to apply
     */
    public void updateSchema(TableSchema newSchema) {
        // Validate that existing required columns are preserved
        for (ColumnDefinition existingCol : schema.getColumns()) {
            if (existingCol.isRequired() && !newSchema.hasColumn(existingCol.getName())) {
                throw new ConstraintViolationException(
                    "Cannot remove required column: " + existingCol.getName());
            }
        }
        
        this.schema = newSchema;
    }
    
    /**
     * Inserts a new row into the table.
     * Validates all constraints before insertion.
     * 
     * @param values map of column names to values
     */
    public void insert(Map<String, Object> values) {
        Map<String, Object> validatedData = new HashMap<>();
        
        // Validate and process each column
        for (ColumnDefinition column : schema.getColumns()) {
            String columnName = column.getName();
            Object value = values.get(columnName);
            
            // Validate constraints
            try {
                column.validate(value);
            } catch (ConstraintViolationException e) {
                throw new ConstraintViolationException(
                    "Validation failed for column '" + columnName + "': " + e.getMessage(), e);
            }
            
            validatedData.put(columnName, value);
        }
        
        // Check for unknown columns
        for (String providedColumn : values.keySet()) {
            if (!schema.hasColumn(providedColumn)) {
                throw new ConstraintViolationException(
                    "Unknown column: " + providedColumn);
            }
        }
        
        rows.add(new TableRow(validatedData));
    }
    
    /**
     * Returns all rows in the table.
     * 
     * @return list of all rows
     */
    public List<TableRow> getAllRows() {
        return new ArrayList<>(rows);
    }
    
    /**
     * Filters rows based on a column value.
     * 
     * @param columnName the column to filter on
     * @param value the value to match
     * @return list of matching rows
     */
    public List<TableRow> filter(String columnName, Object value) {
        if (!schema.hasColumn(columnName)) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        
        return rows.stream()
            .filter(row -> row.matches(columnName, value))
            .collect(Collectors.toList());
    }
    
    public TableSchema getSchema() {
        return schema;
    }
    
    public int getRowCount() {
        return rows.size();
    }
}

