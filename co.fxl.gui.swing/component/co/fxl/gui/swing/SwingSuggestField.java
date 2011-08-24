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

import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextField;

import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.IUpdateable;

class SwingSuggestField extends SwingTextInput<JTextField, ISuggestField>
		implements ISuggestField {

	private List<IUpdateListener<String>> updateListeners = new LinkedList<IUpdateListener<String>>();

	SwingSuggestField(SwingContainer<JTextField> container) {
		super(container);
	}

	@Override
	public ISuggestField height(int height) {
		return super.height(height + 1);
	}

	@Override
	public int height() {
		return super.height() - 1;
	}

	@Override
	public ISuggestField text(String text) {
		if (text == null)
			text = "";
		setTextOnComponent(text);
		font.updateFont();
		for (IUpdateListener<String> l : updateListeners)
			l.onUpdate(text);
		return this;
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		updateListeners.add(listener);
		return super.addStringUpdateListener(listener);
	}

	void addActionListener(ActionListener actionListener) {
		container.component.addActionListener(actionListener);
	}

	@Override
	public ISuggestField addText(String... texts) {
		// TODO ... throw new MethodNotImplementedException();
		return this;
	}

}
