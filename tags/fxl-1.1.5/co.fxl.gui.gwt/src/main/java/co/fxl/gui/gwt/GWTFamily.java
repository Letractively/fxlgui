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

import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IFontElement.IFont.IFamily;

class GWTFamily implements IFamily {

	// TODO Optimization: use font.style.setFontFamily(...)

	private GWTFont font;

	GWTFamily(GWTFont font) {
		this.font = font;
	}

	@Override
	public IFont arial() {
		name("Arial");
		return font;
	}

	@Override
	public IFont openSans() {
		name("'Open Sans'");
		return font;
	}

	@Override
	public IFont timesNewRoman() {
		name("times");
		return font;
	}

	@Override
	public IFont verdana() {
		name("verdana");
		return font;
	}

	@Override
	public IFont name(String font) {
		this.font.style.setProperty("fontFamily", font);
		return this.font;
	}

	@Override
	public IFont courier() {
		name("courier");
		return font;
	}

	@Override
	public IFont georgia() {
		name("georgia");
		return font;
	}

	@Override
	public IFont garamond() {
		name("garamond");
		return font;
	}

	@Override
	public IFont calibri() {
		name("calibri");
		return font;
	}

	@Override
	public IFont helvetica() {
		name("helvetica");
		return font;
	}

	@Override
	public IFont lucinda() {
		name("'Lucinda Sans'");
		return font;
	}
}