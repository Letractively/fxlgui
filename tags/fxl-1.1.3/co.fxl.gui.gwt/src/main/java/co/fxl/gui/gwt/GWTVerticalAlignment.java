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

import co.fxl.gui.api.IAlignment;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;

class GWTVerticalAlignment<T> implements IAlignment<T> {

	private final T returnType;
	private final HasVerticalAlignment widget;

	GWTVerticalAlignment(T returnType, HasVerticalAlignment panel) {
		this.returnType = returnType;
		this.widget = panel;
	}

	@Override
	public T center() {
		setAlignment(HorizontalPanel.ALIGN_MIDDLE);
		return returnType;
	}

	@Override
	public T begin() {
		setAlignment(HorizontalPanel.ALIGN_TOP);
		return returnType;
	}

	@Override
	public T end() {
		setAlignment(HorizontalPanel.ALIGN_BOTTOM);
		return returnType;
	}

	private void setAlignment(VerticalAlignmentConstant constant) {
		widget.setVerticalAlignment(constant);
	}
}