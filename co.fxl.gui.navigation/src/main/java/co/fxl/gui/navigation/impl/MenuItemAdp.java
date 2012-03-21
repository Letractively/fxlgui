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
package co.fxl.gui.navigation.impl;

import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.group.impl.NavigationItemImpl;
import co.fxl.gui.navigation.menu.api.IMenuItem;

class MenuItemAdp implements IMenuItem {

	MenuItemAdp(NavigationItemImpl item) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenuItem addNavigationItem() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenuItem listener(
			co.fxl.gui.navigation.menu.api.IMenuItem.INavigationListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenuItem name(String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IVerticalPanel contentPanel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenuItem visible(boolean visible) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenuItem active(boolean active) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenuItem enabled(boolean enabled) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isActive() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEnabled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenuItem imageResource(String imageResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenuItem toggleLoading(boolean toggleLoading) {
		throw new UnsupportedOperationException();
	}

}