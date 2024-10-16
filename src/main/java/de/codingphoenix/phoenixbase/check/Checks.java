package de.codingphoenix.phoenixbase.check;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Checks {
    public static boolean DEBUG = false;

    public static void checkIfNull(Object object, String name){
        if(object == null){
            throw new NullPointerException("Object '" + name + "' is null");
        }
    }

    public static void checkIfNullOrEmptyMap(Object object, String name) {
        if (object == null) {
            throw new NullPointerException("Object '" + name + "' is null");
        } else if (object instanceof Set<?> set && set.isEmpty()) {
            throw new NullPointerException("Set '" + name + "' is empty");
        } else if (object instanceof List<?> list && list.isEmpty()) {
            throw new NullPointerException("List '" + name + "' is empty");
        } else if (object instanceof Map<?, ?> map && map.isEmpty()) {
            throw new NullPointerException("Map '" + name + "' is empty");
        }
    }


}
