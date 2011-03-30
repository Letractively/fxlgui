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

import java.awt.Color;

import javax.swing.JTextArea;

import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IUpdateable;

class SwingTextArea extends SwingTextInput<JTextArea, ITextArea> implements
		ITextArea {

	// TODO add scrollpanel

	SwingTextArea(SwingContainer<JTextArea> container) {
		super(container);
		jTextArea().setLineWrap(true);
		font().family().arial();
		font().pixel(12);
	}

	private JTextArea jTextArea() {
		return (JTextArea) container.component;
	}

	@Override
	public ITextArea text(String text) {
		super.setText(text);
		return this;
	}

	@Override
	String processText() {
		return html.text;
	}

	@Override
	public ITextArea editable(boolean editable) {
		container.component.setBackground(editable ? Color.WHITE : new Color(
				240, 240, 240));
		return super.editable(editable);
	}

	@Override
	public int cursorPosition() {
		return container.component.getCaretPosition();
	}

	@Override
	public ITextArea cursorPosition(int position) {
		container.component.setCaretPosition(position);
		container.component.requestFocus();
		return this;
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		return super.addStringUpdateListener(listener);
	}

	@Override
	public ITextArea maxLength(int maxLength) {
		// TODO ...
		System.err
				.println("SwingTextArea.maxLength: MethodNotImplementedException");
		return this;
	}
}