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
package co.fxl.gui.swing;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.impl.HyperlinkDecorator;

public class SwingLabel extends SwingTextElement<JLabel, ILabel> implements
		ILabel {

	private HyperlinkDecorator hyperlinkDecorator;

	SwingLabel(SwingContainer<JLabel> container) {
		super(container);
		container.component.setVerticalAlignment(SwingConstants.TOP);
		font().pixel(12);
	}

	@Override
	protected void setTextOnComponent(String text) {
		container.component.setText(text);
	}

	@Override
	public ILabel hyperlink() {
		hyperlinkDecorator = new HyperlinkDecorator(this);
		return this;
	}

	@Override
	public ILabel clickable(boolean enable) {
		super.clickable(enable);
		if (hyperlinkDecorator != null) {
			hyperlinkDecorator.clickable(enable);
		}
		return this;
	}

	@Override
	public IKey<ILabel> addClickListener(IClickListener listener) {
		if (hyperlinkDecorator != null) {
			hyperlinkDecorator.clickable(clickable());
		}
		return super.addClickListener(listener);
	}

	@Override
	public String text() {
		return html.text;
	}

	@Override
	public ILabel text(String text) {
		return setLabelText(text);
	}

	private ILabel setLabelText(String text) {
		html.allowHTML(false);
		super.setText(text);
		return this;
	}

	@Override
	public ILabel html(String text) {
		html.allowHTML(true);
		super.setText(text);
		return this;
	}

	@Override
	public ILabel autoWrap(boolean autoWrap) {
		html.autoWrap = autoWrap;
		update();
		return this;
	}
}