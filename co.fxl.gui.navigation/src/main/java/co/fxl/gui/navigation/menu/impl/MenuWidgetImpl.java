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
package co.fxl.gui.navigation.menu.impl;

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.navigation.api.ITabGroup;
import co.fxl.gui.navigation.api.ITabWidget;
import co.fxl.gui.navigation.menu.api.IMenuItem;
import co.fxl.gui.navigation.menu.api.IMenuWidget;
import co.fxl.gui.register.impl.RegisterWidgetImpl;
import co.fxl.gui.register.impl.RegisterWidgetImpl.ColorDecorator;

class MenuWidgetImpl implements IMenuWidget, ITabGroup<IMenuItem> {

	RegisterWidgetImpl registerWidget;

	MenuWidgetImpl(IContainer panel) {
		registerWidget = new RegisterWidgetImpl(panel.panel());
		registerWidget.topBorder();
		registerWidget.background(new ColorDecorator() {
			@Override
			public void decorate(IColor color) {
				color.rgb(249, 249, 249).gradient().vertical()
						.rgb(216, 216, 216);
			}
		});
		registerWidget.spacing = 3;
		registerWidget.fontSize = 12;
	}

	public IMenuItem addNavigationItem() {
		RegisterStyle style = RegisterStyle.root();
		style.decorateWidget(registerWidget);
		return new MenuItemImpl(registerWidget, style);
	}

	@Override
	public ITabWidget<ITabGroup<IMenuItem>, IMenuItem> visible(boolean visible) {
		registerWidget.visible(true);
		return this;
	}

	// public IPanel<?> fillerPanel() {
	// IPanel<?> fillerPanel = registerWidget.addFillerPanel();
	// return fillerPanel;
	// }

	@Override
	public int height() {
		return registerWidget.heightMenu();
	}

	@Override
	public ITabGroup<IMenuItem> defaultGroup() {
		return addGroup();
	}

	@Override
	public ITabGroup<IMenuItem> addGroup() {
		return this;
	}

	@Override
	public IMenuItem addTab() {
		return addNavigationItem();
	}

	// public IMenuWidget outerSpacing(int outerSpacing) {
	// registerWidget.outerSpacing(outerSpacing);
	// return this;
	// }

	// public IMenuWidget showRegisterPanel(boolean showRegisterPanel) {
	// registerWidget.showRegisterPanel(showRegisterPanel);
	// return this;
	// }
}
