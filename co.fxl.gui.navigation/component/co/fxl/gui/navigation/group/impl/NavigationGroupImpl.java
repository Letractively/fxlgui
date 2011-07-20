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
package co.fxl.gui.navigation.group.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;

class NavigationGroupImpl implements INavigationGroup {

	// TODO Code: Look: add horizontal line spanning width (color: #000000)
	// TODO Code: Look: Register-Widget: no flickering -> use Card-Panel, set
	// panel invisible, decorator with callback: switch when returned

	NavigationWidgetImpl widget;
	ILinearPanel<?> panel;
	ILabel header;
	ILinearPanel<?> itemPanel;
	private boolean first = true;
	List<NavigationItemImpl> items = new LinkedList<NavigationItemImpl>();

	NavigationGroupImpl(NavigationWidgetImpl widget) {
		this.widget = widget;
		ILayout layout = widget.navigationPanel.add().panel();
		panel = createPanel(layout);
		panel.addSpace(3);
		IVerticalPanel headerPanel = panel.add().panel().vertical();
		header = headerPanel.addSpace(2).add().label();
		header.font().weight().bold().pixel(11);// .color().white();
		panel.addSpace(1);
		itemPanel = createPanel(panel.add().panel());
	}

	ILinearPanel<?> createPanel(ILayout layout) {
		IHorizontalPanel panel = layout.horizontal();
		return panel;
	}

	@Override
	public INavigationGroup name(String name) {
		header.text(name + ":");
		return this;
	}

	@Override
	public INavigationItem addItem() {
		if (!first) {
			itemPanel.addSpace(1);
		}
		first = false;
		NavigationItemImpl item = new NavigationItemImpl(this);
		items.add(item);
		return item;
	}

	@Override
	public INavigationGroup visible(boolean visible) {
		panel.visible(visible);
		return this;
	}

	@Override
	public boolean visible() {
		return panel.visible();
	}
}
