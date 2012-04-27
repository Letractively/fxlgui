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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;

public class GWTFont extends GWTWidgetStyle implements IFont {

	private Style style;

	public GWTFont(Widget label) {
		super("font-size-", label);
		style = label.getElement().getStyle();
	}

	@Override
	public IColor color() {
		GWTWidgetStyle style = new GWTWidgetStyle("color-", widget);
		return new GWTStyleColor(style);
	}

	@Override
	public IFamily family() {
		return new GWTFamily(this);
	}

	@Override
	public IWeight weight() {
		return new GWTWeight(this);
	}

	@Override
	public IFont pixel(int i) {
		// TODO ... style.setFontSize(i, Unit.PX);
		addStyleName(i + "px");
		return this;
	}

	@Override
	public IFont underline(boolean underline) {
		if (underline)
			addToStyleNames("font-underline");
		else
			removeFromStyleNames("font-underline");
		return this;
	}
}