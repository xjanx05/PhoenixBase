package de.codingphoenix.phoenixbase.database.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.SQLException;

/**
 * The base database request.
 */
@Setter
@Getter
@Accessors(fluent = true, chain = true)
public abstract class DatabaseRequest {
    /**
     * Defines if a request get executed synchron or asynchron
     */
    private boolean async = false;

    /**
     * Gets the status of the current request.
     */
    private boolean finished = false;

    /**
     * The methode for executing the request.
     *
     * @param connection The connection to the desired database.
     * @throws SQLException If an error occur while executing the request.
     */
    public abstract String generateSQLString() throws SQLException;

}
