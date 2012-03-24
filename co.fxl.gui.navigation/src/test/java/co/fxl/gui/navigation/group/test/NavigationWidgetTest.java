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
package co.fxl.gui.navigation.group.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.api.ITabDecorator;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.group.api.INavigationWidget;
import co.fxl.gui.navigation.group.impl.NavigationWidgetImplProvider;

public class NavigationWidgetTest {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new NavigationWidgetTest().run(display);
		display.size(600, 600).visible(true);
	}

	private void run(IDisplay display) {
		display.register(new NavigationWidgetImplProvider());
		INavigationWidget n = display.container().widget(
				INavigationWidget.class);
		for (int i = 0; i < 5; i++) {
			INavigationGroup g = n.addGroup().name("Group " + i);
			for (int j = 0; j < 3; j++) {
				final String t = "Item " + i + j;
				INavigationItem ni = g.addTab().name(t);
				ni.decorator(new ITabDecorator() {
					@Override
					public void decorate(IVerticalPanel panel,
							ICallback<Void> cb) {
						panel.add().label().text(t);
						cb.onSuccess(null);
					}
				});
			}
		}
	}

}
