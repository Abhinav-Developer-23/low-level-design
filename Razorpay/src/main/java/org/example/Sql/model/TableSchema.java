package org.example.Sql.model;

import org.example.Sql.exception.InvalidSchemaException;

import java.util.*;

/**
 * Represents the schema (structure) of a table.
 * Builder Pattern: Provides fluent API for constructing table schemas.
 * Encapsulation: Protects the internal column definitions list.
 */
public class TableSchema {
    private final String tableName;
    private final List<ColumnDefinition> columns;
    private final Map<String, ColumnDefinition> columnMap;
    
    private TableSchema(String tableName, List<ColumnDefinition> columns) {
        this.tableName = tableName;
        this.columns = new ArrayList<>(columns);
        this.columnMap = new HashMap<>();
        
        // Build index for fast column lookup
        for (ColumnDefinition column : columns) {
            if (columnMap.containsKey(column.getName())) {
                throw new InvalidSchemaException(
                    "Duplicate column name: " + column.getName());
            }
            columnMap.put(column.getName(), column);
        }
        
        if (columns.isEmpty()) {
            throw new InvalidSchemaException("Table must have at least one column");
        }
    }
    
    /**
     * Factory method to create a table schema builder.
     * 
     * @param tableName the name of the table
     * @return builder for table schema
     */
    public static TableSchemaBuilder builder(String tableName) {
        return new TableSchemaBuilder(tableName);
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public List<ColumnDefinition> getColumns() {
        return Collections.unmodifiableList(columns);
    }
    
    /**
     * Gets a column definition by name.
     * 
     * @param columnName the column name
     * @return the column definition, or null if not found
     */
    public ColumnDefinition getColumn(String columnName) {
        return columnMap.get(columnName);
    }
    
    /**
     * Checks if a column exists in the schema.
     * 
     * @param columnName the column name
     * @return true if the column exists
     */
    public boolean hasColumn(String columnName) {
        return columnMap.containsKey(columnName);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Table: ").append(tableName).append("\n");
        for (ColumnDefinition col : columns) {
            sb.append("  - ").append(col).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Builder class for TableSchema.
     * Builder Pattern: Allows step-by-step construction of table schemas.
     */
    public static class TableSchemaBuilder {
        private final String tableName;
        private final List<ColumnDefinition> columns = new ArrayList<>();
        
        private TableSchemaBuilder(String tableName) {
            if (tableName == null || tableName.trim().isEmpty()) {
                throw new InvalidSchemaException("Table name cannot be null or empty");
            }
            this.tableName = tableName;
        }
        
        /**
         * Adds a column to the schema.
         * 
         * @param column the column definition to add
         * @return this builder for chaining
         */
        public TableSchemaBuilder addColumn(ColumnDefinition column) {
            columns.add(column);
            return this;
        }
        
        /**
         * Builds the final TableSchema.
         * 
         * @return the constructed TableSchema
         */
        public TableSchema build() {
            return new TableSchema(tableName, columns);
        }
    }
}






