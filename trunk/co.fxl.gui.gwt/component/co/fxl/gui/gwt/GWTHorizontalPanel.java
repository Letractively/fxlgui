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

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IHorizontalPanel;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTHorizontalPanel extends GWTPanel<HorizontalPanel, IHorizontalPanel>
		implements IHorizontalPanel {

	@SuppressWarnings("unchecked")
	GWTHorizontalPanel(GWTContainer<?> container) {
		super((GWTContainer<HorizontalPanel>) container);
		super.container.setComponent(new HorizontalPanel());
		align().begin();
		super.container.widget
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		spacing(0);
	}

	@Override
	GWTClickHandler<IHorizontalPanel> newGWTClickHandler(
			IClickListener clickListener) {
		return new GWTClickHandler<IHorizontalPanel>(this, clickListener);
	}

	@Override
	public void add(Widget widget) {
		container.widget.add(widget);
	}

	@Override
	public IHorizontalPanel spacing(int pixel) {
		container.widget.setSpacing(pixel);
		return this;
	}

	@Override
	public IHorizontalPanel addSpace(int pixel) {
		Widget p = new AbsolutePanel();
		p.setSize(pixel + "px", "1px");
		container.widget.add(p);
		return this;
	}

	@Override
	public IAlignment<IHorizontalPanel> align() {
		return new GWTHorizontalAlignment<IHorizontalPanel>(this,
				container.widget);
	}

	@Override
	public co.fxl.gui.api.ILinearPanel.ISpacing spacing() {
		throw new MethodNotImplementedException();
	}
}