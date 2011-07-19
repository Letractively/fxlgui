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

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.impl.Style.Element;
import co.fxl.gui.impl.Style.Outline;
import co.fxl.gui.impl.Style.Status;
import co.fxl.gui.impl.Style.Window;

privileged aspect StyleAspect {

	declare @method: 
	public void WidgetTitle.styleHeader(IPanel<?>):
	@Style(window = Window.ALL, outline = Outline.HEADER);

	declare @method: 
	public static void WidgetTitle.styleFooter(IPanel<?>):
	@Style(window = Window.ALL, outline = Outline.FOOTER);

	declare @method: 
	public void WidgetTitle.stylePanel(IPanel<?>):
	@Style(window = Window.ALL, outline = Outline.BACKGROUND);

	after(IElement<?> element) : 
	execution(@Style * *.*(IElement+)) 
	&& args(element) 
	&& if(co.fxl.gui.style.impl.Style.enabled) {
		List<Object> styles = getAnnotation(thisJoinPoint);
		co.fxl.gui.style.impl.Style.instance().style(element, styles);
	}

	List<Object> getAnnotation(JoinPoint joinPoint) {
		Signature sig = joinPoint.getSignature();
		Class<?> c = sig.getDeclaringType();
		String methodName = sig.getName();
		for (Method m : c.getMethods()) {
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
				return styles;
			}
		}
		return null;
	}
}
