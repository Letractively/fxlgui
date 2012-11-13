package co.fxl.gui.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;

class SimplePanelImpl implements ISimplePanel {

	private IContainer container;

	SimplePanelImpl(IContainer container) {
		this.container = container;
	}

	@Override
	public IHorizontalPanel horizontal() {
		return container.panel().horizontal();
	}

	@Override
	public IVerticalPanel vertical() {
		return container.panel().vertical();
	}

}
