package org.example.Sql.model;

import java.util.*;

/**
 * Represents a single row in a table.
 * Encapsulation: Data is stored internally and accessed via getter methods.
 */
public class TableRow {
    private final Map<String, Object> data;
    
    public TableRow(Map<String, Object> data) {
        this.data = new HashMap<>(data);
    }
    
    /**
     * Gets the value for a specific column.
     * 
     * @param columnName the column name
     * @return the value, or null if column doesn't exist or value is null
     */
    public Object getValue(String columnName) {
        return data.get(columnName);
    }
    
    /**
     * Gets all column names in this row.
     * 
     * @return set of column names
     */
    public Set<String> getColumnNames() {
        return Collections.unmodifiableSet(data.keySet());
    }
    
    /**
     * Gets all data as an unmodifiable map.
     * 
     * @return map of column names to values
     */
    public Map<String, Object> getData() {
        return Collections.unmodifiableMap(data);
    }
    
    /**
     * Checks if a column value matches a given filter value.
     * 
     * @param columnName the column name to check
     * @param filterValue the value to match against
     * @return true if the values match
     */
    public boolean matches(String columnName, Object filterValue) {
        Object value = data.get(columnName);
        if (value == null && filterValue == null) {
            return true;
        }
        if (value == null || filterValue == null) {
            return false;
        }
        return value.equals(filterValue);
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableRow tableRow = (TableRow) o;
        return Objects.equals(data, tableRow.data);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}

