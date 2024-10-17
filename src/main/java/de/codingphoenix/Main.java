package de.codingphoenix;

import de.codingphoenix.phoenixbase.database.DatabaseAdapter;
import de.codingphoenix.phoenixbase.database.Order;
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
                .columKey("*")
                .databaseAction(resultSet -> {
                    System.out.println("The Result is: ");
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(1));
                    }
                    return null;
                })
                .order("playTime", Order.Direction.DESCENDING)
                .selectFunction(SelectRequest.SelectFunction.AVERAGE)
                .async(false);
        ;

        databaseAdapter.executeRequest(selectRequest);
    }
}