package de.codingphoenix;

import de.codingphoenix.phoenixbase.database.DataType;
import de.codingphoenix.phoenixbase.database.DatabaseAdapter;
import de.codingphoenix.phoenixbase.database.request.DatabaseRequest;
import de.codingphoenix.phoenixbase.database.request.tablealter.TableAlterRequest;
import de.codingphoenix.phoenixbase.database.request.tablealter.action.TableAlterModifyTypeAction;
import de.codingphoenix.phoenixbase.database.request.tablealter.action.TableAlterRenameAction;


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


        DatabaseRequest request = new TableAlterRequest()
                .table("testing")
                .alterRequestAction(new TableAlterModifyTypeAction().columnName("boolean").dataType(DataType.BOOLEAN))
                .async(false);

        databaseAdapter.executeRequest(request);

        //FINISHED: DELETE, INSERT, SELECT, TABLE CREATE, TABLE DROP, UPDATE
    }
}
