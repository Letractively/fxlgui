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
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.filter.impl.MiniFilterWidgetImplProvider;
import co.fxl.gui.table.bulk.impl.BulkTableWidgetImplProvider;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IInsert;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;
import co.fxl.gui.table.scroll.impl.ScrollTableWidgetImplProvider;

class ScrollTableWidgetCommandTest implements IRows<String> {

	void run(IDisplay display) {
		createContent();
		display.register(new ScrollTableWidgetImplProvider());
		display.register(new BulkTableWidgetImplProvider());
		display.register(new MiniFilterWidgetImplProvider());
		IVerticalPanel panel = display.container().panel().vertical()
				.spacing(10);
		@SuppressWarnings("unchecked")
		IScrollTableWidget<String> widget = (IScrollTableWidget<String>) panel
				.add().widget(IScrollTableWidget.class);
		widget.selection().single();
		widget.addTitle("Table");
		widget.addButton("New");
		for (int i = 0; i < 3; i++)
			widget.addColumn().filterable().name("Column " + i).sortable()
					.type().type(String.class);
		widget.rows(this);
		widget.commandButtons().listenOnMoveUp(null).listenOnMoveDown(null);
		widget.commandButtons().listenOnAdd(
				new IRowListener<IScrollTableWidget.IInsert>() {

					@Override
					public void onClick(Object identifier, int index,
							ICallback<IInsert> callback) {
						if (identifier == null) {
							content.add(index, newContent(content.size()));
						} else
							content.add(newContent(content.size()));
						callback.onSuccess(null);
					}
				});
		widget.commandButtons().listenOnRemove(new IRowListener<Boolean>() {

			@Override
			public void onClick(Object identifier, int index,
					ICallback<Boolean> callback) {
				content.remove(index);
				callback.onSuccess(true);
			}
		});
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
		new ScrollTableWidgetCommandTest().run(display);
	}

	private List<Object[]> content = new LinkedList<Object[]>();

	void createContent() {
		for (int i = 0; i < 10; i++)
			content.add(newContent(i));
	}

	private Object[] newContent(int i) {
		return new Object[] { "C0/" + i, "C1/" + (i + 47), "C2/" + (i + 95) };
	}

	@Override
	public String identifier(int i) {
		assert i < size();
		return String.valueOf(i);
	}

	@Override
	public Object[] row(int i) {
		assert i < size();
		return content.get(i);
	}

	@Override
	public int size() {
		return content.size();
	}
}
