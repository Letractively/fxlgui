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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFontElement.IFont;

import com.google.gwt.user.client.ui.Widget;

class GWTTextAreaTemplate<T extends Widget, R extends IElement<R>> extends
		GWTTextInput<T, R> {

	GWTTextAreaTemplate(GWTContainer<T> container) {
		super(container);
	}

	@Override
	public final IBorder border() {
		return new GWTWidgetBorder(container.widget);
	}

	@Override
	public final IColor color() {
		GWTWidgetStyle style = new GWTWidgetStyle("background-color-",
				container.widget);
		return new GWTStyleColor(style) {
			@Override
			void setColor(String color, com.google.gwt.dom.client.Style stylable) {
				stylable.setBackgroundColor(color);
			}
		};
	}

	@Override
	public final IFont font() {
		return new GWTFont(container.widget);
	}
}