/**
 * This file is part of FXL GUI API.
 *  
 * FXL GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * FXL GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
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
