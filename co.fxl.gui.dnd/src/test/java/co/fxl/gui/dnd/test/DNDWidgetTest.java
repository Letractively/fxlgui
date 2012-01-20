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
package co.fxl.gui.dnd.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.dnd.api.IDNDWidget;
import co.fxl.gui.dnd.api.IDNDWidget.IHeights;
import co.fxl.gui.dnd.impl.DNDWidgetImplProvider;

class DNDWidgetTest implements IHeights {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new DNDWidgetTest().run(display);
	}

	private void run(IDisplay display) {
		display.register(new DNDWidgetImplProvider());
		IVerticalPanel v = display.container().panel().vertical();
		IVerticalPanel h = v.addSpace(50).add().panel().vertical()
				.size(100, 400);
		IDNDWidget dnd = v.add().widget(IDNDWidget.class);
		dnd.element(h);
		dnd.range(10, (400 / 22) + 10);
		dnd.size(100);
		dnd.heights(this);
		v.add().button().text("test").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
			}
		});
		display.fullscreen().visible(true);
	}

	@Override
	public int height(int index) {
		return 22;
	}
}
