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

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;

class NavigationGroupImpl implements INavigationGroup {

	NavigationWidgetImpl widget;
	private IHorizontalPanel panel;
	private ILabel header;
	IHorizontalPanel itemPanel;
	private boolean first = true;

	NavigationGroupImpl(NavigationWidgetImpl widget) {
		this.widget = widget;
		panel = widget.navigationPanel.add().panel().horizontal();
		panel.addSpace(3);
		IVerticalPanel headerPanel = panel.add().panel().vertical();
		header = headerPanel.add().label();
		header.font().weight().bold().pixel(11);//.color().white();
		panel.addSpace(4);
		itemPanel = panel.add().panel().horizontal();
	}

	@Override
	public INavigationGroup name(String name) {
		header.text(name+":");
		return this;
	}

	@Override
	public INavigationItem addItem() {
		if (!first) {
			itemPanel.addSpace(1);
		}
		first = false;
		return new NavigationItemImpl(this);
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
