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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.template.KeyTemplate;

class ClickListenerMouseAdapter<T> extends KeyTemplate<T> implements IKey<T> {

	final class Adapter extends MouseAdapter implements ActionListener {

		@Override
		public void mouseReleased(MouseEvent e) {
			for (KeyType pressedKey : pressedKeys.keySet()) {
				Boolean check = pressedKeys.get(pressedKey);
				if (keyMatches(pressedKey, e) != check)
					return;
			}
			if (e.getButton() != getMouseButton())
				return;
			clickListener.onClick();
		}

		private int getMouseButton() {
			if (buttonType == ButtonType.LEFT)
				return MouseEvent.BUTTON1;
			else
				return MouseEvent.BUTTON3;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for (KeyType pressedKey : pressedKeys.keySet()) {
				Boolean check = pressedKeys.get(pressedKey);
				if (keyMatches(pressedKey, e) != check)
					return;
			}
			// TODO left/right button
			clickListener.onClick();
		}
	}

	Adapter adapter = new Adapter();
	private IClickListener clickListener;

	ClickListenerMouseAdapter(T element, IClickListener clickListener) {
		super(element);
		this.clickListener = clickListener;
	}

	static boolean keyMatches(KeyType key, MouseEvent nativeEvent) {
		switch (key) {
		case SHIFT_KEY:
			return nativeEvent.isShiftDown();
		case CTRL_KEY:
			return nativeEvent.isControlDown();
		default:
			return nativeEvent.isAltDown();
		}
	}

	static boolean keyMatches(KeyType key, ActionEvent e) {
		switch (key) {
		case SHIFT_KEY:
			return (e.getModifiers() & ActionEvent.SHIFT_MASK) > 0;
		case CTRL_KEY:
			return (e.getModifiers() & ActionEvent.CTRL_MASK) > 0;
		default:
			return (e.getModifiers() & ActionEvent.ALT_MASK) > 0;
		}
	}
}