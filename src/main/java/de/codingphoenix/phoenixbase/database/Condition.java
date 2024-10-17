package de.codingphoenix.phoenixbase.database;

import de.codingphoenix.phoenixbase.check.Checks;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class Condition {
    private String key;
    private Object value;
    private Type type = Type.AND;
    private Operator operator = Operator.EQUALS;
    private boolean not = false;

    public Condition(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Condition(String key, Object value, Operator operator) {
        this.key = key;
        this.value = value;
        this.operator = operator;
    }

    @Override
    public String toString() {
        Checks.checkIfNull(key, "key");
        Checks.checkIfNull(value, "value");

        if (operator == Operator.EQUALS)
            return key + "='" + value + "'";
        Checks.checkIfHasSpaces(value, "value");
        return key + " " + operator.operator() + " " + value;
    }


    public enum Type {
        AND, OR,
    }

    public enum Operator {
        EQUALS("="), SMALLER_THAN("<"), GREATER_THAN(">"), SMALLER_EQUALS_THAN("<="), GREATER_EQUALS_THAN(">="),
        ;

        @Getter
        @Accessors(fluent = true)
        private final String operator;

        Operator(String operator) {
            this.operator = operator;
        }
    }
}
