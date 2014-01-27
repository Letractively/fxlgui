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
package co.fxl.gui.canvas.impl;

import co.fxl.gui.api.ILocated;

public class LocatedImpl<T> implements ILocated<T> {

	protected int offsetX = 0;
	protected int offsetY = 0;
	protected int widthInt = -1;
	protected int heightInt = -1;
	public double widthDouble = -1;
	public double heightDouble = -1;

	@Override
	public int offsetX() {
		return offsetX;
	}

	@Override
	public int offsetY() {
		return offsetY;
	}

	@Override
	public int width() {
		return widthInt;
	}

	@Override
	public int height() {
		return heightInt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T offset(int x, int y) {
		offsetX = x;
		offsetY = y;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T width(double width) {
		widthDouble = width;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T height(double height) {
		heightDouble = height;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T width(int width) {
		widthInt = width;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T height(int height) {
		heightInt = height;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T size(int width, int height) {
		width(width);
		height(height);
		return (T) this;
	}

}
