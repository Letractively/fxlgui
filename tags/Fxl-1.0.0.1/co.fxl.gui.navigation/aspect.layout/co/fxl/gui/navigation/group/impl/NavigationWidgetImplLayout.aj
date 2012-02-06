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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.layout.api.ILayout.INavigation;
import co.fxl.gui.layout.impl.Layout;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationWidget;

privileged aspect NavigationWidgetImplLayout {

	declare parents : NavigationGroupImpl implements INavigation.INavigationGroup;
	declare parents : NavigationItemImpl implements INavigation.INavigationGroup.INavigationItem;

	private List<INavigation.INavigationGroup> NavigationWidgetImpl.groups() {
		List<INavigation.INavigationGroup> is = new LinkedList<INavigation.INavigationGroup>();
		for (NavigationGroupImpl item : groups)
			is.add(item);
		return is;
	}

	public String NavigationGroupImpl.name() {
		return header.text();
	}

	public boolean NavigationItemImpl.visible() {
		return basicPanel.visible();
	}

	public List<INavigation.INavigationGroup.INavigationItem> NavigationGroupImpl.items() {
		List<INavigation.INavigationGroup.INavigationItem> is = new LinkedList<INavigation.INavigationGroup.INavigationItem>();
		for (NavigationItemImpl item : items)
			is.add(item);
		return is;
	}

	after(NavigationWidgetImpl widget) : 
	execution(public INavigationWidget NavigationWidgetImpl.visible(boolean)) 
	&& this(widget) 
	&& if(Layout.ENABLED) {
		Layout.instance().navigationMain().groups(widget.groups())
				.panel(widget.navigationPanel);
		IContainer c = widget.hPanel.cell(1, 0).align().end().valign().center()
				.panel().horizontal().add();
		Layout.instance().actionMenu().container(c);
	}

	after(NavigationItemImpl item, boolean visible) :
	execution(public NavigationItemImpl NavigationItemImpl.visible(boolean))
	&& this(item)
	&& args(visible)
	&& if(Layout.ENABLED) {
		Layout.instance().navigationMain().visible(item, visible);
	}

	after(NavigationItemImpl item) :
	execution(public NavigationItemImpl NavigationItemImpl.active())
	&& this(item)
	&& if(Layout.ENABLED) {
		Layout.instance().navigationMain().active(item, true);
	}

	void around() : 
	call(private void NavigationWidgetImpl.ensureSpaceBetweenGroups())  
	&& withincode(public INavigationGroup NavigationWidgetImpl.addGroup()) 
	&& if(Layout.ENABLED) {
	}
}
