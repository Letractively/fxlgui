package co.fxl.gui.table.flex.swing;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.table.flex.IFlexTableWidget;

public class SwingFlexTableWidgetProvider implements
		IWidgetProvider<IFlexTableWidget> {

	@Override
	public Class<IFlexTableWidget> widgetType() {
		return IFlexTableWidget.class;
	}

	@Override
	public IFlexTableWidget createWidget(IContainer container) {
		return new SwingFlexTableWidget(container);
	}

}
