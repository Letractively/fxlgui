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
package co.fxl.gui.register.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.layout.api.ILayout.INavigation;
import co.fxl.gui.layout.impl.Layout;

privileged aspect RegisterWidgetImplLayout {

	declare parents : RegisterImpl implements INavigation.INavigationGroup.INavigationItem;
	declare parents : RegisterWidgetImpl implements INavigation.INavigationGroup;

	public String RegisterImpl.name() {
		return buttonLabel.text();
	}

	public String RegisterWidgetImpl.name() {
		return null;
	}

	public List<INavigation.INavigationGroup.INavigationItem> RegisterWidgetImpl.items() {
		List<INavigation.INavigationGroup.INavigationItem> is = new LinkedList<INavigation.INavigationGroup.INavigationItem>();
		for (RegisterImpl item : registers)
			is.add(item);
		return is;
	}

	after(RegisterWidgetImpl widget) : 
	execution(public RegisterWidgetImpl RegisterWidgetImpl.visible(boolean)) 
	&& this(widget) 
	&& if(Layout.ENABLED) {
		Layout.instance().navigationSub().group(widget)
				.panel(widget.headerPanel);
	}

	after(RegisterImpl item, boolean visible) :
	execution(public RegisterImpl RegisterImpl.enabled(boolean))
	&& this(item)
	&& args(visible)
	&& if(Layout.ENABLED) {
		Layout.instance().navigationSub().visible(item, visible);
	}

	after(RegisterImpl item) :
	execution(public RegisterImpl RegisterImpl.top())
	&& this(item)
	&& if(Layout.ENABLED) {
		Layout.instance().navigationSub().active(item, true);
	}
}