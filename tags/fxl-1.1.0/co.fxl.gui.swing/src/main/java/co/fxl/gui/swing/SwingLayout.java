/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.swing;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPanelProvider;
import co.fxl.gui.api.IVerticalPanel;

class SwingLayout implements ILayout {

	SwingPanel<?> panel;

	SwingLayout(SwingPanel<?> panel) {
		this.panel = panel;
	}

	@Override
	public IPanel<?> plugIn(Class<?> clazz) {
		IPanelProvider<?> provider = panel.container.parent
				.lookupSwingDisplay().panelProviders.get(clazz);
		if (provider == null)
			throw new UnsupportedOperationException(clazz.getName());
		return provider.createPanel(panel.container);
	}

	@Override
	public IDockPanel dock() {
		return new SwingDockPanel(panel.container);
	}

	@Override
	public IGridPanel grid() {
		return new SwingGridPanel(panel.container);
	}

	@Override
	public IHorizontalPanel horizontal() {
		return new SwingHorizontalPanel(panel.container);
	}

	@Override
	public IVerticalPanel vertical() {
		return new SwingVerticalPanel(panel.container);
	}

	@Override
	public ICardPanel card() {
		return new SwingCardPanel(panel.container);
	}

	@Override
	public IAbsolutePanel absolute() {
		return new SwingAbsolutePanel(panel.container);
	}

	@Override
	public IFlowPanel flow() {
		return new SwingFlowPanel(panel.container);
	}

	@Override
	public IFocusPanel focus() {
		return new SwingFocusPanel(panel.container);
	}
}
