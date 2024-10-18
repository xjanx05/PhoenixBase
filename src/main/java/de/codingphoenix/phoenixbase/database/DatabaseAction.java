package de.codingphoenix.phoenixbase.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseAction {

    void databaseAction(ResultSet resultSet) throws SQLException;

}
