/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.TextArea;

class GWTTextArea extends GWTTextAreaTemplate<TextArea, ITextArea> implements
		ITextArea {

	private List<IUpdateListener<String>> changeListeners = new LinkedList<IUpdateListener<String>>();
	private String lastNotifiedValue = null;
	private int lastHeight = 100;
	private List<IResizeListener> resizeListeners = new LinkedList<IResizeListener>();

	GWTTextArea(GWTContainer<TextArea> container) {
		super(container);
		// font().family().openSans();
		container.widget.addStyleName("gwt-TextArea-FXL");
		container.widget.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				notifyChange();
			}
		});
		container.widget.addDropHandler(new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				notifyChange();
			}
		});
		container.widget.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				notifyChange();
			}
		});
		container.widget.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				checkResize();
			}
		});
		container.widget.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				checkResize();
			}
		});
		lastHeight = height();
	}

	@Override
	ITextArea width(boolean isNegative, String widthString) {
		// if (Env.is(Env.CHROME, Env.SAFARI, Env.OPERA)) {
		if (isUndefined())
			return (ITextArea) this;
		if (isNegative) {
			style().clearProperty("maxWidth");
			style().clearProperty("minWidth");
		} else {
			style().setProperty("maxWidth", widthString);
			style().setProperty("minWidth", widthString);
		}
		// }
		return super.width(isNegative, widthString);
	}

	private void checkResize() {
		if (lastHeight != height()) {
			lastHeight = height();
			for (IResizeListener l : resizeListeners) {
				l.onResize(width(), height());
			}
		}
	}

	private void notifyChange() {
		String text = text();
		if (lastNotifiedValue == null || !lastNotifiedValue.equals(text)) {
			lastNotifiedValue = text;
			for (IUpdateListener<String> l : changeListeners)
				l.onUpdate(text);
		}
	}

	@Override
	public ITextArea text(String text) {
		String previous = container.widget.getText();
		container.widget.setText(text);
		if (!previous.equals(text)) {
			notifyChange();
		}
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
	public ITextArea editable(boolean editable) {
		container.widget.setReadOnly(!editable);
		return this;
	}

	@Override
	public ITextArea addUpdateListener(
			final IUpdateListener<String> changeListener) {
		changeListeners.add(changeListener);
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
		position = Math.min(position, text().length());
		container.widget.setCursorPos(position);
		return this;
	}

	@Override
	public ITextArea maxLength(final int maxLength) {
		container.widget.getElement().setAttribute("maxlength",
				String.valueOf(maxLength));
		addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(final String value) {
				if (value.length() > maxLength)
					display().invokeLater(new Runnable() {
						public void run() {
							int c = cursorPosition();
							container.widget.setText(value.substring(0,
									maxLength));
							if (c < container.widget.getText().length())
								cursorPosition(c);
						}
					});
			}
		});
		return this;
	}

	@Override
	public boolean editable() {
		return !container.widget.isReadOnly();
	}

	@Override
	public ITextArea addResizeListener(
			co.fxl.gui.api.IResizable.IResizeListener listener) {
		resizeListeners.add(listener);
		return this;
	}
}
