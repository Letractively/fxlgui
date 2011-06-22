package co.fxl.gui.tree.gwt;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.tree.api.ILazyTreeWidget;

public class GWTLazyTreeWidgetProvider implements
		IWidgetProvider<ILazyTreeWidget> {

	@Override
	public Class<ILazyTreeWidget> widgetType() {
		return ILazyTreeWidget.class;
	}

	@Override
	public ILazyTreeWidget createWidget(IContainer container) {
		return new GWTLazyTreeWidget(container);
	}
}
