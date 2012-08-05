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

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;

import co.fxl.gui.api.IButton;
import co.fxl.gui.impl.HTMLText;

class SwingButton extends SwingTextElement<JButton, IButton> implements IButton {

	private List<ClickListenerMouseAdapter<IButton>> listeners = new LinkedList<ClickListenerMouseAdapter<IButton>>();

	SwingButton(SwingContainer<JButton> container) {
		super(container);
		html.center = true;
		container.component.setHorizontalAlignment(JButton.CENTER);
		container.component.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireClickListeners(arg0);
			}
		});
	}

	@Override
	protected void setTextOnComponent(String text) {
		container.component.setText(text);
	}

	@Override
	public IButton clickable(boolean active) {
		container.component.setEnabled(active);
		return this;
	}

	@Override
	public IKey<IButton> addClickListener(final IClickListener listener) {
		container.component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ClickListenerMouseAdapter<IButton> clickListenerMouseAdapter = new ClickListenerMouseAdapter<IButton>(
				this, listener);
		listeners.add(clickListenerMouseAdapter);
		return clickListenerMouseAdapter;
	}

	@Override
	public String text() {
		return HTMLText.removeHTML(container.component.getText());
	}

	@Override
	public IButton text(String text) {
		setButtonText(text);
		return this;
	}

	private void setButtonText(String text) {
		setText(text);
		update();
	}

	void fireClickListeners(ActionEvent arg0) {
		for (ClickListenerMouseAdapter<IButton> c : listeners)
			c.adapter.actionPerformed(arg0);
	}
}
