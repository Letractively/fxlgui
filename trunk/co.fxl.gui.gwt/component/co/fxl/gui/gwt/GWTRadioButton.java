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

import co.fxl.gui.api.IRadioButton;

import com.google.gwt.user.client.ui.RadioButton;

class GWTRadioButton extends GWTElement<RadioButton, IRadioButton> implements
		IRadioButton {

	class Group implements IGroup {

		@Override
		public IGroup add(IRadioButton... buttons) {
			for (IRadioButton button : buttons) {
				GWTRadioButton gwtButton = (GWTRadioButton) button;
				gwtButton.container.widget
						.setName(GWTRadioButton.this.container.widget.getName());
			}
			return Group.this;
		}
	}

	static int GROUP_ID = 0;

	GWTRadioButton(GWTContainer<RadioButton> container) {
		super(container);
		container.widget.setValue(false);
		container.widget.addStyleName("gwt-RadioButton");
		font().family().arial();
		font().pixel(12);
	}

	@Override
	public IRadioButton checked(boolean checked) {
		container.widget.setValue(checked);
		return this;
	}

	@Override
	public IFont font() {
		return new GWTFont(container.widget);
	}

	@Override
	public IRadioButton text(String text) {
		container.widget.setText(text);
		return this;
	}

	@Override
	public boolean checked() {
		return container.widget.getValue();
	}

	@Override
	public String text() {
		return container.widget.getText();
	}

	@Override
	public IRadioButton tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}

	@Override
	public IGroup group() {
		return new Group();
	}

	static String nextGroup() {
		return "group" + GROUP_ID++;
	}
}
