package de.codingphoenix.phoenixbase.database.request;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.Column;
import de.codingphoenix.phoenixbase.database.DataType;
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
public class TableCreateRequest extends DatabaseRequest {
    @Setter
    private String table;

    @Setter
    private boolean ifNotExists;

    private List<Column> columns;

    public TableCreateRequest column(Column column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(column);
        return this;
    }

    public TableCreateRequest column(String key, DataType dataType) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(new Column(key, dataType));
        return this;
    }


    @Override
    public String generateSQLString() {
        Checks.checkIfNullOrEmptyMap(table, "tablename");
        Checks.checkIfNullOrEmptyMap(columns, "columns");
        StringBuilder sql = new StringBuilder("CREATE TABLE ");

        if (ifNotExists)
            sql.append("IF NOT EXISTS ");

        sql.append(table);


        StringBuilder columnString = null;
        for (Column column : columns) {
            if (columnString == null) {
                columnString = new StringBuilder("(").append(column.toString());
            } else {
                columnString.append(", ").append(column.toString());
            }
        }
        columnString.append(")");
        sql.append(columnString).append(";");

        if (Checks.DEBUG)
            System.out.println("Executing: " + sql);

        return sql.toString();
    }

}
