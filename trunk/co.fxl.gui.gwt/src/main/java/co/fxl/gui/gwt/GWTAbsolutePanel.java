/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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
import co.fxl.gui.api.IElement;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

class GWTAbsolutePanel extends GWTPanel<AbsolutePanel, IAbsolutePanel>
		implements IAbsolutePanel {

	private int x;
	private int y;

	@SuppressWarnings("unchecked")
	GWTAbsolutePanel(GWTContainer<?> container) {
		super((GWTContainer<AbsolutePanel>) container);
	}

	@Override
	public IAbsolutePanel addResizeListener(IResizeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IAbsolutePanel offset(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public IAbsolutePanel offset(IElement<?> element, int x, int y) {
		Widget widget = (Widget) element.nativeElement();
		container.widget.setWidgetPosition(widget, x, y);
		// int newY = container.widget.getWidgetTop(widget);
		return this;
	}

	@Override
	public void add(Widget widget) {
		// widget.setWidth("100%");
		container.widget.add(widget, x, y);
	}

}
