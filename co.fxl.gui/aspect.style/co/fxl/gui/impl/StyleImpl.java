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

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IElement;
import co.fxl.gui.impl.Style.Element;
import co.fxl.gui.impl.Style.Outline;
import co.fxl.gui.impl.Style.Status;
import co.fxl.gui.impl.Style.Window;
import co.fxl.gui.style.Styles;

public class StyleImpl {

	public static void setUp() {
		// Styles.instance().register(new IStyle<IPanel<?>>() {
		// @Override
		// public void style(IPanel<?> panel) {
		// panel.color().remove();
		// panel.color().white();
		// panel.border().style().rounded().color().lightgray();
		// }
		// }, Window.SIDE, Outline.HEADER, Element.BUTTON);
		// Styles.instance().register(new IStyle<IPanel<?>>() {
		// @Override
		// public void style(IPanel<?> panel) {
		// IBorder border = panel.border();
		// border.color().lightgray();
		// border.style().top();
		// }
		// }, Window.SIDE, Outline.FOOTER);
	}

	public static boolean apply(Class<?> clazz, String methodName,
			IElement<?> element) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(methodName)) {
				Style style = m.getAnnotation(Style.class);
				List<Object> styles = new LinkedList<Object>();
				if (!style.window().equals(Window.NONE))
					styles.add(style.window());
				if (!style.outline().equals(Outline.NONE))
					styles.add(style.outline());
				if (!style.list().equals(Style.List.NONE))
					styles.add(style.list());
				if (!style.element().equals(Element.NONE))
					styles.add(style.element());
				if (!style.status().equals(Status.NONE))
					styles.add(style.status());
				return Styles.instance().style(element, styles.toArray());
			}
		}
		return false;
	}
}
