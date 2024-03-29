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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

public class HTMLText implements RuntimeConstants {

	private static final String BORDER_RADIUS_ATTRIBUTE = FIREFOX_LEQ_12 ? "-moz-border-radius"
			: "border-radius";
	private static final boolean WHITE_BG_IF_NOT_DEFINED = false;
	public String text = "";
	public boolean center = false;
	public boolean underline = false;
	public boolean autoWrap = true;
	public boolean selectable = true;
	private boolean allowHTML = false;

	public void setText(String text) {
		if (text == null)
			text = "";
		this.text = text;
	}

	public void allowHTML(boolean allowHTML) {
		this.allowHTML = allowHTML;
	}

	private String center(String text) {
		return "<p align='center'>" + text + "</p>";
	}

	public static String html(String toString) {
		String string = plain(toString);
		return "<html>" + string + "</html>";
	}

	public static String plain(String toString) {
		toString = toString.replace("\n", "<br/>");
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < toString.length(); i++) {
			char c = toString.charAt(i);
			switch (c) {
			case 228:
				b.append("&auml;");
				break;
			case 246:
				b.append("&ouml;");
				break;
			case 252:
				b.append("&uuml;");
				break;
			case 196:
				b.append("&Auml;");
				break;
			case 214:
				b.append("&Ouml;");
				break;
			case 220:
				b.append("&Uuml;");
				break;
			default:
				b.append(c);
				break;
			}
		}
		String string = b.toString();
		return string;
	}

	private String underline(String toString) {
		return "<u>" + toString + "</u>";
	}

	private String noWrap(String toString) {
		return "<p style='white-space: nowrap;'>" + text + "</p>";
	}

	@Override
	public String toString() {
		String toString = allowHTML ? text : text.replace("<", "&#060;")
				.replace(">", "&#062;");
		toString = toString.replace("\n", "<br>");
		if (center)
			toString = center(toString);
		if (underline)
			toString = underline(toString);
		if (!autoWrap)
			toString = noWrap(toString);
		toString = html(toString);
		// if (!selectable)
		// toString = unselectable(toString);
		return toString;
	}

	// private String unselectable(String toString) {
	// return "<div unselectable=\"on\" class=\"unselectable\">" + toString
	// + "</div>";
	// }

	public static String textInput(String text, int width, String align) {
		return textInput(text, width, align, "");
	}

	public static String textInput(String text, int width, String align,
			String style) {
		return "<input class=\"gwt-TextInputBox\" style=\"width:"
				+ (width == -1 ? "100%;" : width + "px;")
				+ (align != null ? "text-align:" + align : "") + style
				+ "\" type=\"text\" value=\""
				+ (text != null ? escape(text) : "") + "\" readonly/>";
	}

	public static String escape(String text) {
		return text.replaceAll("<", "&#060;").replaceAll(">", "&#062;")
				.replaceAll("\"", "&#34;");
	}

	public static String removeMarkUp(String text) {
		StringBuilder b = new StringBuilder();
		boolean open = false;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch (c) {
			case '<':
				open = true;
				break;
			case '>':
				open = false;
				break;
			default:
				if (!open)
					b.append(c);
			}
		}
		return b.toString();
	}

	public static String styledText(String text, String color) {
		if (text == null)
			return "";
		String fontColor = "white";
		if (color == null) {
			color = WHITE_BG_IF_NOT_DEFINED ? ""
					: "background-color:rgb(235,235,235);";
			fontColor = "black";
		} else {
			color = "background-color: " + color;
		}
		String html = "<div class=\"gwt-HTML gwt-Label-FXL\" style=\"font-weight:bold; display: inline-block; color:"
				+ fontColor
				+ "; "
				+ BORDER_RADIUS_ATTRIBUTE
				+ ": 3px; "
				+ color
				+ "; padding: 2px 5px 2px 5px; font-size: 10px; \">"
				+ text + "</div>";
		return html;
	}
}
