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
package co.fxl.gui.demo;

import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.navigation.api.IMenuItem.INavigationListener;

class MenuItem {

	private IMenuItem parent;

	MenuItem(IMenuWidget widget, String title) {
		parent = widget.addNavigationItem().text(title);
	}

	MenuItem(IMenuWidget widget, String title, final Decorator decorator) {
		parent = widget.addNavigationItem().text(title);
		apply(decorator, parent);
	}

	IMenuItem nest(String title, final Decorator decorator) {
		final IMenuItem child = parent.addNavigationItem().text(title);
		apply(decorator, child);
		return child;
	}

	void apply(final Decorator decorator, final IMenuItem child) {
		decorator.decorate(child.contentPanel());
		child.addListener(new INavigationListener() {
			@Override
			public void onActive(boolean active) {
				decorator.update(child.contentPanel());
			}
		});
	}

	void active() {
		parent.active();
	}
}
