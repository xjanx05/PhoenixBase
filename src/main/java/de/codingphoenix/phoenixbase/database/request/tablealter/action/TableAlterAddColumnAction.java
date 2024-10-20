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
public class TableAlterAddColumnAction implements TableAlterRequest.AlterRequestAction {
    private String columnName;
    private DataType dataType;
    private Postion postion = Postion.DEFAULT;
    private String afterColumnName;

    @Override
    public StringBuilder generateSQL() {
        Checks.checkIfNullOrEmptyMap(columnName, "columnName");
        Checks.checkIfNullOrEmptyMap(dataType, "dataType");


        StringBuilder stringBuilder = new StringBuilder("ADD COLUMN ").append(columnName).append(" ").append(dataType);

        if (postion == Postion.DEFAULT) {
            return stringBuilder;
        } else if (postion == Postion.FIRST) {
            stringBuilder.append(" FIRST");
        } else if (postion == Postion.AFTER) {
            stringBuilder.append(" AFTER ").append(afterColumnName);
        }

        return stringBuilder;
    }


    public enum Postion {
        DEFAULT, FIRST, AFTER
    }

//    ALTER TABLE tabellenname ADD COLUMN neue_spalte DATENTYP;
//    ALTER TABLE tabellenname ADD COLUMN neue_spalte DATENTYP FIRST;
//    ALTER TABLE tabellenname ADD COLUMN neue_spalte DATENTYP AFTER bestehende_spalte;
}
