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
package co.fxl.gui.input.impl;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Heights;

class ElementPopUp {

	static Heights HEIGHTS = new Heights(0);
	private IPopUp popUp;
	private IElement<?> element;
	private int lines;

	ElementPopUp(IElement<?> element) {
		this.element = element;
	}

	void lines(int lines) {
		this.lines = lines;
	}

	IVerticalPanel create() {
		popUp = element.display().showPopUp().autoHide(true);
		HEIGHTS.decorateBorder(popUp);
		int w = Math.min(320, element.width());
		int h = Math.min(240, 2 + 19 * lines);
		popUp.size(w, h);
		popUp.offset(element.offsetX(), element.offsetY() + element.height());
		IScrollPane scrollPane = popUp.container().scrollPane();
		scrollPane.border().remove();
		scrollPane.color().white();
		// TODO refine, 16 = hack for GWT
		return scrollPane.viewPort().panel().vertical().width(w - 16);
	}

	void clear() {
		if (popUp != null) {
			popUp.visible(false);
			popUp = null;
		}
	}

	void visible(boolean b) {
		popUp.visible(b);
	}
}
