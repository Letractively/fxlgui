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
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;

import co.fxl.gui.api.IComboBox;

public class SwingComboBox extends SwingTextElement<JComboBox, IComboBox>
		implements IComboBox {

	private boolean hasNull = false;
	private List<IUpdateListener<String>> listeners = new LinkedList<IUpdateListener<String>>();
	private String value;
	boolean programmaticSet;

	public SwingComboBox(SwingContainer<JComboBox> container) {
		super(container);
		color().white();
		container.component.addActionListener(new ActionListener() {

			private boolean ignore = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ignore) // || (value == null && !hasNull))
					return;
				String text = text();
				if (SwingDisplay.instance().waiting) {
					ignore = true;
					text(value);
					ignore = false;
				} else {
					value = text();
					fireUpdateListeners(text);
				}
			}
		});
	}

	@Override
	public IComboBox height(int height) {
		return super.height(height - 2);
	}

	@Override
	public int height() {
		return super.height() + 2;
	}

	@Override
	public IComboBox text(String choice) {
		programmaticSet = true;
		String token = choice;
		if (choice == null) {
			if (!hasNull) {
				addNull();
			}
			token = "";
		}
		boolean found = false;
		for (int i = 0; i < container.component.getItemCount(); i++) {
			found |= container.component.getItemAt(i).equals(token);
		}
		if (!found)
			addText(choice);
		// if (container.component.isEditable()) {
		// container.component.setSelectedItem(token);
		// } else
		for (int i = 0; i < container.component.getItemCount(); i++)
			if (container.component.getItemAt(i).equals(token))
				container.component.setSelectedIndex(i);
		value = text();
		programmaticSet = false;
		return this;
	}

	@Override
	public IComboBox addNull() {
		return addText((String) null);
	}

	@Override
	public IComboBox addText(String... texts) {
		programmaticSet = true;
		for (String choice : texts) {
			if (choice == null) {
				hasNull = true;
				choice = "";
			}
			if (value == null)
				value = choice;
			container.component.addItem(choice);
		}
		programmaticSet = false;
		return this;
	}

	@Override
	public String text() {
		String selectedItem = (String) container.component.getSelectedItem();
		if (selectedItem == null) {
			return null;
		}
		if (hasNull && selectedItem.equals(""))
			return null;
		return selectedItem;
	}

	@Override
	public IComboBox editable(boolean editable) {
		container.component.setEnabled(editable);
		return this;
	}

	@Override
	public IComboBox addUpdateListener(final IUpdateListener<String> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	protected void setTextOnComponent(String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IComboBox clear() {
		container.component.removeAllItems();
		value = text();
		return this;
	}

	@Override
	public boolean editable() {
		return container.component.isEnabled();
	}

	void fireUpdateListeners(String text) {
		for (IUpdateListener<String> l : listeners)
			l.onUpdate(text);
	}
}
