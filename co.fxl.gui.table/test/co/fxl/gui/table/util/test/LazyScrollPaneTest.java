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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.util.api.ILazyScrollPane;
import co.fxl.gui.table.util.api.ILazyScrollPane.IDecorator;
import co.fxl.gui.table.util.impl.LazyScrollPanelImplWidgetProvider;

class LazyScrollPaneTest implements IDecorator {

	void run(IDisplay display) {
		display.register(new LazyScrollPanelImplWidgetProvider());
		ILazyScrollPane w = (ILazyScrollPane) display.container().panel()
				.vertical().add().widget(ILazyScrollPane.class);
		w.minRowHeight(20);
		w.height(600);
		w.size(1000);
		w.decorator(this);
		w.visible(true);
		display.visible(true);
	}

	@Override
	public void decorate(IContainer container, int firstRow, int lastRow) {
		IVerticalPanel v = container.panel().vertical();
		for (int i = firstRow; i <= lastRow; i++) {
			v.add().button().text(String.valueOf(i)).size(100, 20 + (i % 20));
		}
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
