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
package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable.IKey;

public class KeyAdapter<T> extends KeyTemplate<T> {

	public KeyAdapter() {
		super(null);
	}

	public void forward(IKey<T> key) {
		if (buttonType == ButtonType.RIGHT)
			key.mouseRight();
		if (pressedKeys.get(KeyType.ALT_KEY))
			key.altPressed();
		if (pressedKeys.get(KeyType.CTRL_KEY))
			key.ctrlPressed();
		if (pressedKeys.get(KeyType.SHIFT_KEY))
			key.shiftPressed();
		if (isDoubleClick)
			key.doubleClick();
	}

}
