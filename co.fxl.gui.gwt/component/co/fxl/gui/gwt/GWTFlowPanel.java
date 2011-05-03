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

import co.fxl.gui.api.IFlowPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTFlowPanel extends GWTPanel<FlowPanel, IFlowPanel> implements
		IFlowPanel {

	@SuppressWarnings("unchecked")
	GWTFlowPanel(GWTContainer<?> container) {
		super((GWTContainer<FlowPanel>) container);
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.setWidth("100%");
		super.container.setComponent(flowPanel);
	}

	@Override
	GWTClickHandler<IFlowPanel> newGWTClickHandler(IClickListener clickListener) {
		return new GWTClickHandler<IFlowPanel>(this, clickListener);
	}

	@Override
	public void add(Widget widget) {
		widget.getElement().getStyle().setProperty("display", "inline");
		container.widget.add(widget);
	}

	@Override
	public IFlowPanel spacing(int spacing) {
		// TODO ... new MethodNotImplementedException();
		return this;
	}
}