package co.fxl.gui.input.dummy;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.input.api.ICalendarWidget;

public class DummyCalendarWidgetProvider implements
		IWidgetProvider<ICalendarWidget> {

	@Override
	public Class<ICalendarWidget> widgetType() {
		return ICalendarWidget.class;
	}

	@Override
	public ICalendarWidget createWidget(IContainer container) {
		return new DummyCalendarWidget(container);
	}
}
