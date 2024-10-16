package de.codingphoenix.phoenixbase.database.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseRequest {

    @Getter
    @Accessors(fluent = true, chain = true)
    @Setter
    private boolean async;

    public abstract Object execute(Connection connection) throws SQLException;

}
