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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IElement;
import co.fxl.gui.style.api.IStyle;
import co.fxl.gui.style.api.IWindow;

public class StyleTemplate implements IStyle {

	private Map<String, IElementStyle<Object>> STYLES = new HashMap<String, IElementStyle<Object>>();

	protected StyleTemplate() {
	}

	@SuppressWarnings("unchecked")
	public void register(IElementStyle<?> style, Object... styles) {
		STYLES.put(toString(Arrays.asList(styles)),
				(IElementStyle<Object>) style);
	}

	@Override
	public void style(IElement<?> e, List<?> styles) {
		String iD = toString(styles);
		IElementStyle<Object> style = STYLES.get(iD);
		if (style != null) {
			style.style(e);
		}
	}

	private String toString(List<?> styles) {
		StringBuilder b = new StringBuilder(styles.get(0).toString());
		for (int i = 1; i < styles.size(); i++)
			b.append("+" + styles.get(i));
		return b.toString();
	}

	@Override
	public IWindow window() {
		throw new MethodNotImplementedException();
	}

}
