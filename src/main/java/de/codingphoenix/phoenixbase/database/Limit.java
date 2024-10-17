package de.codingphoenix.phoenixbase.database;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Limit {

    @Getter
    @Setter
    @Accessors(fluent = true)
    private int limit;

    @Override
    public String toString() {
        return " LIMIT " + limit;
    }
}
