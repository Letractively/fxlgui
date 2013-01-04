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

import com.google.gwt.user.client.ui.PopupPanel;

class PopUpPanelWidget extends PopupPanel {

	int w;
	int h;
	int x = -1;
	int y = -1;

	PopUpPanelWidget(boolean b, boolean c) {
		super(b, c);
	}

	public void size(int w, int h) {
		width(w);
		height(h);
	}

	void height(int h) {
		setHeight(h + "px");
		this.h = h;
	}

	void width(int w) {
		setWidth(w + "px");
		this.w = w;
	}

	int offsetX() {
		if (x != -1)
			return x;
		return getPopupLeft();
	}

	int offsetY() {
		if (y != -1)
			return y;
		return getPopupTop();
	}

}
