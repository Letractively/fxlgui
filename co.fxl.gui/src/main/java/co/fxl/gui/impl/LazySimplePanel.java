package co.fxl.gui.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;

class LazySimplePanel implements ISimplePanel {

	private IContainer container;

	LazySimplePanel(IContainer container) {
		this.container = container;
	}

	@Override
	public IHorizontalPanel horizontal() {
		return new HorizontalPanelAdp() {

			@Override
			public IContainer add() {
				return container;
			}

			@Override
			protected IHorizontalPanel element() {
				if (super.element() == null) {
					element(container.panel().horizontal());
					container = element().add();
				}
				return super.element();
			}
		};
	}

	@Override
	public IVerticalPanel vertical() {
		return new VerticalPanelAdp() {

			@Override
			public IContainer add() {
				return container;
			}

			@Override
			protected IVerticalPanel element() {
				if (super.element() == null) {
					element(container.panel().vertical());
					container = element().add();
				}
				return super.element();
			}
		};
	}

}
