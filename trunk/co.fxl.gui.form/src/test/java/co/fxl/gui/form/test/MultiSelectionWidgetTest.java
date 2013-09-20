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
package co.fxl.gui.form.test;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.form.api.IMultiSelectionWidget;
import co.fxl.gui.form.api.IMultiSelectionWidget.IMultiSelectionAdapter;
import co.fxl.gui.form.impl.MultiSelectionWidgetImplProvider;
import co.fxl.gui.impl.Display;

class MultiSelectionWidgetTest {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new MultiSelectionWidgetTest().run(display);
	}

	private void run(IDisplay display) {
		Display.instance().register(new MultiSelectionWidgetImplProvider());
		IMultiSelectionWidget widget = Display.instance().container()
				.widget(IMultiSelectionWidget.class);
		widget.adapter(new IMultiSelectionAdapter() {

			@Override
			public void query(String text, ICallback<List<String>> cb) {
				List<String> l = new LinkedList<String>();
				l.add("usergroup:u1");
				l.add("user:u2");
				cb.onSuccess(l);
			}

			@Override
			public String label(String object) {
				return object.substring(object.indexOf(":") + 1);
			}

			@Override
			public String icon(String object) {
				return "cancel.png";
			}

			@Override
			public String create(String text) {
				return "email:" + text;
			}

		});
		display.fullscreen().visible(true);
	}
}
