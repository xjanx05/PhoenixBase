package de.codingphoenix;

import de.codingphoenix.phoenixbase.database.DatabaseAdapter;


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
