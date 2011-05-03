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

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IColored.IGradient;
import co.fxl.gui.api.template.ColorTemplate;

public class GWTStyleColor extends ColorTemplate implements IColor {

	public class Gradient implements IGradient {

		@Override
		public IColor vertical() {
			return GWTStyleColor.this;
		}

	}

	private Style style;

	public GWTStyleColor(Style style) {
		this.style = style;
	}

	@Override
	public IColor setRGB(int r, int g, int b) {
		String color = toString(r, g, b);
		setColor(color);
		return this;
	}

	protected void setColor(String color) {
		com.google.gwt.dom.client.Style stylable = stylable();
		setColor(color, stylable);
	}

	private com.google.gwt.dom.client.Style stylable() {
		GWTWidgetStyle gwtWidgetStyle = (GWTWidgetStyle) style;
		com.google.gwt.dom.client.Style stylable = gwtWidgetStyle.widget
				.getElement().getStyle();
		return stylable;
	}

	String toString(int r, int g, int b) {
		return "#" + toHexString(r) + toHexString(g) + toHexString(b);
	}

	private String toHexString(int r) {
		String hex = Integer.toHexString(r);
		while (hex.length() < 2)
			hex = "0" + hex;
		return hex;
	}

	void setColor(String color, com.google.gwt.dom.client.Style stylable) {
		stylable.setColor(color);
	}

	@Override
	public IColor remove() {
		stylable().clearBackgroundColor();
		stylable().clearColor();
		return this;
	}

	@Override
	public IGradient gradient() {
		return new Gradient();
	}
}
