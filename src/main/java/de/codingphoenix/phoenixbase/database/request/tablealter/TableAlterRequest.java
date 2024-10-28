package de.codingphoenix.phoenixbase.database.request.tablealter;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.request.DatabaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A {@link DatabaseRequest} that will modify the table or its columns.
 */
@Setter
@Getter
@Accessors(fluent = true)
public class TableAlterRequest extends DatabaseRequest {

    /**
     * The name of the table that will be modified.
     */
    private String table;

    /**
     * The action of the request.
     */
    private AlterRequestAction alterRequestAction;

    @Override
    public String generateSQLString()  {
        Checks.checkIfNullOrEmptyMap(table, "tablename");
        Checks.checkIfNullOrEmptyMap(alterRequestAction, "alterRequestAction");

        StringBuilder sql = new StringBuilder("ALTER TABLE ").append(table).append(" ");

        sql.append(alterRequestAction.generateSQL());

        sql.append(";");

        if (Checks.DEBUG)
            System.out.println("Executing: " + sql);

        return sql.toString();
    }

    /**
     * The interface of an Action for the {@link TableAlterRequest}.
     */
    public interface AlterRequestAction {

        StringBuilder generateSQL();

    }

}
