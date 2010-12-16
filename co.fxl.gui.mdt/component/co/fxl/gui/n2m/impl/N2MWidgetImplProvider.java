package co.fxl.gui.n2m.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.n2m.api.IN2MWidget;

@SuppressWarnings("rawtypes")
public class N2MWidgetImplProvider implements IWidgetProvider<IN2MWidget> {

	@Override
	public Class<IN2MWidget> widgetType() {
		return IN2MWidget.class;
	}

	@Override
	public IN2MWidget createWidget(IContainer container) {
		return new N2MWidgetImpl(container);
	}
}
