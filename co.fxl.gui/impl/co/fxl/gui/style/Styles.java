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
package co.fxl.gui.style;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.impl.Style;

public class Styles {

	private static final Styles INSTANCE = new Styles();
	private Map<String, IStyle<Object>> STYLES = new HashMap<String, IStyle<Object>>();

	public static Styles instance() {
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public void register(IStyle<?> style, Object... iDs) {
		STYLES.put(toString(iDs), (IStyle<Object>) style);
	}

	public void style(Object e, Object... styles) {

		// TODO extract aspect
		Style.setUp();

		IStyle<Object> style = STYLES.get(toString(styles));
		if (style != null)
			style.style(e);
		else
			throw new RuntimeException(toString(styles));
	}

	private String toString(Object[] styles) {
		StringBuilder b = new StringBuilder(styles[0].toString());
		for (int i = 1; i < styles.length; i++)
			b.append("+" + styles[i]);
		return b.toString();
	}
}
