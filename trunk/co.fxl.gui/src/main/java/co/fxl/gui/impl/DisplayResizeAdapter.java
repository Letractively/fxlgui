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

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IDisplay.IResizeListener;

class DisplayResizeAdapter {

	private static Map<String, Integer> decrement = new HashMap<String, Integer>();

	static void setDecrement(String string, Integer i) {
		setDecrement(string, i, false);
	}

	static void setDecrement(String string, Integer i, boolean notify) {
		if (i == 0)
			decrement.remove(string);
		else
			decrement.put(string, i);
		if (notify) {
			notifyResizeListeners();
		}
	}

	private static void notifyResizeListeners() {
		Display.instance().notifyResizeListeners();
	}

	static void fire(IResizeListener listener) {
		Display.instance().notifyResizeListener(listener);
	}

	static IResizeConfiguration addResizeListener(IResizeListener listener) {
		return addResizeListener(listener, false);
	}

	static IResizeConfiguration addResizeListener(
			final IResizeListener listener, boolean call) {
		IResizeConfiguration singleton = Display.instance()
				.addResizeListener(listener).singleton();
		if (call)
			fire(listener);
		return singleton;
	}

	static int decrement() {
		int i = 0;
		for (Integer s : decrement.values())
			i += s;
		return i;
	}

	static int withDecrement(int inc) {
		return decrement() + inc;
	}

	static int withDecrement(int e, int inc) {
		int i = decrement() + inc;
		// if (e < i) {
		// Log.instance().error(
		// "Error computing height in resize adapter " + e + "<" + i);
		// }
		return Math.max(e, i);
	}

}
