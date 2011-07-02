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
package co.fxl.gui.mdt.test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.mdt.api.IViewConfiguration;
import co.fxl.gui.mdt.impl.ViewWidget;

public class ViewWidgetTest {

	void run(IDisplay display) {
		IVerticalPanel p = display.container().panel().vertical();
		IVerticalPanel v = p.add().panel().dock().left().panel().vertical()
				.width(300);
		ViewWidget vw = new ViewWidget(v.add().panel(),
				Arrays.asList(new String[] { "A", "B" }), "A", true, false);
		vw.addUpdateListener(new IUpdateListener<IViewConfiguration>() {
			@Override
			public void onUpdate(IViewConfiguration value) {
				System.out.println(value);
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
		new ViewWidgetTest().run(display);
	}
}
