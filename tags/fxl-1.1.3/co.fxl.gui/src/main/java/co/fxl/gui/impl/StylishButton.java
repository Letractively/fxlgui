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
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.style.impl.Style;

public class StylishButton extends ClickableMultiplexer {

	private boolean changeBackground;
	private IHorizontalPanel buttonPanel;
	private ILabel button;
	private IHorizontalPanel backPanel;
	private boolean green;
	private int blue;
	private IImage image;

	public StylishButton(IHorizontalPanel p, String text) {
		this(p, text, true);
	}

	public StylishButton(IHorizontalPanel p, String text, boolean bold) {
		this(p, text, true, 3, bold);
	}

	public StylishButton(IHorizontalPanel p, String text,
			boolean changeBackground, int spacing, boolean bold) {
		this.changeBackground = changeBackground;
		backPanel = p;
		buttonPanel = backPanel.add().panel().horizontal().spacing(spacing);
		image = buttonPanel.add().image().resource("empty_1x1.png")
				.visible(false);
		IBorder br = buttonPanel.border();
		br.color().black();
		br.style().rounded();
		button = buttonPanel.add().label().text(text);
		button.margin().left(4);
		if (bold)
			button.font().weight().bold();
		button.font().color().white();
		buttonPanel.addSpace(4);
		new HyperlinkMouseOverListener(button);
		cs = new IClickable<?>[] { buttonPanel, button };
		Style.instance().window().prepareStylishButton(this);
	}

	public void image(String res) {
		image.resource(res).size(12, 13).visible(true);
		image.margin().left(4);
	}

	public StylishButton green() {
		green = true;
		return this;
	}

	public StylishButton blue(int blue) {
		this.blue = blue;
		Style.instance().window().addImageToStylishButton(this, blue);
		return this;
	}

	@Override
	public Object clickable(boolean clickable) {
		if (changeBackground) {
			Style.instance().window()
					.stylishButton(this, green, blue, clickable);
		} else {
			if (clickable)
				button.font().color().white();
			else
				button.font().color().rgb(240, 240, 240);
		}
		return super.clickable(clickable);
	}

	public IHorizontalPanel panel() {
		return backPanel;
	}

	public IHorizontalPanel buttonPanel() {
		return buttonPanel;
	}

}
