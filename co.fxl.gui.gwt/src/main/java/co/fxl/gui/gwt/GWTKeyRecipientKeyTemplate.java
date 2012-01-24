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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IKeyRecipient.IKey;

import com.google.gwt.event.dom.client.KeyCodes;

public class GWTKeyRecipientKeyTemplate<R> implements IKey<R> {

	public int nativeKeyCode;
	private R element;

	public GWTKeyRecipientKeyTemplate(R element) {
		this.element = element;
	}

	@Override
	public R enter() {
		nativeKeyCode = KeyCodes.KEY_ENTER;
		return (R) element;
	}

	@Override
	public R tab() {
		nativeKeyCode = KeyCodes.KEY_TAB;
		return (R) element;
	}

	@Override
	public R up() {
		nativeKeyCode = KeyCodes.KEY_UP;
		return (R) element;
	}

	@Override
	public R down() {
		nativeKeyCode = KeyCodes.KEY_DOWN;
		return (R) element;
	}

	@Override
	public R left() {
		nativeKeyCode = KeyCodes.KEY_LEFT;
		return (R) element;
	}

	@Override
	public R right() {
		nativeKeyCode = KeyCodes.KEY_RIGHT;
		return (R) element;
	}
}
