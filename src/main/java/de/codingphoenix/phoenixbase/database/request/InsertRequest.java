package de.codingphoenix.phoenixbase.database.request;


import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.DatabaseEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link DatabaseRequest} that requests an insert of a value in a table
 */
@Getter
@Accessors(fluent = true)
public class InsertRequest extends DatabaseRequest {

    /**
     * The name of the table that should be deleted.
     */
    @Setter
    private String table;

    /**
     * The entries which will be written to the Database.
     */
    private List<DatabaseEntry> entries;

    /**
     * Method which will be used for inserting the value.
     */
    @Setter
    private InsertMethode insertMethode = InsertMethode.INSERT;

    /**
     * Adds an entry which will be written to the Database.
     * @param column Name of the column
     * @param value Value that will be inserted
     * @return {@link InsertRequest} for chaining.
     */
    public InsertRequest entry(String column, Object value) {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        entries.add(new DatabaseEntry(column, value));
        return this;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        Checks.checkIfNullOrEmptyMap(entries, "entries");
        Checks.checkIfNullOrEmptyMap(table, "tablename");

        StringBuilder sql = new StringBuilder("INSERT ");

        if (insertMethode.equals(InsertMethode.INSERT_IGNORE))
            sql.append("IGNORE ");

        sql.append("INTO ").append(table);

        StringBuilder columString = null;
        StringBuilder objectString = null;
        for (DatabaseEntry entry : entries) {
            if (columString == null) {
                columString = new StringBuilder(entry.columName());
            } else {
                columString.append(", ").append(entry.columName());
            }
            if (objectString == null) {
                objectString = new StringBuilder(entry.sqlValue());
            } else {
                objectString.append(", ").append(entry.sqlValue());
            }
        }
        sql.append("(").append(columString).append(") VALUES (").append(objectString).append(")");

        if (insertMethode.equals(InsertMethode.INSERT_OR_UPDATE))
            sql.append(generateOnDuplicateString());

        sql.append(";");
        if (Checks.DEBUG)
            System.out.println("Executing: " + sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        preparedStatement.execute();
    }

    /**
     * Generates the sql string for insert with duplicated key for executing
     * @return the sql string
     */
    private StringBuilder generateOnDuplicateString() {
        StringBuilder sql = new StringBuilder(" ON DUPLICATE KEY UPDATE ");

        StringBuilder insertString = null;
        for (DatabaseEntry entry : entries) {
            if (insertString == null) {
                insertString = new StringBuilder(entry.columName()).append(" = ").append(entry.sqlValue());
            } else {
                insertString.append(", ").append(entry.columName()).append(" = ").append(entry.sqlValue());
            }
        }
        return sql.append(insertString);
    }

    /**
     * The methode of insert.
     */
    public enum InsertMethode {
        INSERT, INSERT_OR_UPDATE, INSERT_IGNORE
    }
}
