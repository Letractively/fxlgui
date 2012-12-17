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

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;

class NavigationGroupImpl implements INavigationGroup {

	NavigationWidgetImpl widget;
	IHorizontalPanel panel;
	ILabel header;
	IHorizontalPanel itemPanel;
	private boolean first = true;
	List<NavigationItemImpl> items = new LinkedList<NavigationItemImpl>();
	private boolean visible = true;
	private boolean displayed = true;
	int[] colorInactive;
	int[] colorInactiveGradient = new int[] { 63, 63, 63 };
	private IVerticalPanel headerPanel;

	NavigationGroupImpl(NavigationWidgetImpl widget) {
		this.widget = widget;
		panel = widget.navigationPanel.add().panel().horizontal();
		headerPanel = panel.add().panel().vertical();
		headerPanel.margin().left(widget.groups.isEmpty() ? 3 : 8).right(1);
		header = headerPanel.addSpace(2).add().label();
		header.font().weight().bold().pixel(11);
//		panel.addSpace(1);
		itemPanel = panel.add().panel().horizontal();
		colorInactive = widget.colorInactive;
		colorInactiveGradient = widget.colorInactiveGradient;
		updateVisibilityLabel();
	}

	void updateVisibilityLabel() {
		headerPanel.visible(widget.showGroupLabel);
	}

	@Override
	public INavigationGroup name(String name) {
		header.text(name + ":");
		return this;
	}

	@Override
	public INavigationItem addTab() {
		if (!first) {
			itemPanel.addSpace(1);
		}
		first = false;
		NavigationItemImpl item = new NavigationItemImpl(this);
		items.add(item);
		return item;
	}

	@Override
	public NavigationGroupImpl visible(boolean visible) {
		this.visible = visible;
		updatePanelVisible();
		return this;
	}

	private void updatePanelVisible() {
		panel.visible(visible && displayed);
	}

	@Override
	public boolean visible() {
		return visible;
	}

	void updateVisible() {
		visible = false;
		panel.visible(true);
		for (NavigationItemImpl item : items) {
			visible |= item.basicPanel.visible();
		}
		updatePanelVisible();
	}

	@Override
	public INavigationGroup active(ICallback<Void> cb) {
		for (NavigationItemImpl item : items) {
			if (item.visible()) {
				item.active(true, cb);
				return this;
			}
		}
		return this;
	}

	int width() {
		return panel.width();
	}

	public String name() {
		return header.text();
	}

	void displayed(boolean displayed) {
		this.displayed = displayed;
	}

	public boolean active(String preset, ICallback<Void> cb) {
		for (NavigationItemImpl item : items) {
			if (item.visible() && item.name().equals(preset)) {
				item.active(true, cb);
				return true;
			}
		}
		return false;
	}

	INavigationItem findByName(String name) {
		for (NavigationItemImpl item : items) {
			if (item.name().equals(name)) {
				return item;
			}
		}
		return null;
	}

	void clearCache() {
		for (NavigationItemImpl i : items)
			i.clearCache();
	}
}
