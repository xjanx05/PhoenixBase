package de.codingphoenix.phoenixbase.database.request.tablealter.action;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.DataType;
import de.codingphoenix.phoenixbase.database.request.tablealter.TableAlterRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class TableAlterModifyTypeAction implements TableAlterRequest.AlterRequestAction {
    private DataType dataType;
    private Object dataTypeObject;
    private String columnName;

    @Override
    public StringBuilder generateSQL() {
        Checks.checkIfNullOrEmptyMap(dataType, "dataType");
        Checks.checkIfNullOrEmptyMap(columnName, "columnName");

        return new StringBuilder("MODIFY COLUMN ").append(columnName).append(" ").append(dataType.toSQL(dataTypeObject));
    }

    //    ALTER TABLE tabellenname MODIFY COLUMN spaltenname NEUER_DATENTYP;

}
