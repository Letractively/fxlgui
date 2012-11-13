package co.fxl.gui.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;

public class SimplePanelImplWidgetProvider implements
		IWidgetProvider<ISimplePanel> {

	@Override
	public Class<ISimplePanel> widgetType() {
		return ISimplePanel.class;
	}

	@Override
	public ISimplePanel createWidget(IContainer container) {
		return new SimplePanelImpl(container);
	}

}
