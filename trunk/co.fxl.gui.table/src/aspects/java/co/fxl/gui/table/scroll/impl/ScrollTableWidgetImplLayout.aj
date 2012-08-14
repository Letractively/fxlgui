package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.layout.impl.Layout;

privileged aspect ScrollTableWidgetImplLayout {

	// TODO ARCHITECTURE: Decomposition: extract layout, move as much as
	// possible into component, only leave minimum of glue code in aspect

	void around(final ScrollTableWidgetImpl widget) : 
	call(protected void ScrollTableWidgetImpl.addFilter()) 
	&& withincode(public IScrollTableWidget ScrollTableWidgetImpl.visible(boolean)) 
	&& this(widget) 
	&& if(Layout.ENABLED) {
		FilterDialog.addButton(widget);
	}
}
