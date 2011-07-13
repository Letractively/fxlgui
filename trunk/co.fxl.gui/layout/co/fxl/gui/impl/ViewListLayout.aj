package co.fxl.gui.impl;

public aspect ViewListLayout {

	after() : execution(public void ViewList.ViewImpl.onAllowedClick()) {
		SplitLayoutNavigation.showTable();
	}
}
