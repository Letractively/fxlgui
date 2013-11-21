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
package co.fxl.gui.rtf.impl;

import co.fxl.gui.rtf.api.IHTMLArea;
import co.fxl.gui.rtf.api.IHTMLArea.Formatting;

public interface RichTextToolbarTextAdp {

	void notifyChange();

	IHTMLArea insertHTML(String string);

	void apply(Formatting f);

	IHTMLArea insertImage(String tf);

	IHTMLArea insertHyperlink(String tf);

	void toggle(Formatting f);

	boolean is(Formatting f);

	boolean is(String tag);

	boolean supports(Formatting f);

	String textColor();

	void textColor(String color);

	boolean supportsColor();

}
