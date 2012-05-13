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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IElement;

public class DisplayResizeAdapter {

	private static Map<String, Integer> decrement = new HashMap<String, Integer>();
	private static List<IResizeListener> listeners = new LinkedList<IResizeListener>();

	public static void setDecrement(String string, Integer i) {
		setDecrement(string, i, false);
	}

	public static void setDecrement(String string, Integer i, boolean notify) {
		if (i == 0)
			decrement.remove(string);
		else
			decrement.put(string, i);
		if (notify) {
			for (IResizeListener l : new LinkedList<IResizeListener>(listeners))
				call(l);
		}
	}

	public static IResizeConfiguration addResizeListener(
			IResizeListener listener) {
		return addResizeListener(listener, false);
	}

	public static IResizeConfiguration addResizeListener(
			final IResizeListener listener, boolean call) {
		IResizeConfiguration singleton = Display.instance()
				.addResizeListener(listener).singleton();
		listeners.add(listener);
		if (call)
			call(listener);
		return singleton;
	}

	public static int decrement() {
		int i = 0;
		for (Integer s : decrement.values())
			i += s;
		return i;
	}

	public static int withDecrement(int inc) {
		return decrement() + inc;
	}

	public static void autoResize(final IElement<?> e) {
		autoResize(e, 0);
	}

	public static void autoResize(final IElement<?> e, final int dec) {
		autoResize(e, dec, false);
	}

	public static void autoResize(final IElement<?> e, final int dec, boolean b) {
		final IResizeListener listener = new IResizeListener() {
			@Override
			public boolean onResize(int width, int height) {
				if (!e.visible()) {
					remove(this);
					return false;
				}
				int offsetY = e.offsetY();
				// TODO ... un-hard-code
				if (offsetY < DisplayResizeAdapter.withDecrement(100))
					offsetY = DisplayResizeAdapter.withDecrement(100);
				int maxFromDisplay = height - offsetY - 10 - dec;
				if (maxFromDisplay > 0)
					e.height(maxFromDisplay);
				boolean visible = e.visible();
				if (!visible)
					remove(this);
				return visible;
			}
		};
		listeners.add(listener);
		Display.instance().addResizeListener(listener);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				call(listener);
			}
		};
		if (b) {
			Display.instance().invokeLater(runnable);
		} else
			runnable.run();
	}

	private static void remove(IResizeListener iResizeListener) {
		listeners.remove(iResizeListener);
	}

	private static void call(final IResizeListener listener) {
		listener.onResize(Display.instance().width(), Display.instance()
				.height());
	}

}
