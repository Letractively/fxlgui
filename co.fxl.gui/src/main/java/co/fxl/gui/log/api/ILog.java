package co.fxl.gui.log.api;

import co.fxl.gui.api.IContainer;

public interface ILog {

	ILog debug(String message);

	ILog container(IContainer c);

}
