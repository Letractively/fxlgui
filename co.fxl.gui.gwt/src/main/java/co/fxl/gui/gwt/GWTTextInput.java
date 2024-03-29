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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.gwt;

import com.google.gwt.user.client.ui.Widget;

public class GWTTextInput<T extends Widget, R> extends GWTElement<T, R> {

	public GWTTextInput() {
		super();
	}

	public GWTTextInput(GWTContainer<T> container) {
		super(container);
	}

	@Override
	public final int width() {
		return super.width() + 8 + IEDECREMENTW;
	}

	@Override
	public final R width(int width) {
		return (R) super.width(width - 8 - IEDECREMENTW);
	}

	@Override
	public int height() {
		return super.height() + IEDECREMENTH;
	}

	@Override
	public final R height(int height) {
		return (R) super.height(height - IEDECREMENTH);
	}

}
