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
import co.fxl.gui.impl.ColorTemplate;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class GWTStyleColor extends ColorTemplate implements IColor {

	public class Gradient implements IGradient {

		private GWTStyleColor original;
		int[] fallback = null;

		Gradient(GWTStyleColor original) {
			this.original = original;
		}

		@Override
		public IColor vertical() {
			return new GWTStyleColor(new Style() {

				@Override
				public void addStyleName(String style) {
					throw new MethodNotImplementedException();
				}
			}) {

				protected void setColor(String color) {
					GWTWidgetStyle gwtWidgetStyle = (GWTWidgetStyle) original.style;
					Element element = gwtWidgetStyle.widget.getElement();
					String attribute = "background";
					String gradient = "-webkit-gradient(linear, left top, left bottom, from("
							+ original.color + "), to(" + color + "))";
					if (GWTDisplay.isInternetExplorer()) {

						// TODO Look: GWT: use repeating image a la
						// background-image: url(h64_FFAACC_FFAAAA.png);
						// background-repeat: repeat-x; background: mittelwert

						if (fallback != null) {
							gradient = toString(fallback[0], fallback[1],
									fallback[2]);
						} else {
							attribute = "filter";
							gradient = "progid:DXImageTransform.Microsoft.gradient(startColorstr='"
									+ original.color
									+ "', endColorstr='"
									+ color + "')";
							DOM.setStyleAttribute(element, "zoom", "1");
						}
						// DOM.setStyleAttribute(element, "-ms-" + attribute,
						// gradient);
					} else if (GWTDisplay.isFirefox()) {
						gradient = "-moz-linear-gradient(top, "
								+ original.color + ", " + color + ")";
					} else if (GWTDisplay.isOpera()) {
						if (fallback != null) {
							gradient = toString(fallback[0], fallback[1],
									fallback[2]);
						} else {
							gradient = mix(original.color, color);
						}

						// TODO Look: GWT: use repeating image a la
						// background-image: url(h64_FFAACC_FFAAAA.png);
						// background-repeat: repeat-x; background: mittelwert

						// attribute = "background-image";
						// gradient = "url(gradient_"
						// + gwtWidgetStyle.widget.getOffsetHeight() + "_"
						// + original.color.substring(1).toLowerCase()
						// + "_" + color.substring(1).toLowerCase()
						// + ".png";
						// DOM.setStyleAttribute(element, "background-repeat",
						// "repeat-x");
						// DOM.setStyleAttribute(element, "background",
						// original.color);
					}
					DOM.setStyleAttribute(element, attribute, gradient);
				}
			};
		}

		@Override
		public IGradient fallback(int r, int g, int b) {
			fallback = new int[] { r, g, b };
			return this;
		}
	}

	static String mix(String color1, String color2) {
		int[] rgb1 = rgb(color1);
		int[] rgb2 = rgb(color2);
		return toString(mix(rgb1, rgb2, 0), mix(rgb1, rgb2, 1),
				mix(rgb1, rgb2, 2));
	}

	static int mix(int[] rgb1, int[] rgb2, int i) {
		return (rgb1[i] + rgb2[i]) / 2;
	}

	static int[] rgb(String color1) {
		return new int[] { fromHex(color1.substring(1, 3)),
				fromHex(color1.substring(3, 5)),
				fromHex(color1.substring(5, 7)) };
	}

	static int fromHex(String s) {
		int parseInt = Integer.parseInt(s, 16);
		return parseInt;
	}

	private Style style;
	private String color;

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
		this.color = color;
		com.google.gwt.dom.client.Style stylable = stylable();
		setColor(color, stylable);
	}

	private com.google.gwt.dom.client.Style stylable() {
		GWTWidgetStyle gwtWidgetStyle = (GWTWidgetStyle) style;
		com.google.gwt.dom.client.Style stylable = gwtWidgetStyle.widget
				.getElement().getStyle();
		return stylable;
	}

	static String toString(int r, int g, int b) {
		return "#" + toHexString(r) + toHexString(g) + toHexString(b);
	}

	static String toHexString(int r) {
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
		stylable().clearProperty("filter");
		stylable().clearProperty("background");
		setBackgroundNone();
		stylable().clearBackgroundColor();
		stylable().clearColor();
		return this;
	}

	protected void setBackgroundNone() {
		GWTWidgetStyle gwtWidgetStyle = (GWTWidgetStyle) style;
		Element element = gwtWidgetStyle.widget.getElement();
		DOM.setStyleAttribute(element, "background", "none");
		DOM.setStyleAttribute(element, "filter", "none");
	}

	@Override
	public IGradient gradient() {
		return new Gradient(this);
	}
}
