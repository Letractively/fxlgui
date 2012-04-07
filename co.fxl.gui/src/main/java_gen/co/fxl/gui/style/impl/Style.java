package co.fxl.gui.style.impl;

import co.fxl.gui.style.api.IStyle;


public class Style {
    public static boolean ENABLED = true;
    private static IStyle instance;

    public static void register(IStyle instance) {
        Style.instance = instance;
    }

    public static IStyle instance() {
        assert (Style.instance) != null;

        return Style.instance;
    }
}
