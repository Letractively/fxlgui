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
package co.fxl.gui.api.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.HorizontalScalingPanel;

public class HorizontalScalingPanelTest {

	public void run(IDisplay display) {
		IVerticalPanel panel = display.container().scrollPane().viewPort()
				.panel().vertical().size(500, 500).spacing(10);
		display.visible(true);
		HorizontalScalingPanel hsp = new HorizontalScalingPanel(panel.add(),false);
		for (int i = 0; i < 10; i++) {
			IHorizontalPanel spacing = hsp.add().panel().horizontal()
					.spacing(10);
			spacing.color().lightgray();
			spacing.border();
			spacing.add().label().text("I" + i);
		}
		hsp.width(200);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new HorizontalScalingPanelTest().run(display);
	}
}
