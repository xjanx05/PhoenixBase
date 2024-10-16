package de.codingphoenix;

import de.codingphoenix.phoenixbase.database.DatabaseAdapter;
import de.codingphoenix.phoenixbase.database.request.SelectRequest;

public class Main {
    public static void main(String[] args) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter.Builder()
                .driverType(DatabaseAdapter.DriverType.MARIADB)
                .host("localhost")
                .port(3306)
                .database("atirion")
                .user("root")
                .password("root")
                .build();

        databaseAdapter.connect();

        SelectRequest selectRequest = new SelectRequest();
        selectRequest
                .table("player")
                .condition("uuid", "0e3749e5-4bc7-4c42-b026-35ee38c900ff")
                .condition("firstJoin", "1722943846322")
                .columKey("*")
                .databaseAction(resultSet -> {
                    System.out.println("The Result is: ");
                    if (resultSet.next()) {
                        System.out.println(resultSet.getString("playTime"));
                    } else {
                        System.out.println("No Result found");
                    }
                    return null;
                })
                .async(false);
        ;

        databaseAdapter.executeRequest(selectRequest);
    }
}