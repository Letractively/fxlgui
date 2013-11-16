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
import co.fxl.gui.impl.ColorTemplate;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

public class GWTFont extends GWTWidgetStyle implements IFont {

	Style style;

	public GWTFont(Widget label) {
		super("font-size-", label);
		style = label.getElement().getStyle();
	}

	@Override
	public IColor color() {
		return new ColorTemplate() {

			@Override
			public IColor remove() {
				style.clearColor();
				return this;
			}

			@Override
			protected IColor setRGB(int r, int g, int b) {
				style.setColor(GWTStyleColor.toString(r, g, b));
				return this;
			}
		};
	}

	@Override
	public IFamily family() {
		return new GWTFamily(this);
	}

	@Override
	public IWeight weight() {
		return new IWeight() {

			@Override
			public IFont bold() {
				style.setFontWeight(FontWeight.BOLD);
				return GWTFont.this;
			}

			@Override
			public IFont italic() {
				style.setFontStyle(FontStyle.ITALIC);
				return GWTFont.this;
			}

			@Override
			public IFont plain() {
				style.setFontWeight(FontWeight.NORMAL);
				style.setFontStyle(FontStyle.NORMAL);
				return GWTFont.this;
			}

		};
		// return new GWTWeight(this);
	}

	@Override
	public IFont pixel(int i) {
		style.setFontSize(i, Unit.PX);
		return this;
	}

	@Override
	public IFont underline(boolean underline) {
		if (underline)
			style.setTextDecoration(TextDecoration.UNDERLINE);
		else
			style.setTextDecoration(TextDecoration.NONE);
		return this;
	}
}