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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;

public abstract class KeyTemplate<T> implements IKey<T> {

	public enum Type {

		CTRL_KEY, ALT_KEY, SHIFT_KEY, RIGHT_CLICK, DOUBLE_CLICK;
	}

	private final T element;
	protected final IClickListener clickListener;
	protected Type key = null;

	protected KeyTemplate(T element, IClickListener clickListener) {
		this.element = element;
		this.clickListener = clickListener;
	}

	@Override
	public T altPressed() {
		key = Type.ALT_KEY;
		return element;
	}

	@Override
	public T ctrlPressed() {
		key = Type.CTRL_KEY;
		return element;
	}

	@Override
	public T mouseLeft() {
		key = null;
		return element;
	}

	@Override
	public T mouseRight() {
		key = Type.RIGHT_CLICK;
		return element;
	}

	@Override
	public T shiftPressed() {
		key = Type.SHIFT_KEY;
		return element;
	}

	@Override
	public T doubleClick() {
		throw new MethodNotImplementedException();
	}
}