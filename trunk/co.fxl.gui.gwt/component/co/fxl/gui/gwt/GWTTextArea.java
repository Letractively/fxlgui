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

import co.fxl.gui.api.ITextArea;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextArea;

class GWTTextArea extends GWTElement<TextArea, ITextArea> implements ITextArea {

	private List<IUpdateListener<String>> changeListeners = new LinkedList<IUpdateListener<String>>();

	GWTTextArea(GWTContainer<TextArea> container) {
		super(container);
		container.widget.addStyleName("gwt-TextArea-FXL");
		defaultFont();
		height(100);
	}

	@Override
	public ITextArea text(String text) {
		String previous = container.widget.getText();
		container.widget.setText(text);
		if (!previous.equals(text)) {
			for (IUpdateListener<String> ul : changeListeners)
				ul.onUpdate(text);
		}
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
		String text = container.widget.getText();
		if (text == null)
			return "";
		return text;
	}

	@Override
	public ITextArea editable(boolean editable) {
		container.widget.setReadOnly(!editable);
		return this;
	}

	@Override
	public ITextArea addUpdateListener(
			final IUpdateListener<String> changeListener) {
		changeListeners.add(changeListener);
		container.widget.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				changeListener.onUpdate(container.widget.getText());
			}
		});
		return this;
	}

	@Override
	public ITextArea tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}

	@Override
	public int cursorPosition() {
		return container.widget.getCursorPos();
	}

	@Override
	public ITextArea cursorPosition(int position) {
		position = Math.max(position, text().length());
		container.widget.setCursorPos(position);
		return this;
	}

	@Override
	public ITextArea maxLength(final int maxLength) {
		addUpdateListener(new IUpdateListener<String>() {

			@Override
			public void onUpdate(final String value) {
				if (value.length() > maxLength)
					display().invokeLater(new Runnable() {
						public void run() {
							GWTTextArea.this.container.widget.setText(value
									.substring(0, maxLength));
						}
					});
			}
		});
		return this;
	}
}
