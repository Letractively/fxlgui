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

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.template.HTMLText;

import com.google.gwt.user.client.ui.HTML;

class GWTLabel extends GWTElement<HTML, ILabel> implements ILabel {

	private boolean isHyperlink = false;
	private HTMLText html = new HTMLText();

	GWTLabel(GWTContainer<HTML> container) {
		super(container);
		container.widget.setStyleName("gwt-Label");
	}

	@Override
	public ILabel text(String text) {
		html.setText(text);
		container.widget.setHTML(html.toString());
		return this;
	}

	@Override
	public IFont font() {
		return new GWTFont(container.widget);
	}

	@Override
	public ILabel clickable(boolean enable) {
		if (isHyperlink) {
			if (enable) {
				font().underline(true);
				font().color().blue();
			} else {
				font().underline(false);
				font().color().gray();
			}
		}
		return super.clickable(enable);
	}

	@Override
	public ILabel hyperlink() {
		isHyperlink = true;
		font().underline(true);
		font().color().blue();
		return this;
	}

	@Override
	GWTClickHandler<ILabel> newGWTClickHandler(IClickListener clickListener) {
		return new GWTClickHandler<ILabel>(this, clickListener);
	}

	@Override
	public String text() {
		return container.widget.getText();
	}

	@Override
	public ILabel tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}

	@Override
	public ILabel autoWrap(boolean autoWrap) {
		container.widget.setWordWrap(autoWrap);
		return this;
	}
}
