package de.codingphoenix.phoenixbase.database;

import lombok.AllArgsConstructor;
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

    public Condition(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "='" + value + "'";
    }


    public enum Type {
        AND, OR
    }
}
