package de.codingphoenix.phoenixbase.database.request.tablealter.action;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.request.tablealter.TableAlterRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class TableAlterAddAttributeAction implements TableAlterRequest.AlterRequestAction {
    private String columnName;
    private AttributeType attributeType;

    @Override
    public StringBuilder generateSQL() {
        Checks.checkIfNullOrEmptyMap(columnName, "columnName");
        Checks.checkIfNullOrEmptyMap(attributeType, "attributeType");
        return new StringBuilder("ADD ").append(attributeType.name().replaceAll("_", " ")).append(" (").append(columnName).append(")");
    }

//    ALTER TABLE tabellenname ADD UNIQUE (spaltenname);
//    ALTER TABLE tabellenname ADD PRIMARY KEY (spaltenname);

    public enum AttributeType {
        UNIQUE, PRIMARY_KEY
    }
}
