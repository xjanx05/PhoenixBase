package de.codingphoenix.phoenixbase.database.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.SQLException;

@Setter
@Getter
@Accessors(fluent = true, chain = true)
public abstract class DatabaseRequest {

    private boolean async = false;

    public abstract Object execute(Connection connection) throws SQLException;

}
