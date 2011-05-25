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
package co.fxl.gui.input.test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.input.api.IMultiComboBoxWidget;
import co.fxl.gui.input.impl.MultiComboBoxWidgetProvider;

class MultiComboBoxWidgetTest {

	void run(IDisplay display) {
		display.register(new MultiComboBoxWidgetProvider());
		final IVerticalPanel vertical = display.container().panel().vertical();
		IMultiComboBoxWidget widget = (IMultiComboBoxWidget) vertical.add()
				.widget(IMultiComboBoxWidget.class);
		for (int i = 0; i < 3; i++)
			widget.addText("Item " + i);
		widget.addUpdateListener(new IUpdateListener<String[]>() {

			@Override
			public void onUpdate(String[] value) {
				vertical.add().label().text(Arrays.asList(value).toString());
			}
		});
		display.fullscreen().visible(true);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new MultiComboBoxWidgetTest().run(display);
	}
}
