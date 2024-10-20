package de.codingphoenix.phoenixbase.database.request.tablealter.action;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.request.tablealter.TableAlterRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Setter
@Getter
@Accessors(fluent = true)
public class TableAlterRenameAction implements TableAlterRequest.AlterRequestAction {
    private String newTableName;


    @Override
    public StringBuilder generateSQL() {
        Checks.checkIfNullOrEmptyMap(newTableName, "newTableName");
        return new StringBuilder("RENAME TO ").append(newTableName);
    }

    //    ALTER TABLE tabellenname RENAME TO neuer_tabellenname;

}
