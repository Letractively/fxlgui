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
package co.fxl.gui.table.util.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.util.api.ICompositeScrollPane;
import co.fxl.gui.table.util.impl.CompositeScrollPaneImplWidgetProvider;

class LazyScrollPaneTest {

	void run(IDisplay display) {
		display.register(new CompositeScrollPaneImplWidgetProvider());
		ICompositeScrollPane widget = display.container().panel().vertical()
				.size(600, 600).add().widget(ICompositeScrollPane.class);
		widget.size(600, 600);
		IVerticalPanel v = widget.viewPort().panel().vertical();
		add(v, 100);
		IVerticalPanel v2 = widget.right(200).panel().vertical().width(200);
		add(v2, 50);
		display.visible(true);
		widget.visible(true);
	}

	private void add(IVerticalPanel v, int m) {
		for (int i = 0; i < m; i++)
			v.add().label().text("T" + i);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new LazyScrollPaneTest().run(display);
	}
}
