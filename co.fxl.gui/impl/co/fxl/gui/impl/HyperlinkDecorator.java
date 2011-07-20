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

import co.fxl.gui.api.ILabel;

public class HyperlinkDecorator extends HyperlinkMouseOverListener {

	public HyperlinkDecorator(ILabel label) {
		super(label);
		styleHyperlinkActive(label);
	}

	private void styleHyperlinkActive(ILabel label) {
		label.font().color().rgb(0, 87, 141);
	}

	private void styleHyperlinkInactive(ILabel label) {
		label.font().color().gray();
	}

	public HyperlinkDecorator clickable(boolean enable) {
		if (label.clickable()) {
			styleHyperlinkActive(label);
		} else {
			styleHyperlinkInactive(label);
		}
		return this;
	}
}