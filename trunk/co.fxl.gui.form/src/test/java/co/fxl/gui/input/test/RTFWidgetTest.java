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

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.input.api.IRTFWidget;
import co.fxl.gui.input.api.IRTFWidget.IComposite;
import co.fxl.gui.input.impl.RTFWidgetImplProvider;

public class RTFWidgetTest {

	public void run(IDisplay display) {
		display.register(new RTFWidgetImplProvider());
		IRTFWidget w = (IRTFWidget) display.container()
				.widget(IRTFWidget.class);
		w.addToken("#", "{#}");
		w.addToken("name", "{name}");
		IComposite c = w.addComposite();
		IComposite rc = c.addComposite("ReleaseCycle");
		rc.addComposite("id").token("ReleaseCycle.id");
		rc.addComposite("version").token("ReleaseCycle.version");
		IComposite rf = c.addComposite("ReleaseFolder");
		rf.addComposite("id").token("ReleaseFolder.id");
		rf.addComposite("version").token("ReleaseFolder.version");
		w.visible(true);
		display.visible(true);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new RTFWidgetTest().run(display);
	}
}
