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

import co.fxl.gui.api.IToggleButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ToggleButton;

class GWTToggleButton extends GWTElement<ToggleButton, IToggleButton> implements
		IToggleButton {

	GWTToggleButton(GWTContainer<ToggleButton> container) {
		super(container);
		font(this);
		container.widget.addStyleName("gwt-ToggleButton");
	}

	@Override
	public IToggleButton addUpdateListener(
			final IUpdateListener<Boolean> listener) {
		container.widget.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onUpdate(down());
			}
		});
		return this;
	}

	@Override
	public IToggleButton down(boolean checked) {
		container.widget.setDown(checked);
		return this;
	}

	@Override
	public boolean down() {
		return container.widget.isDown();
	}

	@Override
	public IFont font() {
		return new GWTFont(container.widget);
	}

	@Override
	public IToggleButton text(String text) {
		container.widget.setText(text);
		return this;
	}

	@Override
	public String text() {
		return container.widget.getText();
	}
}
