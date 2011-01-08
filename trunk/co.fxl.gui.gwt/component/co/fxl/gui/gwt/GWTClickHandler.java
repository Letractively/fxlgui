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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.KeyTemplate;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;

class GWTClickHandler<T> extends KeyTemplate<T> {

	private IClickListener clickListener;

	GWTClickHandler(T element, IClickListener clickListener) {
		super(element);
		this.clickListener = clickListener;
	}

	public void onClick(ClickEvent event) {
		for (KeyType pressedKey : pressedKeys.keySet()) {
			Boolean check = pressedKeys.get(pressedKey);
			if (keyMatches(pressedKey, event.getNativeEvent()) != check)
				return;
		}
		clickListener.onClick();
	}

	boolean keyMatches(KeyType key, NativeEvent nativeEvent) {
		switch (key) {
		case SHIFT_KEY:
			return nativeEvent.getShiftKey();
		case CTRL_KEY:
			return nativeEvent.getCtrlKey();
		default:
			return nativeEvent.getAltKey();
		}
	}
}