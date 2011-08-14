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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTDockPanel extends GWTPanel<DockPanel, IDockPanel> implements
		IDockPanel {

	private List<DockLayoutConstant> positions = new LinkedList<DockLayoutConstant>(
			Arrays.asList(new DockLayoutConstant[] { DockPanel.CENTER,
					DockPanel.NORTH, DockPanel.WEST, DockPanel.EAST,
					DockPanel.SOUTH }));
	private int height = -1;

	@SuppressWarnings("unchecked")
	GWTDockPanel(GWTContainer<?> container) {
		super((GWTContainer<DockPanel>) container);
		super.container.setComponent(new DockPanel());
		super.container.widget.setWidth("100%");
		super.container.widget.setVerticalAlignment(HasAlignment.ALIGN_TOP);
		super.container.widget.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		super.container.widget.setBorderWidth(0);
		super.container.widget.setSpacing(0);
	}

	@Override
	public IDockPanel height(int height) {
		this.height = height;
		return super.height(height);
	}

	@Override
	public void add(Widget widget) {
		// if (!(widget instanceof ScrollPanel))
		// widget.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		DockLayoutConstant location = positions.remove(0);
		container.widget.add(widget, location);
		if (widget instanceof HasHorizontalAlignment) {
			((HasHorizontalAlignment) widget)
					.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		}
		if (widget instanceof HasVerticalAlignment) {
			((HasVerticalAlignment) widget)
					.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		}
		if (location.equals(DockPanel.SOUTH)
				|| location.equals(DockPanel.NORTH)
				|| location.equals(DockPanel.CENTER)) {
			container.widget.setCellWidth(widget, "100%");
			widget.setWidth("100%");
		}
		if (location.equals(DockPanel.EAST) || location.equals(DockPanel.WEST)
				|| location.equals(DockPanel.CENTER)) {
			container.widget.setCellHeight(widget, height != -1 ? height + "px"
					: "100%");
			widget.setHeight(height != -1 ? height + "px" : "100%");
		}
	}

	private void setPosition(DockLayoutConstant position) {
		positions.remove(position);
		positions.add(0, position);
	}

	@Override
	public IContainer center() {
		setPosition(DockPanel.CENTER);
		return add();
	}

	@Override
	public IContainer left() {
		setPosition(DockPanel.WEST);
		return add();
	}

	@Override
	public IContainer top() {
		setPosition(DockPanel.NORTH);
		return add();
	}

	@Override
	public IContainer bottom() {
		setPosition(DockPanel.SOUTH);
		return add();
	}

	@Override
	public IContainer right() {
		setPosition(DockPanel.EAST);
		return add();
	}

	@Override
	public IDockPanel spacing(int pixel) {
		super.container.widget.setSpacing(pixel);
		return this;
	}
}
