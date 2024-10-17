package de.codingphoenix.phoenixbase.database.request;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.Condition;
import de.codingphoenix.phoenixbase.database.DatabaseAction;
import de.codingphoenix.phoenixbase.database.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public class SelectRequest extends DatabaseRequest {
    @Setter
    private DatabaseAction databaseAction;
    @Setter
    private Order order;
    @Setter
    private String table;
    private Set<Condition> conditions;
    private Set<String> columKey;
    @Setter
    private SelectFunction selectFunction = SelectFunction.NORMAL;

    public SelectRequest condition(Condition condition) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(condition);
        return this;
    }

    public SelectRequest order(String key, Order.Direction direction) {
        if (order == null)
            order = new Order();
        order.orderRule(key, direction);
        return this;
    }

    public SelectRequest condition(String key, Object value) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(new Condition(key, value));
        return this;
    }

    public SelectRequest columKey(String columKey) {
        if (this.columKey == null) {
            this.columKey = new HashSet<>();
        }
        this.columKey.add(columKey);
        return this;
    }

    @Override
    public Object execute(Connection connection) throws SQLException {
        Checks.checkIfNullOrEmptyMap(databaseAction, "action");
        Checks.checkIfNullOrEmptyMap(table, "tablename");
        Checks.checkIfNullOrEmptyMap(columKey, "columkey");

        String sql = "SELECT " + (!selectFunction.equals(SelectFunction.NORMAL) && !isStarRequest() ?  selectFunction.function() + "(" + parseColumName() + ")" : parseColumName()) + " FROM " + table + (conditions != null && !conditions.isEmpty() ? " WHERE " + parseCondition() : "") + (order != null ? order.toString() : "");

        if (Checks.DEBUG)
            System.out.println("Executing: " + sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return databaseAction.databaseAction(resultSet);
    }

    private boolean isStarRequest() {
        if (columKey == null || columKey.size() != 1)
            return false;
        if (!columKey.toArray(new String[]{})[0].equals("*")) {
            return false;
        }
        if (Checks.DEBUG)
            System.out.println("The request was a star request");
        return true;
    }

    private String parseColumName() {
        if (columKey.isEmpty())
            return "";
        if (columKey.size() == 1)
            return columKey.toArray(new String[]{})[0];
        StringBuilder parsedName = null;
        for (String name : columKey) {
            if (parsedName == null) {
                parsedName = new StringBuilder(name);
                continue;
            }
            parsedName.append(", ").append(name);
        }
        return parsedName.toString();
    }

    private String parseCondition() {
        if (conditions.isEmpty())
            return "";
        if (conditions.size() == 1)
            return columKey.toArray(new String[]{})[0];
        StringBuilder parsedCondition = null;
        for (Condition condition : conditions) {
            if (parsedCondition == null) {
                parsedCondition = new StringBuilder(condition.not() ? " NOT " : "").append(condition.toString());
                continue;
            }
            parsedCondition.append(condition.type().equals(Condition.Type.AND) ? " AND " : " OR ").append(condition.toString());
        }
        return parsedCondition.toString();
    }

    public enum SelectFunction {
        NORMAL(""), COUNT("COUNT"), AVERAGE("AVG"), SUM("SUM");

        @Getter
        @Accessors(fluent = true)
        private final String function;

        SelectFunction(String funktion) {
            this.function = funktion;
        }
    }
}
