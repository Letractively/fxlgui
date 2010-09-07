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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import co.fxl.gui.api.IUpdateable.IUpdateListener;

class SwingTextInput<T extends JTextComponent, R> extends
		SwingTextElement<T, R> {

	private final class UpdateListener implements DocumentListener {
		private final IUpdateListener<String> updateListener;

		private UpdateListener(IUpdateListener<String> updateListener) {
			this.updateListener = updateListener;
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			updateListener.onUpdate(text());
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			updateListener.onUpdate(text());
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			updateListener.onUpdate(text());
		}
	}

	SwingTextInput(SwingContainer<T> container) {
		super(container);
	}

	public R editable(boolean editable) {
		container.component.setEditable(editable);
		@SuppressWarnings("unchecked")
		R ret = (R) this;
		return ret;
	}

	@Override
	void setTextOnComponent(String text) {
		container.component.setText(text);
		container.component.setCaretPosition(0);
	}

	public String text() {
		html.text = container.component.getText();
		return html.text;
	}

	public R addUpdateListener(final IUpdateListener<String> updateListener) {
		container.component.getDocument().addDocumentListener(
				new UpdateListener(updateListener));
		@SuppressWarnings("unchecked")
		R ret = (R) this;
		return ret;
	}
}
