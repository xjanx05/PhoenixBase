package de.codingphoenix.phoenixbase.check;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Checks {

    /**
     * Enables more debug messages.
     */

    public static boolean DEBUG = false;


    /**
     * Checks if object is null
     *
     * @param object Object that will be checked
     * @param name   Name of the object for the error message
     */
    public static void checkIfNull(Object object, String name) {
        if (object == null)
            throw new NullPointerException("Object '" + name + "' is null");
    }

    /**
     * Checks if string only has allowed characters
     * Default Regex: A-Z; a-z; 0-9; _
     *
     * @param input Object that will be checked
     * @param name   Name of the object for the error message
     */
    public static void checkIfStringOnlyHasAllowedCharacters(String input, String name) {
        if (input != null && !input.matches("^[A-Za-z0-9_]+$"))
            throw new IllegalArgumentException("Object '" + name + "' has forbidden characters.");
    }

    /**
     * Checks if string only has allowed characters
     *
     * @param input Object that will be checked
     * @param regex The regex for the check.
     * @param name   Name of the object for the error message
     */
    public static void checkIfStringOnlyHasAllowedCharacters(String input, String regex, String name) {
        if (input != null && input.matches(regex))
            throw new IllegalArgumentException("Object '" + name + "' has forbidden characters.");
    }

    /**
     * Checks if object has spaces
     *
     * @param object Object that will be checked
     * @param name   Name of the object for the error message
     */
    public static void checkIfHasSpaces(Object object, String name) {
        if (object instanceof String string && string.contains(" "))
            throw new NullPointerException("Object '" + name + "' has spaces");
    }

    /**
     * Checks if object is not a number
     *
     * @param object Object that will be checked
     * @param name   Name of the object for the error message
     */
    public static void checkIfNotNumber(Object object, String name) {
        if (!(object instanceof Number))
            throw new NullPointerException("Object '" + name + "' is not a number");
    }

    /**
     * Checks if object is 'null' or if it`s a map it`s empty
     *
     * @param object Object that will be checked
     * @param name   Name of the object for the error message
     */
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

    /**
     * Check if an Integer is at min value
     *
     * @param integer Integer that will be checked
     * @param name    Name of the object for the error message
     */
    public static void checkIfIntMinValue(int integer, String name) {
        if (integer == Integer.MIN_VALUE)
            throw new NullPointerException("Integer '" + name + "' was not set.");
    }
}
