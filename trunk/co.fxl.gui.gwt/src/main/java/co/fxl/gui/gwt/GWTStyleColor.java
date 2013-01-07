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
import com.google.gwt.user.client.ui.Widget;

public class GWTStyleColor extends ColorTemplate implements IColor {

	public class Gradient implements IGradient {

		private GWTStyleColor original;
		int[] fallback = null;

		Gradient(GWTStyleColor original) {
			this.original = original;
		}

		@Override
		public IColor vertical() {
			return new GWTStyleColor(getWidget(), new Style() {

				@Override
				public void addStyleName(String style) {
					throw new UnsupportedOperationException();
				}
			}) {

				protected void setColor(String color) {
					GWTWidgetStyle gwtWidgetStyle = (GWTWidgetStyle) original.style;
					Element element = gwtWidgetStyle.widget.getElement();
					String attribute = "background";
					String gradient = "-webkit-gradient(linear, left top, left bottom, from("
							+ original.color + "), to(" + color + "))";
					if (GWTDisplay.isInternetExplorer()) {
						// if (!GWTDisplay.isInternetExplorer8OrBelow()) {
						// attribute = "filter";
						// gradient =
						// "progid:DXImageTransform.Microsoft.gradient(startColorstr='"
						// + original.color
						// + "', endColorstr='"
						// + color + "')";
						// DOM.setStyleAttribute(element, "zoom", "1");
						// } else {
						setImageGradient(element);
						attribute = null;
						// }
					} else if (GWTDisplay.isFirefox()) {
						gradient = "-moz-linear-gradient(top, "
								+ original.color + ", " + color + ")";
					} else if (GWTDisplay.isOpera()) {
						setImageGradient(element);
						attribute = null;
					}
					if (attribute != null)
						DOM.setStyleAttribute(element, attribute, gradient);
				}

				protected void setImageGradient(Element element) {
					String fb = original.color;
					if (fallback != null)
						fb = toString(fallback[0], fallback[1], fallback[2]);
					DOM.setStyleAttribute(element, "background", fb);
					String file = "gradient_" + original.rgb[0] + "_" + rgb[0]
							+ ".png";
					String attribute0 = "backgroundImage";
					String resourceURI = GWTImage.getResourceURI(file);
					String gradient0 = "url(" + resourceURI + ")";
					DOM.setStyleAttribute(element, "backgroundRepeat",
							"repeat-x");
					DOM.setStyleAttribute(element, attribute0, gradient0);
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
	private Widget widget;

	public GWTStyleColor(Style style) {
		this.style = style;
		if (style != null && style instanceof GWTWidgetStyle)
			widget = getWidget();
	}

	public GWTStyleColor(Widget widget, Style style2) {
		this.widget = widget;
		this.style = style2;
	}

	@Override
	public IColor setRGB(int r, int g, int b) {
		String color = toString(r, g, b);
		rgb = new int[] { r, g, b };
		setColor(color);
		return this;
	}

	protected void setColor(String color) {
		this.color = color;
		com.google.gwt.dom.client.Style stylable = stylable();
		setColor(color, stylable);
	}

	private com.google.gwt.dom.client.Style stylable() {
		Widget widget = getWidget();
		com.google.gwt.dom.client.Style stylable = widget.getElement()
				.getStyle();
		return stylable;
	}

	protected Widget getWidget() {
		GWTWidgetStyle gwtWidgetStyle = (GWTWidgetStyle) style;
		Widget widget = gwtWidgetStyle.widget;
		return widget;
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
		if (!GWTDisplay.isInternetExplorer8OrBelow())
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
		if (!GWTDisplay.isInternetExplorer8OrBelow())
			DOM.setStyleAttribute(element, "filter", "none");
	}

	@Override
	public IGradient gradient() {
		return new Gradient(this);
	}
}
