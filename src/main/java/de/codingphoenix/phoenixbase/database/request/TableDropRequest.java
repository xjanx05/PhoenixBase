package de.codingphoenix.phoenixbase.database.request;

import de.codingphoenix.phoenixbase.check.Checks;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
@Accessors(fluent = true)
public class TableDropRequest extends DatabaseRequest {
    @Setter
    private String table;

    @Override
    public void execute(Connection connection) throws SQLException {
        Checks.checkIfNullOrEmptyMap(table, "tablename");

        StringBuilder sql = new StringBuilder("DROP TABLE").append(table).append(";");

        if (Checks.DEBUG)
            System.out.println("Executing: " + sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        preparedStatement.execute();
    }
}
