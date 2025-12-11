package org.example.Sql.database;

import org.example.Sql.exception.TableNotFoundException;
import org.example.Sql.interfaces.Database;
import org.example.Sql.model.TableRow;
import org.example.Sql.model.TableSchema;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the Database interface.
 * Single Responsibility: Manages collection of tables and delegates table operations.
 * Thread-Safety: Uses ConcurrentHashMap for thread-safe operations.
 */
public class InMemoryDatabase implements Database {
    private final Map<String, Table> tables;
    
    public InMemoryDatabase() {
        this.tables = new ConcurrentHashMap<>();
    }
    
    @Override
    public void createTable(TableSchema schema) {
        String tableName = schema.getTableName();
        
        if (tables.containsKey(tableName)) {
            throw new IllegalArgumentException(
                "Table already exists: " + tableName);
        }
        
        tables.put(tableName, new Table(schema));
        System.out.println("Table '" + tableName + "' created successfully");
    }
    
    @Override
    public void updateTable(String tableName, TableSchema newSchema) {
        Table table = getTable(tableName);
        
        if (!tableName.equals(newSchema.getTableName())) {
            throw new IllegalArgumentException(
                "Schema table name must match existing table name");
        }
        
        table.updateSchema(newSchema);
        System.out.println("Table '" + tableName + "' updated successfully");
    }
    
    @Override
    public void deleteTable(String tableName) {
        if (!tables.containsKey(tableName)) {
            throw new TableNotFoundException(
                "Table not found: " + tableName);
        }
        
        tables.remove(tableName);
        System.out.println("Table '" + tableName + "' deleted successfully");
    }
    
    @Override
    public void insert(String tableName, Map<String, Object> values) {
        Table table = getTable(tableName);
        table.insert(values);
    }
    
    @Override
    public List<TableRow> fetchAll(String tableName) {
        Table table = getTable(tableName);
        return table.getAllRows();
    }
    
    @Override
    public List<TableRow> filter(String tableName, String columnName, Object value) {
        Table table = getTable(tableName);
        return table.filter(columnName, value);
    }
    
    /**
     * Helper method to get a table by name or throw exception if not found.
     * 
     * @param tableName the table name
     * @return the table
     * @throws TableNotFoundException if table doesn't exist
     */
    private Table getTable(String tableName) {
        Table table = tables.get(tableName);
        if (table == null) {
            throw new TableNotFoundException(
                "Table not found: " + tableName);
        }
        return table;
    }
}

