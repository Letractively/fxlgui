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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.layout.impl.Layout;

privileged aspect NavigationWidgetImplLayout {

	after(NavigationWidgetImpl widget) : 
	execution(NavigationWidgetImpl.new(IContainer)) 
	&& this(widget) 
	&& if(Layout.ENABLED) {
		NavigationDialog.addButton(widget);
	}

	INavigationItem around(NavigationItemImpl item) :
	call(INavigationItem NavigationItemImpl.updateVisibility(boolean))
	&& withincode(public INavigationItem NavigationItemImpl.visible(boolean)) 
	&& this(item) 
	&& if(Layout.ENABLED) {
		return item;
	}

	void around() : 
	call(private void NavigationWidgetImpl.ensureSpaceBetweenGroups())  
	&& withincode(public INavigationGroup NavigationWidgetImpl.addGroup()) 
	&& if(Layout.ENABLED) {
	}
}
