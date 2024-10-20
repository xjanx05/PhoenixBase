package de.codingphoenix.phoenixbase.database.request.tablealter;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.request.DatabaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
@Accessors(fluent = true)
public class TableAlterRequest extends DatabaseRequest {

    @Setter
    private String table;
    @Setter
    private AlterRequestAction alterRequestAction;

    @Override
    public void execute(Connection connection) throws SQLException {
        Checks.checkIfNullOrEmptyMap(table, "tablename");
        Checks.checkIfNullOrEmptyMap(alterRequestAction, "alterRequestAction");

        StringBuilder sql = new StringBuilder("ALTER TABLE ").append(table).append(" ");

        sql.append(alterRequestAction.generateSQL());

        sql.append(";");

        if (Checks.DEBUG)
            System.out.println("Executing: " + sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        preparedStatement.execute();
    }

    public interface AlterRequestAction {

        StringBuilder generateSQL();

    }

}
