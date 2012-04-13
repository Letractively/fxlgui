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
package co.fxl.gui.i18n.impl;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.i18n.api.II18N;

public class I18N {

	public static boolean ENABLED = true;
	private static II18N instance = new II18N() {

		@Override
		public String translate(String text) {
			return text;
		}

		@Override
		public boolean active(boolean active) {
			return true;
		}
	};
	private static Map<String, II18N> i18ns = new HashMap<String, II18N>();
	private static String defaultI18N;

	public static void nameDefault(String name) {
		defaultI18N = name;
	}

	private I18N() {
	}

	public static boolean isDefined(String name) {
		return i18ns.containsKey(name);
	}

	public static void register(String language, II18N i18N) {
		i18ns.put(language, i18N);
	}

	public static boolean active(boolean active) {
		return instance == null ? false : instance.active(active);
	}

	public static void activate(String language) {
		if (language == null || language.equals(defaultI18N)) {
			ENABLED = false;
			instance = null;
		} else {
			ENABLED = true;
			instance = i18ns.get(language);
		}
	}

	public static II18N instance() {
		assert ENABLED;
		return instance;
	}
}
