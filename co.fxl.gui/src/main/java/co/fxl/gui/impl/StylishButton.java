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

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;

public class StylishButton extends ClickableMultiplexer {

	private boolean changeBackground;
	private IHorizontalPanel buttonPanel;
	private ILabel button;

	public StylishButton(IHorizontalPanel p, String text) {
		this(p, text, true);
	}

	public StylishButton(IHorizontalPanel p, String text, boolean bold) {
		this(p, text, true, 3, bold);
	}

	public StylishButton(IHorizontalPanel p, String text,
			boolean changeBackground, int spacing, boolean bold) {
		this.changeBackground = changeBackground;
		buttonPanel = p.spacing(spacing);
		styleActive();
		IBorder br = buttonPanel.border();
		br.color().black();
		br.style().rounded();
		button = buttonPanel.addSpace(4).add().label().text(text);
		if (bold)
			button.font().weight().bold();
		button.font().color().white();
		buttonPanel.addSpace(4);
		new HyperlinkMouseOverListener(button);
		cs = new IClickable<?>[] { buttonPanel, button };
	}

	@Override
	public Object clickable(boolean clickable) {
		if (changeBackground) {
			if (clickable)
				styleActive();
			else
				buttonPanel.color().remove().rgb(140, 140, 140);
		} else {
			if (clickable)
				button.font().color().white();
			else
				button.font().color().rgb(240, 240, 240);
		}
		return super.clickable(clickable);
	}

	private void styleActive() {
		buttonPanel.color().remove().rgb(111, 111, 111).gradient().vertical()
				.rgb(63, 63, 63);
	}

	public IHorizontalPanel panel() {
		return buttonPanel;
	}
}
