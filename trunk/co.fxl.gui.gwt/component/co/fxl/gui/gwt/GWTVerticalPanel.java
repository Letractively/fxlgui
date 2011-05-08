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
import co.fxl.gui.api.IVerticalPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTVerticalPanel extends GWTPanel<VerticalPanel, IVerticalPanel>
		implements IVerticalPanel {

	private boolean stretch = true;
	private int innerSpace = 0;

	@SuppressWarnings("unchecked")
	GWTVerticalPanel(GWTContainer<?> container) {
		super((GWTContainer<VerticalPanel>) container);
		super.container.setComponent(new VerticalPanel());
		super.container.widget.setBorderWidth(0);
		spacing(0);
		align().begin();
		super.container.widget.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
	}

	@Override
	GWTClickHandler<IVerticalPanel> newGWTClickHandler(
			IClickListener clickListener) {
		return new GWTClickHandler<IVerticalPanel>(this, clickListener);
	}

	@Override
	public void add(Widget widget) {
		if (stretch)
			widget.setWidth("100%");
		if (innerSpace > 0 && container.widget.getWidgetCount() > 0)
			addSpace(innerSpace);
		container.widget.add(widget);
	}

	@Override
	public IVerticalPanel spacing(int pixel) {
		container.widget.setSpacing(pixel);
		return this;
	}

	@Override
	public IVerticalPanel addSpace(int pixel) {
		Widget p = new AbsolutePanel();
		p.setSize("1px", pixel + "px");
		container.widget.add(p);
		return this;
	}

	@Override
	public IVerticalPanel stretch(boolean stretch) {
		this.stretch = stretch;
		return this;
	}

	@Override
	public IAlignment<IVerticalPanel> align() {
		return new IAlignment<IVerticalPanel>() {

			@Override
			public IVerticalPanel end() {
				container.widget
						.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
				return GWTVerticalPanel.this;
			}

			@Override
			public IVerticalPanel center() {
				container.widget
						.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
				return GWTVerticalPanel.this;
			}

			@Override
			public IVerticalPanel begin() {
				container.widget
						.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
				return GWTVerticalPanel.this;
			}
		};
	}

	@Override
	public co.fxl.gui.api.ILinearPanel.ISpacing spacing() {
		return new ISpacing() {

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing left(int pixel) {
				container.widget.getElement().getStyle()
						.setPaddingLeft(pixel, Unit.PX);
				return this;
			}

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing right(int pixel) {
				container.widget.getElement().getStyle()
						.setPaddingRight(pixel, Unit.PX);
				return this;
			}

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing top(int pixel) {
				container.widget.getElement().getStyle()
						.setPaddingTop(pixel, Unit.PX);
				return this;
			}

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing bottom(int pixel) {
				container.widget.getElement().getStyle()
						.setPaddingBottom(pixel, Unit.PX);
				return this;
			}

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing inner(int pixel) {
				innerSpace = pixel;
				return this;
			}
		};
	}
}