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
package co.fxl.gui.impl;

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseOverElement;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;

public class HyperlinkMouseOverListener implements IMouseOverListener,
		IClickListener {

	private IFont font;
	private IClickable<?> clickable;
	ILabel label;

	public HyperlinkMouseOverListener(ILabel label) {
		this(label, label, label);
		this.label = label;
	}

	public HyperlinkMouseOverListener(IButton button) {
		this(button, button, button);
	}

	public HyperlinkMouseOverListener(IFontElement fontElement,
			IMouseOverElement<?> e, IClickable<?> c) {
		font = fontElement.font();
		clickable = c;
		e.addMouseOverListener(this);
		c.addClickListener(this);
	}

	@Override
	public void onMouseOver() {
		if (clickable.clickable())
			font.underline(true);
	}

	@Override
	public void onMouseOut() {
		font.underline(false);
	}

	@Override
	public void onClick() {
		onMouseOut();
	}

}
