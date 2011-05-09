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

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.IUpdateable;

class SwingPasswordField extends SwingTextInput<JPasswordField, IPasswordField>
		implements IPasswordField {

	SwingPasswordField(SwingContainer<JPasswordField> container) {
		super(container);
	}

	@Override
	public IPasswordField text(String text) {
		setTextOnComponent(text);
		font.updateFont();
		return this;
	}

	@Override
	public IPasswordField addCarriageReturnListener(
			final ICarriageReturnListener changeListener) {
		jTextField().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeListener.onCarriageReturn();
			}
		});
		return this;
	}

	@Override
	public IPasswordField columns(int rows) {
		jTextField().setColumns(rows);
		return this;
	}

	private JTextField jTextField() {
		return ((JTextField) container.component);
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		return super.addStringUpdateListener(listener);
	}

	void addActionListener(ActionListener actionListener) {
		container.component.addActionListener(actionListener);
	}
}
