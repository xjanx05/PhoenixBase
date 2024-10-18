package de.codingphoenix;

import de.codingphoenix.phoenixbase.database.Column;
import de.codingphoenix.phoenixbase.database.Condition;
import de.codingphoenix.phoenixbase.database.DataType;
import de.codingphoenix.phoenixbase.database.DatabaseAdapter;
import de.codingphoenix.phoenixbase.database.request.DatabaseRequest;
import de.codingphoenix.phoenixbase.database.request.TableCreateRequest;
import de.codingphoenix.phoenixbase.database.request.UpdateRequest;


public class Main {
    public static void main(String[] args) {

        DatabaseAdapter databaseAdapter = new DatabaseAdapter.Builder()
                .driverType(DatabaseAdapter.DriverType.MARIADB)
                .host("localhost")
                .port(3306)
                .database("atirion")
                .user("root")
                .password("root")
                .build()
                .connect();

        //databaseAdapter.executeRequest();

        //FINISHED: DELETE, INSERT, SELECT, TABLE CREATE, TABLE DROP, UPDATE
    }
}
