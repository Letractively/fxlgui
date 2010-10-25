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
package co.fxl.gui.navigation.impl;

import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.navigation.api.IToolbar;
import co.fxl.gui.register.impl.RegisterWidgetImpl;

class NavigationWidgetImpl implements IMenuWidget {

	RegisterWidgetImpl registerWidget;

	NavigationWidgetImpl(ILayout panel) {
		registerWidget = new RegisterWidgetImpl(panel);
		registerWidget.background(0, 51, 102);
	}

	@Override
	public IMenuItem addNavigationItem() {
		RegisterStyle style = RegisterStyle.root();
		style.decorateWidget(registerWidget);
		return new MenuItemImpl(registerWidget, style);
	}

	@Override
	public IMenuWidget visible(boolean visible) {
		registerWidget.visible(true);
		return this;
	}

	@Override
	public IToolbar addToolbar() {
		throw new MethodNotImplementedException();
	}

	public IPanel<?> fillerPanel() {
		return registerWidget.addFillerPanel();
	}
}
