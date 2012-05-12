package co.fxl.gui.log.api;

import co.fxl.gui.api.IContainer;

public interface ILog {

	ILog start(String message);

	ILog stop(String message);

	ILog debug(String message);

	ILog debug(String message, long duration);

	ILog debug(String message, long duration, Throwable... stacktraces);

	ILog container(IContainer c);

}
