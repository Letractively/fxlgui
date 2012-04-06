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

import co.fxl.gui.api.IButton;

import com.google.gwt.user.client.ui.Button;

class GWTButton extends GWTElement<Button, IButton> implements IButton {

	GWTButton(GWTContainer<Button> container) {
		super(container);
		container.widget.addStyleName("gwt-Button-FXL");
		defaultFont();
	}

	@Override
	public IButton clickable(boolean active) {
		container.widget.setEnabled(active);
		return super.clickable(active);
	}

	@Override
	public IFont font() {
		return new GWTFont(container.widget);
	}

	@Override
	public IButton text(String text) {
		setButtonText(text);
		return this;
	}

	private void setButtonText(String text) {
		container.widget.setText(text);
	}

	@Override
	public String text() {
		return container.widget.getText();
	}

	@Override
	public IButton tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}

	@Override
	GWTClickHandler<IButton> newGWTClickHandler(IClickListener clickListener) {
		return new GWTClickHandler<IButton>(this, clickListener);
	}
}
