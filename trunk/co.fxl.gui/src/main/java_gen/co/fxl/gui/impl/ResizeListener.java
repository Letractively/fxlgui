package co.fxl.gui.impl;

import co.fxl.gui.api.IDisplay;


public class ResizeListener implements IDisplay.IResizeListener {
    private static ResizeListener instance;
    private static IDisplay.IResizeListener listener;
    private static IDisplay display;

    public static void setup(IDisplay display, IDisplay.IResizeListener l) {
        ResizeListener.display = display;

        if ((ResizeListener.instance) == null) {
            ResizeListener.instance = new ResizeListener();
            display.addResizeListener(ResizeListener.instance);
        }

        ResizeListener.listener = l;
    }

    @Override
    public boolean onResize(int width, int height) {
        return ResizeListener.listener.onResize(width, height);
    }

    public static void remove(IDisplay.IResizeListener l) {
        if (l == (ResizeListener.listener)) {
            ResizeListener.display.removeResizeListener(l);
        }
    }
}
