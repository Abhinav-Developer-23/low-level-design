package org.example.Sql.interfaces;

import org.example.Sql.model.TableRow;
import org.example.Sql.model.TableSchema;

import java.util.List;
import java.util.Map;

/**
 * Interface defining core database operations.
 * Interface Segregation Principle: Clients depend only on methods they use.
 * Dependency Inversion Principle: High-level code depends on abstraction, not concrete implementation.
 */
public interface Database {
    
    /**
     * Creates a new table with the given schema.
     * 
     * @param schema the table schema defining structure and constraints
     * @throws IllegalArgumentException if table already exists
     */
    void createTable(TableSchema schema);
    
    /**
     * Updates an existing table's schema.
     * 
     * @param tableName the name of the table to update
     * @param newSchema the new schema to apply
     * @throws org.example.Sql.exception.TableNotFoundException if table doesn't exist
     */
    void updateTable(String tableName, TableSchema newSchema);
    
    /**
     * Deletes a table from the database.
     * 
     * @param tableName the name of the table to delete
     * @throws org.example.Sql.exception.TableNotFoundException if table doesn't exist
     */
    void deleteTable(String tableName);
    
    /**
     * Inserts a new row into a table.
     * 
     * @param tableName the name of the table
     * @param values map of column names to values
     * @throws org.example.Sql.exception.TableNotFoundException if table doesn't exist
     * @throws org.example.Sql.exception.ConstraintViolationException if constraints are violated
     */
    void insert(String tableName, Map<String, Object> values);
    
    /**
     * Retrieves all rows from a table.
     * 
     * @param tableName the name of the table
     * @return list of all rows in the table
     * @throws org.example.Sql.exception.TableNotFoundException if table doesn't exist
     */
    List<TableRow> fetchAll(String tableName);
    
    /**
     * Filters rows where a specific column matches a value.
     * 
     * @param tableName the name of the table
     * @param columnName the column to filter on
     * @param value the value to match
     * @return list of matching rows
     * @throws org.example.Sql.exception.TableNotFoundException if table doesn't exist
     */
    List<TableRow> filter(String tableName, String columnName, Object value);
}







