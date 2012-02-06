package co.fxl.gui.dnd.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.dnd.api.IDNDWidget;

public class DNDWidgetImplProvider implements IWidgetProvider<IDNDWidget> {

	@Override
	public Class<IDNDWidget> widgetType() {
		return IDNDWidget.class;
	}

	@Override
	public IDNDWidget createWidget(IContainer container) {
		return new DNDWidgetImpl(container);
	}

}
