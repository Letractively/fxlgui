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

class GWTFamily extends GWTWidgetStyle implements IFamily {

	private GWTFont font;

	GWTFamily(GWTFont font) {
		super("font-family-", font.widget);
		this.font = font;
	}

	@Override
	public IFont arial() {
		addStyleName("arial");
		return font;
	}

	@Override
	public IFont timesNewRoman() {
		addStyleName("times");
		return font;
	}

	@Override
	public IFont verdana() {
		addStyleName("verdana");
		return font;
	}

	@Override
	public IFont name(String font) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IFont courier() {
		addStyleName("courier");
		return font;
	}

	@Override
	public IFont georgia() {
		addStyleName("georgia");
		return font;
	}

	@Override
	public IFont garamond() {
		addStyleName("garamond");
		return font;
	}

	@Override
	public IFont calibri() {
		addStyleName("calibri");
		return font;
	}

	@Override
	public IFont helvetica() {
		addStyleName("helvetica");
		return font;
	}

	@Override
	public IFont lucinda() {
		addStyleName("lucinda");
		return font;
	}
}