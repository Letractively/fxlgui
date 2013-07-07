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
package co.fxl.gui.style.impl;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.impl.Display;
import co.fxl.gui.style.api.IStyle;

public class Style {

	private static IStyle instance;
	private static Map<String, IStyle> styles = new HashMap<String, IStyle>();

	static {
		register(NinetyNineDesignsStyle.NAME, new NinetyNineDesignsStyle());
		register(GrayScaleStyle.NAME, new GrayScaleStyle());
		activate(GrayScaleStyle.NAME);
	}

	public static void register(String name, IStyle instance) {
		styles.put(name, instance);
	}

	public static void activate(String name) {
		instance = styles.get(name);
		Display.instance().font(instance.fontFamily(), instance.fontSize());
	}

	public static IStyle instance() {
		assert instance != null;
		return instance;
	}

}
