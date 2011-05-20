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
package co.fxl.gui.api.template;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.template.Styles;

public class HyperlinkDecorator implements IMouseOverListener {

	private boolean enabled;
	private ILabel label;

	public HyperlinkDecorator(ILabel label) {
		this.label = label;
		label.addMouseOverListener(this);
		activeColor();
	}

	private void activeColor() {
		Styles.instance().hyperlink().clickable(label, true);
	}

	private void inactiveColor() {
		Styles.instance().hyperlink().clickable(label, false);
	}

	private void activeHighlight() {
		Styles.instance().hyperlink().highlight(label, true);
	}

	private void inactiveHighlight() {
		Styles.instance().hyperlink().highlight(label, false);
	}

	public HyperlinkDecorator clickable(boolean enable) {
		this.enabled = enable;
		if (enabled) {
			activeColor();
		} else {
			inactiveColor();
			inactiveHighlight();
		}
		return this;
	}

	@Override
	public void onMouseOver() {
		update(true);
	}

	protected void update(boolean over) {
		if (enabled && over) {
			activeHighlight();
		} else {
			inactiveHighlight();
		}
	}

	@Override
	public void onMouseOut() {
		update(false);
	}
}