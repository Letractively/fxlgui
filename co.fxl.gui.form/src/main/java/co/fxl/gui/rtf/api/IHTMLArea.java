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
package co.fxl.gui.rtf.api;

import co.fxl.gui.api.ITextArea;

public interface IHTMLArea extends ITextArea {

	public final String TOGGLE_PREFIX = "TOGGLE_";

	public enum Formatting {

		TOGGLE_BOLD, TOGGLE_ITALIC, TOGGLE_STRIKETHROUGH, TOGGLE_SUBSCRIPT, TOGGLE_SUPERSCRIPT, TOGGLE_UNDERLINE, JUSTIFY_LEFT, JUSTIFY_CENTER, JUSTIFY_RIGHT, INDENT_RIGHT, INDENT_LEFT, INSERT_HORIZONTAL_RULE, INSERT_ORDERED_LIST, INSERT_UNORDERED_LIST,
		// INSERT_IMAGE, CREATE_LINK, REMOVE_LINK,
		REMOVE_FORMATTING
	}

	void toggle(Formatting f);

	boolean is(Formatting f);

	void apply(Formatting f);

	boolean supports(Formatting f);

	String html();

	IHTMLArea html(String html);

	void notifyChange();

	IHTMLArea closeListener(IClickListener l);

	int htmlCursorPosition();

}
