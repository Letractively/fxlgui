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

import co.fxl.gui.api.IPasswordField;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.PasswordTextBox;

class GWTPasswordField extends GWTElement<PasswordTextBox, IPasswordField>
		implements IPasswordField {

	GWTPasswordField(GWTContainer<PasswordTextBox> container) {
		super(container);
		font().family().arial();
		font().pixel(12);
	}

	public IPasswordField text(String text) {
		if (text == null)
			text = "";
		container.widget.setText(text);
		return this;
	}

	@Override
	public IPasswordField addUpdateListener(
			final IUpdateListener<String> changeListener) {
		container.widget.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				changeListener.onUpdate(container.widget.getText());
			}
		});
		return this;
	}

	@Override
	public IBorder border() {
		return new GWTWidgetBorder(container.widget);
	}

	@Override
	public IColor color() {
		GWTWidgetStyle style = new GWTWidgetStyle("background-color-",
				container.widget);
		return new GWTStyleColor(style);
	}

	@Override
	public IFont font() {
		return new GWTFont(container.widget);
	}

	@Override
	public String text() {
		return container.widget.getText();
	}

	@Override
	public IPasswordField editable(boolean editable) {
		container.widget.setReadOnly(!editable);
		return this;
	}

	@Override
	public int height() {
		return super.height() + 2;
	}

	@Override
	public IPasswordField addCarriageReturnListener(
			final ICarriageReturnListener listener) {
		container.widget.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				char charCode = event.getCharCode();
				if (charCode == '\r') {
					listener.onCarriageReturn();
				}
			}
		});
		return this;
	}

	@Override
	public IPasswordField columns(int rows) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IPasswordField tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}
}
