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

import co.fxl.gui.api.ITextField;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

class GWTTextField extends GWTElement<TextBox, ITextField> implements
		ITextField {

	GWTTextField(GWTContainer<TextBox> container) {
		super(container);
		container.widget.addStyleName("gwt-TextBox-FXL");
		defaultFont();
	}

	public ITextField text(String text) {
		if (text == null)
			text = "";
		container.widget.setText(text);
		return this;
	}

	@Override
	public ITextField addUpdateListener(
			final IUpdateListener<String> changeListener) {
		container.widget.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
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
		return new GWTStyleColor(style) {
			@Override
			void setColor(String color, com.google.gwt.dom.client.Style stylable) {
				stylable.setBackgroundColor(color);
			}
		};
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
	public ITextField editable(boolean editable) {
		container.widget.setReadOnly(!editable);
		return this;
	}

	@Override
	public int height() {
		return super.height() + 8;
	}

	@Override
	public ITextField height(int height) {
		return (ITextField) super.height(height - 8);
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
	public ITextField addCarriageReturnListener(
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
	public ITextField columns(int rows) {
		container.widget.setMaxLength(rows);
		return this;
	}

	@Override
	public ITextField tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}
}
