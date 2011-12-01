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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import co.fxl.gui.api.ICheckBox;

class SwingCheckBox extends SwingTextElement<JCheckBox, ICheckBox> implements
		ICheckBox {

	SwingCheckBox(SwingContainer<JCheckBox> container) {
		super(container);
		container.component.setOpaque(false);
	}

	@Override
	public ICheckBox checked(boolean checked) {
		container.component.setSelected(checked);
		return this;
	}

	@Override
	public ICheckBox editable(boolean editable) {
		container.component.setEnabled(editable);
		return this;
	}

	@Override
	public ICheckBox addUpdateListener(final IUpdateListener<Boolean> listener) {
		container.component.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listener.onUpdate(container.component.isSelected());
			}
		});
		return this;
	}

	@Override
	protected void setTextOnComponent(String text) {
		container.component.setText(text);
	}

	@Override
	public boolean checked() {
		return container.component.getSelectedObjects() != null;
	}

	@Override
	public String text() {
		return container.component.getText();
	}

	@Override
	public ICheckBox text(String text) {
		setText(text);
		return this;
	}
}
