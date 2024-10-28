package de.codingphoenix.phoenixbase.database.request;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.Condition;
import de.codingphoenix.phoenixbase.database.Limit;
import de.codingphoenix.phoenixbase.database.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


/**
 * A {@link DatabaseRequest} that requests a deletion of a table
 */
@Getter
@Accessors(fluent = true)
public class DeleteRequest extends DatabaseRequest {
    /**
     * The name of the table that should be deleted.
     */
    @Setter
    private String table;
    /**
     * The {@link Order} of the table when the deleting will occur.
     */
    private Order order;
    /**
     * A list of conditions that must be matched in order to delete a row.
     */
    private Set<Condition> conditions;
    /**
     * The maximum of rows deleted by this request.
     */
    private Limit limit;

    /**
     * Sets the limit of rows that can be deleted by this request.
     *
     * @param limit The maximum rows that can be deleted.
     * @return {@link DeleteRequest} for chaining.
     */
    public DeleteRequest limit(int limit) {
        if (this.limit == null) {
            this.limit = new Limit();
        }
        this.limit.limit(limit);
        return this;
    }

    /**
     * Adds and condition that a row must match in order to get deleted.
     *
     * @param condition The condition
     * @return {@link DeleteRequest} for chaining.
     */
    public DeleteRequest condition(Condition condition) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(condition);
        return this;
    }

    /**
     * Adds and condition that a row must match in order to get deleted. By column key and value.
     *
     * @param key   The key of the column for the condition
     * @param value The value of the row that must match with the given row key
     * @return {@link DeleteRequest} for chaining.
     */
    public DeleteRequest condition(String key, Object value) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(new Condition(key, value));
        return this;
    }

    /**
     * Adds and condition that a row must match the operator in order to get deleted. By column key, value and operator.
     *
     * @param key      The key of the column for the condition
     * @param value    The value of the row that must match with the given row key
     * @param operator The {@link de.codingphoenix.phoenixbase.database.Condition.Operator} of the request. E.g. if they need to match or be less than, ...
     * @return {@link DeleteRequest} for chaining.
     */
    public DeleteRequest condition(String key, Object value, Condition.Operator operator) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(new Condition(key, value, operator));
        return this;
    }

    /**
     * Sets the {@link Order} of the table when the deleting will occur
     *
     * @param key       The key of the column the order will be assigned on.
     * @param direction The direction of the sorting.
     * @return {@link DeleteRequest} for chaining.
     */
    public DeleteRequest order(String key, Order.Direction direction) {
        if (order == null)
            order = new Order();
        order.orderRule(key, direction);
        return this;
    }

    @Override
    public String generateSQLString(){
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

        return sql.toString();
    }

    /**
     * Parsed the conditions to a string for executing
     *
     * @return the sql string
     */
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
