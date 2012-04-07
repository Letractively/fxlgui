package co.fxl.gui.impl;

import co.fxl.gui.api.IDisplay;


public class Display {
    private static IDisplay instance;

    public static void instance(IDisplay display) {
        Display.instance = display;
    }

    public static IDisplay instance() {
        return Display.instance;
    }
}
