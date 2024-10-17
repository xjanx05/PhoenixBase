package de.codingphoenix;

import de.codingphoenix.phoenixbase.database.DatabaseAdapter;
import de.codingphoenix.phoenixbase.database.Order;
import de.codingphoenix.phoenixbase.database.request.DatabaseRequest;
import de.codingphoenix.phoenixbase.database.request.DeleteRequest;
import de.codingphoenix.phoenixbase.database.request.InsertRequest;
import de.codingphoenix.phoenixbase.database.request.SelectRequest;

import java.util.UUID;

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


        DatabaseRequest insertRequest = new InsertRequest()
                .table("player")
                .entry("uuid", "c2330a58-126d-4fd3-8b7c-5f8a3577da9b")
                .entry("playtime", 42)
                .insertMethode(InsertRequest.InsertMethode.INSERT_IGNORE)
                .async(false);


        DatabaseRequest selectRequest = new SelectRequest()
                .table("player")
                .columKey("*")
                .order("firstJoin", Order.Direction.DESCENDING)
                .databaseAction(resultSet -> {
                    System.out.println("The Result is: ");
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(1));
                    }
                    return null;
                })
                .async(false);

        DatabaseRequest deleteRequest = new DeleteRequest()
                .table("player")
                .condition("uuid", "77031033-c9f0-4ed2-b8f6-8460bbc41daa")
                .async(false);
        ;

        databaseAdapter.executeRequest(insertRequest);
        databaseAdapter.executeRequest(selectRequest);
        databaseAdapter.executeRequest(deleteRequest);
        databaseAdapter.executeRequest(selectRequest);
    }
}