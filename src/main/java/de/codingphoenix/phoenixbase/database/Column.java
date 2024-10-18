package de.codingphoenix.phoenixbase.database;

import de.codingphoenix.phoenixbase.check.Checks;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    private String key;
    private DataType dataType;
    private Object dataTypeParamenterObject;
    private ColumnType columnType;

    private Object defaultValue;
    private boolean notNull;

    public Column(String key, DataType dataType) {
        this.key = key;
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        Checks.checkIfNull(key, "columnkey");
        Checks.checkIfNull(dataType, "datatype");
        StringBuilder column = new StringBuilder(key).append(" ").append(dataType.toString() + (dataType.canHaveObject() && dataTypeParamenterObject != null ? "(" + dataTypeParamenterObject.toString() + ")" : ""));

        if (notNull) {
            column.append(" NOT NULL");
        }

        if (columnType != null) {
            if (columnType.equals(ColumnType.PRIMARY_KEY_AUTOINCREMENT)) {
                if (!dataType.equals(DataType.TINYTEXT) && !dataType.equals(DataType.INT) && !dataType.equals(DataType.BIGINT)) {
                    System.out.println("ERROR: You cannot set an autoincrement to a non int value. Setting it to default primary key.");
                    columnType = ColumnType.PRIMARY_KEY;
                }
            }
            column.append(" ").append(columnType.name().replaceAll("_", " "));
        }

        if (defaultValue != null) {
            column.append(" DEFAULT ").append(defaultValue.toString());

        }

        return column.toString();
    }

    public enum ColumnType {
        PRIMARY_KEY, PRIMARY_KEY_AUTOINCREMENT, UNIQUE;
    }




}
