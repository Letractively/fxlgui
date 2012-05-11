package co.fxl.gui.log.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.log.api.ILog;

public class LogWidgetProvider implements IWidgetProvider<ILog> {

	@Override
	public Class<ILog> widgetType() {
		return ILog.class;
	}

	@Override
	public ILog createWidget(IContainer container) {
		return Log.instance().container(container);
	}

}
