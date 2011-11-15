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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ITextField;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

class GWTTextField extends GWTElement<TextBox, ITextField> implements
		ITextField {

	private List<IUpdateListener<String>> updateListeners = new LinkedList<IUpdateListener<String>>();

	GWTTextField(GWTContainer<TextBox> container) {
		super(container);
		assert container != null : "GWTTextField.new: container is null";
		container.widget.addStyleName("gwt-TextBox-FXL");
		defaultFont();
	}

	public ITextField text(String text) {
		if (text == null)
			text = "";
		container.widget.setText(text);
		for (IUpdateListener<String> l : updateListeners)
			l.onUpdate(text);
		return this;
	}

	@Override
	public ITextField addUpdateListener(
			final IUpdateListener<String> changeListener) {
		updateListeners.add(changeListener);
		container.widget.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				changeListener.onUpdate(container.widget.getText());
			}
		});
		return this;
	}

	@Override
	public String text() {
		String text = container.widget.getText();
		if (text == null)
			return "";
		return text;
	}

	@Override
	public ITextField editable(boolean editable) {
		container.widget.setReadOnly(!editable);
		return this;
	}

	@Override
	public int width() {
		return super.width() + 8;
	}

	@Override
	public ITextField width(int width) {
		return (ITextField) super.width(width - 8);
	}

	@Override
	public ITextField maxLength(int rows) {
		container.widget.setMaxLength(rows);
		return this;
	}

	@Override
	public int cursorPosition() {
		return container.widget.getCursorPos();
	}

	@Override
	public ITextField cursorPosition(int position) {
		position = Math.max(position, text().length());
		container.widget.setCursorPos(position);
		return this;
	}
}
