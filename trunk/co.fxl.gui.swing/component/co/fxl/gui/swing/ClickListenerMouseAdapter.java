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
			if (checkKeys(e))
				clickListener.onClick();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (checkKeys(e))
				clickListener.onClick();
		}
	}

	Adapter adapter = new Adapter();

	ClickListenerMouseAdapter(T element, IClickListener clickListener) {
		super(element, clickListener);
	}

	private boolean checkKeys(MouseEvent e) {
		if (key == null)
			return true;
		if (key.equals(Type.CTRL_KEY) && !e.isControlDown())
			return false;
		if (key.equals(Type.ALT_KEY) && !e.isAltDown())
			return false;
		if (key.equals(Type.SHIFT_KEY) && !e.isShiftDown())
			return false;
		if (key.equals(Type.RIGHT_CLICK) && e.getButton() != MouseEvent.BUTTON2)
			return false;
		return true;
	}

	private boolean checkKeys(ActionEvent e) {
		if (key == null)
			return true;
		if (key.equals(Type.CTRL_KEY)
				&& !((e.getModifiers() & ActionEvent.CTRL_MASK) > 0))
			return false;
		if (key.equals(Type.ALT_KEY)
				&& !((e.getModifiers() & ActionEvent.ALT_MASK) > 0))
			return false;
		if (key.equals(Type.SHIFT_KEY)
				&& !((e.getModifiers() & ActionEvent.SHIFT_MASK) > 0))
			return false;
		if (key.equals(Type.RIGHT_CLICK))
			throw new MethodNotImplementedException();
		return true;
	}
}