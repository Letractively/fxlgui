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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTLayout implements ILayout {

	GWTContainer<?> panel;

	GWTLayout(GWTContainer<?> panel) {
		this.panel = panel;
	}

	@Override
	public IPanel<?> plugIn(Class<?> clazz) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDockPanel dock() {
		setComponent(new DockPanel());
		return (IDockPanel) (panel.element = new GWTDockPanel(panel));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void setComponent(Widget component) {
		if (panel == null)
			throw new MethodNotImplementedException();
		GWTContainer c = (GWTContainer) panel;
		c.setComponent(component);
	}

	@Override
	public IGridPanel grid() {
		setComponent(new Grid());
		return (IGridPanel) (panel.element = new GWTGridPanel(panel));
	}

	@Override
	public IHorizontalPanel horizontal() {
		setComponent(new HorizontalPanel());
		return (IHorizontalPanel) (panel.element = new GWTHorizontalPanel(panel));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IVerticalPanel vertical() {
		Widget component = (Widget) new VerticalPanel();
		if (panel == null)
			throw new MethodNotImplementedException();
		GWTContainer c = (GWTContainer) panel;
		c.setComponent(component);
		return (IVerticalPanel) (panel.element = new GWTVerticalPanel(panel));
	}

	@Override
	public ICardPanel card() {
		setComponent(new DeckPanel());
		return (ICardPanel) (panel.element = new GWTCardPanel(panel));
	}

	@Override
	public IAbsolutePanel absolute() {
		setComponent(new AbsolutePanel());
		return (IAbsolutePanel) (panel.element = new GWTAbsolutePanel(panel));
	}

	@Override
	public IFlowPanel flow() {
		setComponent(new FlowPanel());
		return (IFlowPanel) (panel.element = new GWTFlowPanel(panel));
	}
}
