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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.IClickable.IKey;

public abstract class KeyTemplate<T> implements IKey<T> {

	public enum ButtonType {
		LEFT, RIGHT;
	}

	public enum KeyType {
		CTRL_KEY, ALT_KEY, SHIFT_KEY;
	}

	protected final T element;
	protected ButtonType buttonType = ButtonType.LEFT;
	protected final Map<KeyType, Boolean> pressedKeys = new HashMap<KeyType, Boolean>();
	protected boolean isDoubleClick = false;

	protected KeyTemplate(T element) {
		this.element = element;
		pressedKeys.put(KeyType.ALT_KEY, false);
		pressedKeys.put(KeyType.SHIFT_KEY, false);
		pressedKeys.put(KeyType.CTRL_KEY, false);
	}

	@Override
	public T altPressed() {
		pressedKeys.put(KeyType.ALT_KEY, true);
		return element;
	}

	@Override
	public T ctrlPressed() {
		pressedKeys.put(KeyType.CTRL_KEY, true);
		return element;
	}

	@Override
	public T mouseLeft() {
		buttonType = ButtonType.LEFT;
		return element;
	}

	@Override
	public T mouseRight() {
		buttonType = ButtonType.RIGHT;
		return element;
	}

	@Override
	public T shiftPressed() {
		pressedKeys.put(KeyType.SHIFT_KEY, true);
		return element;
	}

	@Override
	public T doubleClick() {
		isDoubleClick = true;
		return element;
	}
}