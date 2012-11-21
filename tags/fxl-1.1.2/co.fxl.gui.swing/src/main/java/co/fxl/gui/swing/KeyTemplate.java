/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.IKeyRecipient.IKey;

class KeyTemplate<T extends IElement<T>> implements IKeyRecipient.IKey<T> {

	private final T holder;
	private IClickListener clickListener;

	KeyTemplate(T holder, IClickListener clickListener) {
		this.holder = holder;
		this.clickListener = clickListener;
	}

	@Override
	public T enter() {
		addListener(KeyEvent.VK_ENTER);
		return holder;
	}

	@Override
	public T tab() {
		addListener(KeyEvent.VK_TAB);
		return holder;
	}

	@Override
	public T up() {
		addListener(KeyEvent.VK_UP);
		return holder;
	}

	@Override
	public T down() {
		addListener(KeyEvent.VK_DOWN);
		return holder;
	}

	@Override
	public T left() {
		addListener(KeyEvent.VK_LEFT);
		return holder;
	}

	@Override
	public T right() {
		addListener(KeyEvent.VK_RIGHT);
		return holder;
	}

	private void addListener(final int key) {
		((JComponent) holder.nativeElement()).addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (key == e.getKeyCode())
					clickListener.onClick();

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
	}

	@Override
	public IKey<T> ctrl() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T character(char c) {
		throw new UnsupportedOperationException();
	}
}