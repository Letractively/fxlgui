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

import co.fxl.gui.style.api.IStyle;

public class Style {

	public static boolean ENABLED = true;
	private static IStyle instance = new StyleImpl();
	private static Map<String, IStyle> styles = new HashMap<String, IStyle>();
	private static String defaultStyle = "DEFAULT";

	public static void nameDefault(String name) {
		defaultStyle = name;
	}

	public static boolean isDefined(String name) {
		return styles.containsKey(name);
	}

	public static void register(String name, IStyle instance) {
		styles.put(name, instance);
	}

	public static void activate(String name) {
		if (instance != null)
			instance.activate(false);
		if (name == null || name.equals(defaultStyle)) {
			ENABLED = false;
			instance = null;
		} else {
			ENABLED = true;
			instance = styles.get(name);
			instance.activate(true);
		}
	}

	public static IStyle instance() {
		assert instance != null;
		return instance;
	}

}
