package co.fxl.gui.impl;

import co.fxl.gui.api.IDisplay;


public class SidePanelResizeListener implements IDisplay.IResizeListener {
    private static SidePanelResizeListener instance;
    private static IDisplay.IResizeListener listener;

    public static void setup(IDisplay display, IDisplay.IResizeListener l) {
        if ((SidePanelResizeListener.instance) == null) {
            SidePanelResizeListener.instance = new SidePanelResizeListener();
            display.addResizeListener(SidePanelResizeListener.instance);
        }

        SidePanelResizeListener.listener = l;
    }

    @Override
    public boolean onResize(int width, int height) {
        return SidePanelResizeListener.listener.onResize(width, height);
    }
}
