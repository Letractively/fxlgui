package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.table.scroll.api.IScrollTableWidget;

privileged aspect ScrollTableWidgetImplLayout {

	void around(final ScrollTableWidgetImpl widget) : 
	call(protected void ScrollTableWidgetImpl.addFilter()) 
	&& withincode(public IScrollTableWidget ScrollTableWidgetImpl.visible(boolean)) 
	&& this(widget) {
		FilterDialog.addButton(widget);
	}
}
