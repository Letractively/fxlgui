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
package co.fxl.gui.api.template;

public class HTMLText {

	public String text = "";
	public boolean center = false;
	public boolean underline = false;
	public boolean autoWrap = true;

	public void setText(String text) {
		if (text == null)
			text = "";
		this.text = text;
	}

	private String center(String text) {
		return "<p align='center'>" + text + "</p>";
	}

	private String html(String toString) {
		toString = toString.replace("\n", "<br/>");
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < toString.length(); i++) {
			char c = toString.charAt(i);
			switch (c) {
			case 'ä':
				b.append("&auml;");
				break;
			case 'ö':
				b.append("&ouml;");
				break;
			case 'ü':
				b.append("&uuml;");
				break;
			case 'Ä':
				b.append("&Auml;");
				break;
			case 'Ö':
				b.append("&Ouml;");
				break;
			case 'Ü':
				b.append("&Uuml;");
				break;
			default:
				b.append(c);
				break;
			}
		}
		return "<html>" + b.toString() + "</html>";
	}

	private String underline(String toString) {
		return "<u>" + toString + "</u>";
	}

	private String noWrap(String toString) {
		return "<p style='white-space: nowrap;'>" + text + "</p>";
	}

	@Override
	public String toString() {
		String toString = text.replace("<", "&#060;").replace("\n", "<br>");
		if (center)
			toString = center(toString);
		if (underline)
			toString = underline(toString);
		if (!autoWrap)
			toString = noWrap(toString);
		return html(toString);
	}
}
