package de.codingphoenix;

import de.codingphoenix.phoenixbase.database.Column;
import de.codingphoenix.phoenixbase.database.DataType;
import de.codingphoenix.phoenixbase.database.DatabaseAdapter;
import de.codingphoenix.phoenixbase.database.request.DatabaseRequest;
import de.codingphoenix.phoenixbase.database.request.TableCreateRequest;


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

        DatabaseRequest tableCreateRequest = new TableCreateRequest()
                .table("testing")
                .ifNotExists(true)
                .column(new Column().key("uuid").dataType(DataType.VARCHAR).dataTypeParamenterObject(64).columnType(Column.ColumnType.PRIMARY_KEY))
                .column(new Column().key("playerName").dataType(DataType.VARCHAR).dataTypeParamenterObject(16).columnType(Column.ColumnType.UNIQUE))
                .column(new Column().key("boolean").dataType(DataType.BOOLEAN).notNull(true))
                .async(false);


        databaseAdapter.executeRequest(tableCreateRequest);

        //FINISHED: DELETE, SELECT, INSERT, TABLE DROP, TABLE CREATE
    }
}