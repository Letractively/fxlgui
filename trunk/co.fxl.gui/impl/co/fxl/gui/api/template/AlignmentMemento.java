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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IAlignment;

public class AlignmentMemento<T> implements IAlignment<T> {

	public enum Type {
		BEGIN, CENTER, END;
	}

	private T t;
	public Type type = Type.BEGIN;

	public AlignmentMemento(T t) {
		this.t = t;
	}

	@Override
	public T begin() {
		return set(Type.BEGIN);
	}

	private T set(Type type) {
		this.type = type;
		return t;
	}

	@Override
	public T center() {
		return set(Type.CENTER);
	}

	@Override
	public T end() {
		return set(Type.END);
	}

	public void forward(IAlignment<?> align) {
		if (type == Type.BEGIN)
			align.begin();
		else if (type == Type.CENTER)
			align.center();
		else if (type == Type.END)
			align.end();
	}
}
