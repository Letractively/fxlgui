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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ISplitPane;

import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTSplitPane extends GWTElement<Widget, ISplitPane> implements ISplitPane {

	GWTSplitPane(GWTContainer<Widget> container) {
		super(container);
		container.widget.setHeight("600px");
	}

	@Override
	public IContainer first() {
		return new GWTContainer<Widget>(container.parent) {
			public void setComponent(Widget component) {
				super.widget = component;
				component.setWidth("100%");
				component.setHeight("100%");
				HorizontalSplitPanel p = (HorizontalSplitPanel) container.widget;
				p.setLeftWidget(component);
			}
		};
	}

	@Override
	public IContainer second() {
		return new GWTContainer<Widget>(container.parent) {
			public void setComponent(Widget component) {
				super.widget = component;
				component.setWidth("100%");
				component.setHeight("100%");
				HorizontalSplitPanel p = (HorizontalSplitPanel) container.widget;
				p.setRightWidget(component);
			}
		};
	}

	@Override
	public ISplitPane vertical() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ISplitPane splitPosition(int pixel) {
		HorizontalSplitPanel p = (HorizontalSplitPanel) container.widget;
		p.setSplitPosition(pixel + "px");
		return this;
	}

	@Override
	public IBorder border() {
		return new GWTWidgetBorder(container.widget);
	}
}