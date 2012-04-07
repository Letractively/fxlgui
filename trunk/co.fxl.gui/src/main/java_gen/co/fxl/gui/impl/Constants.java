package co.fxl.gui.impl;

import java.util.HashMap;
import java.util.Map;


public class Constants {
    private static Map<java.lang.String, java.lang.Object> constants = new HashMap<java.lang.String, java.lang.Object>();

    public static void put(String id, Object o) {
        Constants.constants.put(id, o);
    }

    @SuppressWarnings(value = "unchecked")
    public static <T> T get(String string, T defaultValue) {
        if (Constants.constants.containsKey(string)) {
            return ((T) (Constants.constants.get(string)));
        }

        return defaultValue;
    }
}
