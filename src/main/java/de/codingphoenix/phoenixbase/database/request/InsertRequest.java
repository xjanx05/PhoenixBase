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

@Getter
@Accessors(fluent = true)
public class InsertRequest extends DatabaseRequest {
    @Setter
    private String table;
    private List<DatabaseEntry> entries;
    @Setter
    private InsertMethode insertMethode = InsertMethode.INSERT;


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


    private StringBuilder generateOnDuplicateString() {
        StringBuilder sql = new StringBuilder(" ON DUPLICATE KEY UPDATE ");

        StringBuilder updateString = null;
        for (DatabaseEntry entry : entries) {
            if (updateString == null) {
                updateString = new StringBuilder(entry.columName()).append(" = ").append(entry.sqlValue());
            } else {
                updateString.append(", ").append(entry.columName()).append(" = ").append(entry.sqlValue());
            }
        }
        return sql.append(updateString);
    }

    public enum InsertMethode {
        INSERT, INSERT_OR_UPDATE, INSERT_IGNORE
    }
}
