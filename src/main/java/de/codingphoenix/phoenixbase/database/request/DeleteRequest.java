package de.codingphoenix.phoenixbase.database.request;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.Condition;
import de.codingphoenix.phoenixbase.database.Limit;
import de.codingphoenix.phoenixbase.database.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public class DeleteRequest extends DatabaseRequest {
    @Setter
    private String table;
    private Order order;
    private Set<Condition> conditions;
    private Limit limit;

    public DeleteRequest limit(int limit) {
        if (this.limit == null) {
            this.limit = new Limit();
        }
        this.limit.limit(limit);
        return this;
    }

    public DeleteRequest condition(Condition condition) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(condition);
        return this;
    }

    public DeleteRequest order(String key, Order.Direction direction) {
        if (order == null)
            order = new Order();
        order.orderRule(key, direction);
        return this;
    }

    public DeleteRequest condition(String key, Object value) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(new Condition(key, value));
        return this;
    }

    public DeleteRequest condition(String key, Object value, Condition.Operator operator) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(new Condition(key, value, operator));
        return this;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        Checks.checkIfNullOrEmptyMap(table, "tablename");

        StringBuilder sql = new StringBuilder("DELETE FROM ").append(table);


        if (conditions != null && !conditions.isEmpty())
            sql.append(" WHERE ").append(parseCondition());

        if (order != null)
            sql.append(order);

        if (limit != null)
            sql.append(limit);

        sql.append(";");
        if (Checks.DEBUG)
            System.out.println("Executing: " + sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        preparedStatement.execute();
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
