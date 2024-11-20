package de.codingphoenix.phoenixbase.database.request;

import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.Condition;
import de.codingphoenix.phoenixbase.database.DatabaseAction;
import de.codingphoenix.phoenixbase.database.Limit;
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
/**
 * A {@link DatabaseRequest} that selects certain values from a table.
 */
public class SelectRequest extends DatabaseRequest {

    /**
     * The name of the table that should be deleted.
     */
    @Setter
    private String table;

    /**
     * The methode to which the result of the query will be parsed to.
     */
    @Setter
    private DatabaseAction databaseAction;

    /**
     * The ResultSet will be stored here after the request is executed.
     */
    @Setter
    private ResultSet resultSet;

    /**
     * The function for the select request. E.g. the amount of rows that will match the conditions.
     */
    @Setter
    private SelectFunction selectFunction = SelectFunction.NORMAL;

    /**
     * The {@link Order} of the table when the deleting will occur.
     */
    private Order order;
    /**
     * A list of conditions that must be matched in order to delete a row.
     */
    private Set<Condition> conditions;
    /**
     * The key for the values that you want from the table.
     */
    private Set<String> columKey;
    /**
     * The maximum of rows deleted by this request.
     */
    private Limit limit;

    /**
     * Sets the limit of rows that should be selected by this request.
     *
     * @param limit The maximum rows that can be deleted.
     * @return {@link SelectRequest} for chaining.
     */
    public SelectRequest limit(int limit) {
        if (this.limit == null) {
            this.limit = new Limit();
        }
        this.limit.limit(limit);
        return this;
    }

    /**
     * Sets the {@link Order} of the table when the selection will occur
     *
     * @param key       The key of the column the order will be assigned on.
     * @param direction The direction of the sorting.
     * @return {@link SelectRequest} for chaining.
     */
    public SelectRequest order(String key, Order.Direction direction) {
        if (order == null)
            order = new Order();
        order.orderRule(key, direction);
        return this;
    }

    /**
     * Adds and condition that a row must match in order to get selected.
     *
     * @param condition The condition
     * @return {@link SelectRequest} for chaining.
     */
    public SelectRequest condition(Condition condition) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(condition);
        return this;
    }

    /**
     * Adds and condition that a row must match in order to get selected. By column key and value.
     *
     * @param key   The key of the column for the condition
     * @param value The value of the row that must match with the given row key
     * @return {@link SelectRequest} for chaining.
     */
    public SelectRequest condition(String key, Object value) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(new Condition(key, value));
        return this;
    }

    /**
     * Adds and condition that a row must match the operator in order to get selected. By column key, value and operator.
     *
     * @param key      The key of the column for the condition
     * @param value    The value of the row that must match with the given row key
     * @param operator The {@link de.codingphoenix.phoenixbase.database.Condition.Operator} of the request. E.g. if they need to match or be less than, ...
     * @return {@link SelectRequest} for chaining.
     */
    public SelectRequest condition(String key, Object value, Condition.Operator operator) {
        if (conditions == null) {
            conditions = new HashSet<>();
        }
        conditions.add(new Condition(key, value, operator));
        return this;
    }

    /**
     * Adds a key for the values that you want from the table
     * @param columKey The key of the column
     * @return {@link SelectRequest} for chaining.
     */
    public SelectRequest columKey(String columKey) {
        if (this.columKey == null) {
            this.columKey = new HashSet<>();
        }
        this.columKey.add(columKey);
        return this;
    }

    @Override
    public String generateSQLString() {
        Checks.checkIfNullOrEmptyMap(table, "tablename");
        Checks.checkIfNullOrEmptyMap(columKey, "columkey");


        StringBuilder sql = new StringBuilder("SELECT ");

        if (!selectFunction.equals(SelectFunction.NORMAL) && !isStarRequest()) {
            sql.append(selectFunction.function()).append("(").append(parseColumnName()).append(")");
        } else {
            sql.append(parseColumnName());
        }

        sql.append(" FROM ").append(table);

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
     * Checks if the request is a star request
     *
     * @return the result of the check
     */
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

    /**
     * Parsed the column names to a string for executing
     *
     * @return the sql string
     */
    private String parseColumnName() {
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

    public enum SelectFunction {
        /**
         * The default function. This will return the results directly.
         */
        NORMAL(""),
        /**
         * Counts the amount of rows that returns from the SELECT statement.
         */
        COUNT("COUNT"),
        /**
         * Calculates the average value of a numerical dataset that returns from the SELECT statement.
         */
        AVERAGE("AVG"),
        /**
         * Calculates the sum value of a numerical dataset that returns from the SELECT statement.
         */
        SUM("SUM");

        /**
         * The sql key for the function
         */
        @Getter
        @Accessors(fluent = true)
        private final String function;

        SelectFunction(String funktion) {
            this.function = funktion;
        }
    }
}
