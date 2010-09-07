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

import javax.swing.JComboBox;

import co.fxl.gui.api.IComboBox;

public class SwingComboBox extends SwingTextElement<JComboBox, IComboBox>
		implements IComboBox {

	public SwingComboBox(SwingContainer<JComboBox> container) {
		super(container);
	}

	@Override
	public IComboBox text(String choice) {
		if (container.component.isEditable()) {
			container.component.setSelectedItem(choice);
		} else
			for (int i = 0; i < container.component.getItemCount(); i++)
				if (container.component.getItemAt(i).equals(choice))
					container.component.setSelectedIndex(i);
		return this;
	}

	@Override
	public IComboBox addText(String... texts) {
		for (String choice : texts)
			container.component.addItem(choice);
		return this;
	}

	@Override
	public String text() {
		return (String) container.component.getSelectedItem();
	}

	@Override
	public IComboBox editable(boolean editable) {
		container.component.setEditable(editable);
		return this;
	}

	@Override
	public IComboBox addUpdateListener(final IUpdateListener<String> listener) {
		container.component.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.onUpdate(text());
			}
		});
		return this;
	}

	@Override
	protected void setTextOnComponent(String text) {
		throw new MethodNotImplementedException();
	}

}
