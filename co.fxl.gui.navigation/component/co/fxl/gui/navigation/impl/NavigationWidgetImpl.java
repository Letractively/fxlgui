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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.navigation.api.IToolbar;
import co.fxl.gui.register.impl.RegisterWidgetImpl;

class NavigationWidgetImpl implements IMenuWidget {

	RegisterWidgetImpl registerWidget;

	NavigationWidgetImpl(IContainer panel) {
		registerWidget = new RegisterWidgetImpl(panel.panel());
		registerWidget.background(200, 200, 200);
		registerWidget.spacing = 4;
		registerWidget.fontSize = 12;
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
		IPanel<?> fillerPanel = registerWidget.addFillerPanel();
		return fillerPanel;
	}

	@Override
	public IMenuWidget background(int r, int g, int b) {
		registerWidget.background(r, g, b);
		return this;
	}
}
