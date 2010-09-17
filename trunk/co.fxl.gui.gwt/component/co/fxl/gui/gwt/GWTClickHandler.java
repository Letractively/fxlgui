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

	GWTClickHandler(T element, IClickListener clickListener) {
		super(element, clickListener);
	}

	public void onClick(ClickEvent event) {

		// TODO when time since last click < 0.5s, fire as double click

		if (keyEquals(key, event.getNativeEvent()))
			clickListener.onClick();
	}

	boolean keyEquals(Type key, NativeEvent nativeEvent) {
		if (key == null)
			return true;
		else if (key.equals(Type.CTRL_KEY))
			return nativeEvent.getCtrlKey();
		else if (key.equals(Type.ALT_KEY))
			return nativeEvent.getAltKey();
		else if (key.equals(Type.SHIFT_KEY))
			return nativeEvent.getCtrlKey();
		else
			throw new MethodNotImplementedException();
	}
}