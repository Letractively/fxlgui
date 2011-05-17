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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IVerticalPanel;

public class GUIAPITest {

	public void run(IDisplay display) {
		IVerticalPanel panel = display.container().panel().vertical()
				.spacing(10);
		decorate(panel);
		display.visible(true);
	}

	public void decorate(IVerticalPanel panel) {
		final IGridPanel grid = panel.add().panel().grid().spacing(10)
				.indent(10);
		grid.resize(5, 5);
		for (int i = 0; i < 5; i++) {
			addRow(grid, i, false);
		}
	}

	void addRow(final IGridPanel grid, final int i, boolean isNew) {
		for (int j = 0; j < 5; j++) {
			final boolean insert = j % 2 == 0;
			IGridCell c = grid.cell(j, i);
			String text = "row:" + i + " - column:" + j;
			if (!isNew) {
				c.button().text((insert ? "INS " : "REM ") + text)
						.addClickListener(new IClickListener() {

							@Override
							public void onClick() {
								if (!insert) {
									grid.row(i).remove();
								} else {
									grid.row(i).insert();
									addRow(grid, i, true);
								}
							}
						});
			} else
				c.label().text("INSERTED " + text);
		}
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new GUIAPITest().run(display);
	}
}
