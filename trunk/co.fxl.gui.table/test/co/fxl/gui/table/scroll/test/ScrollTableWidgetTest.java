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
package co.fxl.gui.table.scroll.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.bulk.impl.BulkTableWidgetImplProvider;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.scroll.impl.ScrollTableWidgetImplProvider;

class ScrollTableWidgetTest implements IRows<String> {

	void run(IDisplay display) {
		display.register(new ScrollTableWidgetImplProvider());
		display.register(new BulkTableWidgetImplProvider());
		IVerticalPanel panel = display.container().panel().vertical()
				.spacing(10);
		@SuppressWarnings("unchecked")
		IScrollTableWidget<String> widget = (IScrollTableWidget<String>) panel
				.add().widget(IScrollTableWidget.class);
		widget.selection().multi();
		widget.addTitle("Table");
		widget.addButton("New");
		for (int i = 0; i < 3; i++) {
			IScrollTableColumn<String> c = widget.addColumn();
			c.name("Column " + i).sortable().type().type(String.class);
		}
		widget.rows(this);
		widget.visible(true);
		display.visible(true);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new ScrollTableWidgetTest().run(display);
	}

	@Override
	public String identifier(int i) {
		assert i < size();
		return String.valueOf(i);
	}

	@Override
	public Object[] row(int i) {
		assert i < size();
		return new Object[] { "C0/" + i, "C1/" + (i + 47), "C2/" + (i + 95) };
	}

	@Override
	public int size() {
		return 1000;
	}
}
