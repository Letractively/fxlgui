package co.fxl.gui.impl;

import co.fxl.gui.layout.impl.Layout;

public aspect ViewListLayout {

	after() : execution(public void ViewList.ViewImpl.onAllowedClick()) {
		Layout.instance().actionMenu().showContent();
	}
}
