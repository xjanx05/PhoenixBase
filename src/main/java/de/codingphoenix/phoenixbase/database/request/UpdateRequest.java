package de.codingphoenix.phoenixbase.database.request;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.Condition;
import de.codingphoenix.phoenixbase.database.DatabaseEntry;
import de.codingphoenix.phoenixbase.database.Limit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public class UpdateRequest extends DatabaseRequest {
    @Setter
    private String table;
    @Setter
    private UpdatePriority updatePriority = UpdatePriority.NORMAL;
    @Setter
    private boolean updateIgnore = false;
    private List<DatabaseEntry> entries;
    private Set<Condition> conditions;
    private Limit limit;


    public UpdateRequest limit(int limit) {
        if (this.limit == null) {
            this.limit = new Limit();
        }
        this.limit.limit(limit);
        return this;
    }

    public UpdateRequest condition(Condition condition) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(condition);
        return this;
    }

    public UpdateRequest entry(String column, Object value) {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        entries.add(new DatabaseEntry(column, value));
        return this;
    }

    @Override
    public String generateSQLString() {
        Checks.checkIfNullOrEmptyMap(table, "tablename");


        StringBuilder sql = new StringBuilder("UPDATE");

        if (updatePriority == UpdatePriority.LOW)
            sql.append(" LOW_PRIORITY");

        if (updateIgnore)
            sql.append(" IGNORE");

        sql.append(" ").append(table).append(" SET ");

        StringBuilder updateString = null;
        for (DatabaseEntry entry : entries) {
            if (updateString == null) {
                updateString = new StringBuilder(entry.columName()).append("=").append(entry.sqlValue());
            } else {
                updateString.append(", ").append(entry.columName()).append("=").append(entry.sqlValue());
            }
        }
        sql.append(" ").append(updateString);

        if (conditions != null && !conditions.isEmpty())
            sql.append(" WHERE ").append(parseCondition());

        if (limit != null)
            sql.append(limit);

        sql.append(";");

        if (Checks.DEBUG)
            System.out.println("Executing: " + sql);

        return sql.toString();
    }

    public enum UpdatePriority {
        LOW, NORMAL
    }

    private String parseCondition() {
        if (conditions.isEmpty())
            return "";
        if (conditions.size() == 1)
            return conditions.toArray(new Condition[]{})[0].toString();
        StringBuilder parsedCondition = null;
        for (Condition condition : conditions) {
            if (parsedCondition == null) {
                parsedCondition = new StringBuilder(condition.not() ? " NOT " : "").append(condition);
                continue;
            }
            parsedCondition.append(condition.type().equals(Condition.Type.AND) ? " AND " : " OR ").append(condition);
        }
        return parsedCondition.toString();
    }

}
