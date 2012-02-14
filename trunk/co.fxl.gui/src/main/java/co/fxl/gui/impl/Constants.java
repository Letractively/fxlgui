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

public class Constants {

	private static Map<String, Object> constants = new HashMap<String, Object>();

	public static void put(String id, Object o) {
		constants.put(id, o);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String string, T defaultValue) {
		if (constants.containsKey(string))
			return (T) constants.get(string);
		return defaultValue;
	}
}
