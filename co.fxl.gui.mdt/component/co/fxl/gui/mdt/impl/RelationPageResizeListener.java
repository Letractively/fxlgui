package co.fxl.gui.mdt.impl;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDisplay.IResizeListener;

public class RelationPageResizeListener implements IResizeListener {

	private static RelationPageResizeListener instance;
	private static IResizeListener listener;

	public static void setup(IDisplay display, IResizeListener l) {
		if (instance == null) {
			instance = new RelationPageResizeListener();
			display.addResizeListener(instance);
		}
		listener = l;
	}

	@Override
	public void onResize(int width, int height) {
		listener.onResize(width, height);
	}
}
