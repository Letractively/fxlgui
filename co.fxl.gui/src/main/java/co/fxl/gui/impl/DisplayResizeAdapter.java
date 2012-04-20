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
package co.fxl.gui.impl;

import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IDisplay.IResizeListener;

public class DisplayResizeAdapter {

	private static int decrement = 0;

	public static void addDecrement(int decrement) {
		DisplayResizeAdapter.decrement += decrement;
	}

	public static IResizeConfiguration addResizeListener(
			IResizeListener listener) {
		return addResizeListener(listener, false);
	}

	public static IResizeConfiguration addResizeListener(
			final IResizeListener listener, boolean call) {
		IResizeListener adp = new IResizeListener() {
			@Override
			public boolean onResize(int width, int height) {
				return listener.onResize(width, height - decrement);
			}
		};
		IResizeConfiguration singleton = Display.instance()
				.addResizeListener(adp).singleton();
		if (call)
			adp.onResize(Display.instance().width(), Display.instance()
					.height());
		return singleton;
	}

	public static int withDecrement(int inc) {
		return decrement + inc;
	}

}
