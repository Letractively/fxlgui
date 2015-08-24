/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import co.fxl.gui.api.IHorizontalLine;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;

class GWTHorizontalLine extends GWTElement<HTML, IHorizontalLine> implements
		IHorizontalLine {

	GWTHorizontalLine(GWTContainer<HTML> container) {
		super(container);
		container.widget
				.setHTML("<hr style=\"height:1px; border:none; color:lightgray; background-color:lightgray;\"/>");
	}

	@Override
	public IColor color() {
		GWTWidgetStyle style = new GWTWidgetStyle("color-", container.widget);
		return new GWTStyleColor(style) {

			@Override
			void setColor(String color, com.google.gwt.dom.client.Style stylable) {
				Style style = container.widget.getElement().getStyle();
				style.setBackgroundColor(color);
				style.setColor(color);
			}
		};
	}
}
