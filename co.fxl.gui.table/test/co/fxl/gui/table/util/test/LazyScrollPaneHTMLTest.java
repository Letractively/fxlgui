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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.table.util.api.ILazyScrollPane;
import co.fxl.gui.table.util.impl.LazyScrollPanelImplWidgetProvider;
import co.fxl.gui.table.util.test.HTMLDecorator.Resolver;

class LazyScrollPaneHTMLTest implements IClickListener {

	private ILazyScrollPane widget;

	void run(IDisplay display) {
		display.fullscreen();
		display.register(new LazyScrollPanelImplWidgetProvider());
		widget = (ILazyScrollPane) display.container().panel().vertical().add()
				.widget(ILazyScrollPane.class);
		display.visible(true);
		widget.size(1000);
		widget.minRowHeight(20);
		widget.height(900);
		HTMLDecorator d = new HTMLDecorator();
		d.template("<b>A</b> = ${A}, <b>B</b> = ${B}");
		d.addParameter("${A}").resolver(new Resolver() {
			@Override
			public String resolve(int rowIndex) {
				return "AR" + rowIndex;
			}
		});
		d.addParameter("${B}").resolver(new Resolver() {
			@Override
			public String resolve(int rowIndex) {
				return "BR" + rowIndex;
			}
		});
		widget.decorator(d);
		widget.visible(true);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new LazyScrollPaneHTMLTest().run(display);
	}

	@Override
	public void onClick() {
		throw new MethodNotImplementedException();
	}
}
