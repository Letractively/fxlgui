package co.fxl.gui.swing;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDisplay.IExceptionHandler;

class SwingUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private List<IExceptionHandler> handlers = new LinkedList<IExceptionHandler>();

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		for (IExceptionHandler h : handlers) {
			h.onException(arg1);
		}
	}

	void add(IExceptionHandler handler) {
		handlers.add(handler);
	}
}
