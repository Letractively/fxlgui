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
package co.fxl.gui.table.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.api.ITableWidget;
import co.fxl.gui.table.impl.TableWidgetImplProvider;

class TableWidgetTest {

	void run(IDisplay display) {
		display.register(new TableWidgetImplProvider());
		IVerticalPanel panel = display.container().panel().vertical()
				.spacing(10);
		panel.color().rgb(0, 0, 0).gradient().vertical().rgb(255, 255, 255);
//		@SuppressWarnings("unchecked")
//		ITableWidget<String> widget = (ITableWidget<String>) panel.add()
//				.widget(ITableWidget.class);
//		widget.selection().single();
//		widget.addTitle("Table");
//		widget.addButton("New");
//		for (int i = 0; i < 3; i++)
//			widget.addColumn().name("Column " + i).sortable().type()
//					.type(String.class);
//		for (int j = 0; j < 10; j++) {
//			widget.addRow().add("C0").add("C1").add("C2");
//		}
//		widget.visible(true);
		display.visible(true);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new TableWidgetTest().run(display);
	}
}
