package org.example.Sql;

import org.example.Sql.interfaces.Database;
import org.example.Sql.database.InMemoryDatabase;
import org.example.Sql.exception.ConstraintViolationException;
import org.example.Sql.model.ColumnDefinition;
import org.example.Sql.model.TableRow;
import org.example.Sql.model.TableSchema;

import java.util.List;
import java.util.Map;

/**
 * Demo runner showcasing the in-memory SQL-like database.
 */
public class Main {
    public static void main(String[] args) {
        Database database = new InMemoryDatabase();

        // Define schema using the builder + strategy based constraints
        TableSchema userSchema = TableSchema.builder("users")
            .addColumn(ColumnDefinition.intColumn("id")
                .required()
                .withMinValue(1024)
                .build())
            .addColumn(ColumnDefinition.stringColumn("name")
                .required()
                .withMaxLength(20)
                .build())
            .addColumn(ColumnDefinition.stringColumn("email")
                .optional()
                .withMaxLength(20)
                .build())
            .build();

        System.out.println("Creating table 'users' ...");
        database.createTable(userSchema);

        // Insert valid records
        System.out.println("Inserting rows ...");
        database.insert("users", Map.of(
            "id", 1024,
            "name", "Alice",
            "email", "alice@example.com"
        ));
        database.insert("users", Map.of(
            "id", 2048,
            "name", "Bob Builder",
            "email", "bob@work.com"
        ));
        database.insert("users", Map.of(
            "id", 4096,
            "name", "Charlie"
        ));

        // Try an invalid insert to demonstrate constraint enforcement
        try {
            database.insert("users", Map.of(
                "id", 100, // too small
                "name", "ThisNameIsWayTooLongToStore"
            ));
        } catch (ConstraintViolationException ex) {
            System.out.println("Blocked invalid insert: " + ex.getMessage());
        }

        System.out.println("\nAll records in 'users':");
        printRows(database.fetchAll("users"));

        System.out.println("\nFilter by name = 'Alice':");
        printRows(database.filter("users", "name", "Alice"));

        // Update schema - add a new optional column
        TableSchema updatedSchema = TableSchema.builder("users")
            .addColumn(ColumnDefinition.intColumn("id")
                .required()
                .withMinValue(1024)
                .build())
            .addColumn(ColumnDefinition.stringColumn("name")
                .required()
                .withMaxLength(20)
                .build())
            .addColumn(ColumnDefinition.stringColumn("email")
                .optional()
                .withMaxLength(20)
                .build())
            .addColumn(ColumnDefinition.stringColumn("status")
                .optional()
                .withMaxLength(20)
                .build())
            .build();

        System.out.println("\nUpdating schema to add 'status' column ...");
        database.updateTable("users", updatedSchema);

        database.insert("users", Map.of(
            "id", 8192,
            "name", "Daisy",
            "email", "daisy@flower.com",
            "status", "ACTIVE"
        ));

        System.out.println("\nAll records after schema update:");
        printRows(database.fetchAll("users"));

        System.out.println("\nDeleting table 'users' ...");
        database.deleteTable("users");
        System.out.println("Demo complete.");
    }

    private static void printRows(List<TableRow> rows) {
        if (rows.isEmpty()) {
            System.out.println("  (no rows)");
            return;
        }
        for (TableRow row : rows) {
            System.out.println("  " + row);
        }
    }
}

