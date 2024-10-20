package de.codingphoenix.phoenixbase.database.request.tablealter.action;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.request.tablealter.TableAlterRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class TableAlterDropAction implements TableAlterRequest.AlterRequestAction {


    private int dropType = Integer.MIN_VALUE;
    private String dropObjectName;

    @Override
    public StringBuilder generateSQL() {
        Checks.checkIfIntMinValue(dropType, "dropType");
        if (dropType == PRIMARY_KEY_DROP_TYPE) {
            return new StringBuilder("DROP PRIMARY KEY");
        }
        Checks.checkIfNullOrEmptyMap(dropObjectName, "dropObjectName");
        if (dropType != 1 && dropType != 2) {
            throw new IllegalArgumentException("Drop type not found.");
        }
        return new StringBuilder("DROP ").append((dropType == COLUMN_DROP_TYPE ? "COLUMN " : "INDEX ")).append(dropObjectName);
    }

    public static final int COLUMN_DROP_TYPE = 1;
    public static final int INDEX_DROP_TYPE = 2;
    public static final int PRIMARY_KEY_DROP_TYPE = 3;


    //    ALTER TABLE tabellenname DROP COLUMN spaltenname;
//    ALTER TABLE tabellenname DROP INDEX indexname;
//    ALTER TABLE tabellenname DROP PRIMARY KEY;  -> Der Primärschlüssel (Key) wird entfernt, aber die Spalten und ihre Werte bleiben erhalten. Nur die einzigartige und referentielle Einschränkung, die der Primärschlüssel bereitgestellt hat, geht verloren. z.b. autoincrement


}
