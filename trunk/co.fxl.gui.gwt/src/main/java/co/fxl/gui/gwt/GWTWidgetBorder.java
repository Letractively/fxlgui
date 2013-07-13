/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.gwt;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.impl.Env;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class GWTWidgetBorder extends GWTBorder {

	private Element element;
	private String lastBorderType;
	private Widget widget;

	public GWTWidgetBorder(Element element) {
		this.element = element;
	}

	public GWTWidgetBorder(Widget widget) {
		this(widget.getElement());
		this.widget = widget;
	}

	@Override
	protected void shadow(int pixel) {
		if (widget != null) {

			// antialiasing is missing

			if (GWTDisplay.isInternetExplorer8OrBelow()) {
				width(1);
			} else
				widget.addStyleName(pixel == 4 ? "shadowblur"
						: "shadowblurLight");
		}
	}

	@Override
	public IBorder remove() {
		if (lastBorderType != null) {
			if (!lastBorderType.equals(borderType))
				element.getStyle().clearProperty(lastBorderType);
			else if (lastBorderType.equals(GWTBorder.BORDER_BOTTOM_LESS)) {
				DOM.setStyleAttribute(element, "borderTop", "none");
				DOM.setStyleAttribute(element, "borderLeft", "none");
				DOM.setStyleAttribute(element, "borderRight", "none");
			}
		} else {
			element.getStyle().setBorderWidth(0, Unit.PX);
		}
		return this;
	}

	@Override
	protected void update() {
		remove();
		// if (roundShadow)
		// element.addClassName("quicksearchshadow");
		if (!top || !left || !right || !bottom) {
			if (top)
				DOM.setStyleAttribute(element, "borderTop", width + "px "
						+ color + " " + style);
			if (left)
				DOM.setStyleAttribute(element, "borderLeft", width + "px "
						+ color + " " + style);
			if (right)
				DOM.setStyleAttribute(element, "borderRight", width + "px "
						+ color + " " + style);
			if (bottom)
				DOM.setStyleAttribute(element, "borderBottom", width + "px "
						+ color + " " + style);
		}
		if (borderType.equals(GWTBorder.BORDER_BOTTOM_LESS)) {
			DOM.setStyleAttribute(element, "borderTop", width + "px " + color
					+ " " + style);
			DOM.setStyleAttribute(element, "borderLeft", width + "px " + color
					+ " " + style);
			DOM.setStyleAttribute(element, "borderRight", width + "px " + color
					+ " " + style);
		} else if (borderType.equals(GWTBorder.BORDER_ROUNDED)) {
			if (!top && !left && !right && !bottom)
				DOM.setStyleAttribute(element, "border", width + "px " + color
						+ " " + style);
			String attr = "borderRadius";
			if (Env.runtime().leq(Env.FIREFOX, 12))
				attr = "-moz-border-radius";
			// TODO Look: Firefox/IE/Opera: Rounded Corners in
			// Firefox/Opera/Firefox dont work, use style
			try {
				DOM.setStyleAttribute(element, attr, roundWidth + "px");
			} catch (AssertionError e) {
			}
			if (!roundBottom) {
				element.getStyle().clearProperty("borderBottomLeftRadius");
				element.getStyle().clearProperty("borderBottomRightRadius");
			}
			if (!roundLeft) {
				element.getStyle().clearProperty("borderBottomLeftRadius");
				element.getStyle().clearProperty("borderTopLeftRadius");
			}
			if (!roundRight) {
				element.getStyle().clearProperty("borderBottomRightRadius");
				element.getStyle().clearProperty("borderTopRightRadius");
			}
			if (!roundTop) {
				element.getStyle().clearProperty("borderTopLeftRadius");
				element.getStyle().clearProperty("borderTopRightRadius");
			}
		} else {
			DOM.setStyleAttribute(element, borderType, width + "px " + color
					+ " " + style);
		}
		lastBorderType = borderType;
	}
}
