package de.codingphoenix.phoenixbase.database.request.tablealter.action;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.DataType;
import de.codingphoenix.phoenixbase.database.request.tablealter.TableAlterRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 Action for the {@link TableAlterRequest} that will add a column.
 */
@Setter
@Getter
@Accessors(fluent = true)
public class TableAlterAddColumnAction implements TableAlterRequest.AlterRequestAction {
    /**
     * The name of the column the attribute should be added.
     */
    private String columnName;
    /**
     * The Type of the data.
     */
    private DataType dataType;
    /**
     * The position of the column.
     */
    private Postion postion = Postion.DEFAULT;
    /**
     * If {@link Postion} is set to {@linkplain Postion.AFTER} the name of the column the new will be added after.
     */
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

    /**
     * The type of position where the column will be added.
     */
    public enum Postion {
        DEFAULT, FIRST, AFTER
    }

//    ALTER TABLE tabellenname ADD COLUMN neue_spalte DATENTYP;
//    ALTER TABLE tabellenname ADD COLUMN neue_spalte DATENTYP FIRST;
//    ALTER TABLE tabellenname ADD COLUMN neue_spalte DATENTYP AFTER bestehende_spalte;
}
