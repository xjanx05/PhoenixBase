package de.codingphoenix.phoenixbase.check;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Checks {
    public static boolean DEBUG = true;

    public static void checkIfNull(Object object, String name){
        if(object == null){
            throw new NullPointerException("Object '" + name + "' is null");
        }
    }

    public static void checkIfHasSpaces(Object object, String name){
        if(object instanceof String string && string.contains(" ")){
            throw new NullPointerException("Object '" + name + "' has spaces");
        }
    }
    public static void checkIfNotNumber(Object object, String name){
        if(!(object instanceof Number)){
            throw new NullPointerException("Object '" + name + "' is not a number");
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
