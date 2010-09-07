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

public class CreateCSS {

	private static final String DDDDDD = "#DDDDDD";
	private static final String LIGHTGRAY = "lightgray";
	private static final String ULTRALIGHTGRAY = "ultralightgray";
	private static final String LIGHTGREEN = "lightgreen";
	private static final String EEEEEE = "#EEEEEE";
	private static String[] colors = new String[] { "white", "blue", "red",
			"black", "gray", "yellow", "green" };

	public static void main(String[] args) {
		StringBuilder content = new StringBuilder();
		addFontSizes(content);
		addFontWeights(content);
		addFontFamilies(content);
		content.append(".font-underline { text-decoration: underline }\n");
		addColors(content);
		addBackgroundColors(content);
		addBorderColors(content);
		addBorderWidth(content);
		addBorderStyle(content);
		addCursors(content);
		System.out.println(content.toString());
	}

	private static void addCursors(StringBuilder content) {
		String[] cursors = new String[] { "wait", "pointer", "crosshair",
				"help", "move" };
		for (int i = 0; i < cursors.length; i++)
			content.append(".cursor-" + cursors[i] + " { cursor: " + cursors[i]
					+ " }\n");
	}

	private static void addBorderStyle(StringBuilder content) {
		String[] weights = new String[] { "dashed", "solid", "dotted" };
		for (int i = 0; i < weights.length; i++)
			content.append(".border-style-" + weights[i] + " { border-style: "
					+ weights[i] + " }\n");
	}

	private static void addBorderWidth(StringBuilder content) {
		for (int i = 1; i <= 20; i++)
			content.append(".border-width-" + i + " { border-width: " + i
					+ " }\n");
	}

	private static void addBorderColors(StringBuilder content) {
		for (int i = 0; i < colors.length; i++)
			content.append(".border-color-" + colors[i] + " { border-color: "
					+ colors[i] + " }\n");
		content.append(".border-color-" + LIGHTGRAY + " { border-color: "
				+ DDDDDD + " }\n");
		content.append(".border-color-" + ULTRALIGHTGRAY + " { border-color: "
				+ EEEEEE + " }\n");
		content.append(".border-color-" + LIGHTGREEN + "" + " { border-color: "
				+ "88FF88" + " }\n");
	}

	private static void addBackgroundColors(StringBuilder content) {
		for (int i = 0; i < colors.length; i++)
			content.append(".background-color-" + colors[i]
					+ " { background-color: " + colors[i] + " }\n");
		content.append(".background-color-" + LIGHTGRAY + ""
				+ " { background-color: " + DDDDDD + " }\n");
		content.append(".background-color-" + ULTRALIGHTGRAY + ""
				+ " { background-color: " + EEEEEE + " }\n");
		content.append(".background-color-" + LIGHTGREEN + ""
				+ " { background-color: " + "88FF88" + " }\n");
	}

	private static String[] addColors(StringBuilder content) {
		for (int i = 0; i < colors.length; i++)
			content.append(".color-" + colors[i] + " { color: " + colors[i]
					+ " }\n");
		content.append(".color-" + LIGHTGRAY + "" + " { color: " + DDDDDD
				+ " }\n");
		content.append(".color-" + ULTRALIGHTGRAY + "" + " { color: " + EEEEEE
				+ " }\n");
		content.append(".color-" + LIGHTGREEN + "" + " { color: " + "88FF88"
				+ " }\n");
		return colors;
	}

	private static void addFontFamilies(StringBuilder content) {
		String[] families = new String[] { "arial", "times", "verdana" };
		for (int i = 0; i < families.length; i++)
			content.append(".font-family-" + families[i] + " { font-family: "
					+ families[i] + " }\n");
	}

	private static void addFontWeights(StringBuilder content) {
		String[] weights = new String[] { "bold", "normal", "light" };
		for (int i = 0; i < weights.length; i++)
			content.append(".font-weight-" + weights[i] + " { font-weight: "
					+ weights[i] + " }\n");
	}

	private static void addFontSizes(StringBuilder content) {
		for (int i = 1; i <= 30; i++)
			content.append(".font-size-" + i + "px { font-size: " + i
					+ "px }\n");
	}
}
