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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable;

public class ClickableMultiplexer implements IClickable<Object> {

	IClickable<?>[] cs;

	public ClickableMultiplexer() {
	}

	public ClickableMultiplexer(IClickable<?>... cs) {
		this.cs = cs;
	}

	@Override
	public Object clickable(boolean clickable) {
		for (IClickable<?> c : cs)
			c.clickable(clickable);
		return this;
	}

	@Override
	public boolean clickable() {
		return cs[0].clickable();
	}

	@Override
	public co.fxl.gui.api.IClickable.IKey<Object> addClickListener(
			co.fxl.gui.api.IClickable.IClickListener clickListener) {
		final List<co.fxl.gui.api.IClickable.IKey<?>> keys = new LinkedList<co.fxl.gui.api.IClickable.IKey<?>>();
		for (IClickable<?> c : cs)
			keys.add(c.addClickListener(clickListener));
		return new IKey<Object>() {

			@Override
			public Object mouseLeft() {
				for (IKey<?> k : keys)
					k.mouseLeft();
				return this;
			}

			@Override
			public Object mouseRight() {
				for (IKey<?> k : keys)
					k.mouseRight();
				return this;
			}

			@Override
			public Object shiftPressed() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object altPressed() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object ctrlPressed() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object doubleClick() {
				throw new UnsupportedOperationException();
			}

		};
	}

}
