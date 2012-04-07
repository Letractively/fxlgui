package co.fxl.gui.layout.impl;

import co.fxl.gui.layout.api.ILayout;


public class Layout {
    public static final boolean ENABLED = false;
    private static ILayout instance;

    public static void register(ILayout instance) {
        Layout.instance = instance;
    }

    public static ILayout instance() {
        return Layout.instance;
    }
}
