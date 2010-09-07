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

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import co.fxl.gui.api.IRadioButton;

class SwingRadioButton extends SwingTextElement<JRadioButton, IRadioButton>
		implements IRadioButton {

	class Group implements IGroup {

		@Override
		public IGroup add(IRadioButton... buttons) {
			for (IRadioButton button : buttons) {
				SwingRadioButton swingButton = (SwingRadioButton) button;
				swingButton.group.remove(swingButton.container.component);
				group.add(swingButton.container.component);
			}
			return Group.this;
		}
	}

	ButtonGroup group = new ButtonGroup();

	SwingRadioButton(SwingContainer<JRadioButton> container) {
		super(container);
		container.component.setOpaque(false);
		group.add(container.component);
	}

	@Override
	protected void setTextOnComponent(String text) {
		container.component.setText(text);
	}

	@Override
	public boolean checked() {
		return container.component.isSelected();
	}

	@Override
	public String text() {
		return container.component.getText();
	}

	@Override
	public IRadioButton text(String text) {
		setText(text);
		return this;
	}

	@Override
	public IRadioButton checked(boolean checked) {
		container.component.setSelected(checked);
		return this;
	}

	@Override
	public IGroup group() {
		return new Group();
	}
}
