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

public @interface Style {

	public enum Window {
		DIALOG, VIEWLIST, HEADER, CONTENT, FOOTER, TITLE, MAIN, SIDE, NONE;
	}

	public enum List {
		CHOICE, NUMBER, ENTRY, NONE;
	}

	public enum Element {
		HYPERLINK, SEPARATOR, INPUT, BUTTON, BORDER, BACKGROUND, LABEL, COMBOBOX, NONE;
	}

	public enum Status {
		ACTIVE, INACTIVE, HIGHLIGHT, UNHIGHLIGHT, ERROR, NONE;
	}

	Window window() default Window.NONE;

	List list() default List.NONE;

	Element element() default Element.NONE;

	Status status() default Status.NONE;
}
