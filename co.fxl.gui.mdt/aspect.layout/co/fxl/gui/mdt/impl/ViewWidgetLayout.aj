package co.fxl.gui.mdt.impl;

import co.fxl.gui.layout.impl.Layout;
import co.fxl.gui.mdt.api.IViewConfiguration.ActionType;
import co.fxl.gui.mdt.api.IViewConfiguration.ViewType;
import co.fxl.gui.mdt.impl.ViewWidget.Link;

privileged aspect ViewWidgetLayout {

	after() :
	execution(private void ViewWidget.fire(ViewType, Link, ActionType)) {
		Layout.instance().actionMenu().showContent();
	}
}
