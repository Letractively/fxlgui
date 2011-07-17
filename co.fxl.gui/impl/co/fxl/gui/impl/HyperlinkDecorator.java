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
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;

public class HyperlinkDecorator implements IMouseOverListener {

	private boolean enabled = true;
	private ILabel label;

	public HyperlinkDecorator(ILabel label) {
		this.label = label;
		// TODO doesn't work (yet) with GWT:
		label.addMouseOverListener(this);
		styleHyperlinkActive(label);
	}

	private void styleHyperlinkActive(ILabel label) {
		// Styles.instance().style(label, Style.Element.HYPERLINK,
		// Style.Status.ACTIVE);
		label.font().color().rgb(0, 87, 141);
	}

	private void styleHyperlinkInactive(ILabel label) {
		// Styles.instance().style(label, Style.Element.HYPERLINK,
		// Style.Status.INACTIVE);
		label.font().color().gray();
	}

	private void styleHyperlinkHighlight(ILabel label) {
		// Styles.instance().style(label, Style.Element.HYPERLINK,
		// Style.Status.HIGHLIGHT);
		label.font().underline(true);
	}

	private void styleHyperlinkUnhighlight(ILabel label) {
		// Styles.instance().style(label, Style.Element.HYPERLINK,
		// Style.Status.UNHIGHLIGHT);
		label.font().underline(false);
	}

	public HyperlinkDecorator clickable(boolean enable) {
		this.enabled = enable;
		if (enabled) {
			styleHyperlinkActive(label);
		} else {
			styleHyperlinkInactive(label);
			styleHyperlinkUnhighlight(label);
		}
		return this;
	}

	@Override
	public void onMouseOver() {
		update(true);
	}

	protected void update(boolean over) {
		if (enabled && over) {
			styleHyperlinkHighlight(label);
		} else {
			styleHyperlinkUnhighlight(label);
		}
	}

	@Override
	public void onMouseOut() {
		update(false);
	}
}