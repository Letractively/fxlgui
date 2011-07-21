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

import co.fxl.gui.i18n.api.II18N;

public class I18N {

	public static final boolean ENABLED = false;
	private static II18N instance = new II18N() {

		@Override
		public String translate(String text) {
			return text;
		}
	};

	private I18N() {
	}

	public static void register(II18N i18N) {
		assert ENABLED;
		instance = i18N;
	}

	public static II18N instance() {
		return instance;
	}
}
