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
package co.fxl.gui.navigation.group.api;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.navigation.api.ITabWidget;

public interface INavigationWidget extends
		ITabWidget<INavigationGroup, INavigationItem> {

	public interface INavigationListener {

		void onBeforeNavigation(INavigationItem activeItem, boolean viaClick,
				co.fxl.gui.api.ICallback<Void> cb);
	}

	INavigationWidget addNavigationListener(INavigationListener l);

	INavigationWidget refresh(ICallback<Void> cb);

	INavigationItem activeItem();

	boolean update(boolean alwaysAdjust);

	INavigationWidget holdUpdate(boolean b);

	boolean active(String preset, ICallback<Void> cb);

	INavigationItem findByName(String name);

	INavigationWidget configureListener(IClickListener l);

	INavigationWidget showConfigure(boolean b);
}
