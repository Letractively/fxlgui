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

import co.fxl.gui.api.IColored.IColor;

public class ColorMemento extends ColorTemplate {

	public ColorMemento() {
	}

	public ColorMemento(int r, int g, int b) {
		rgb(r, g, b);
	}

	@Override
	public IColor remove() {
		rgb = null;
		return this;
	}

	@Override
	protected IColor setRGB(int r, int g, int b) {
		return this;
	}

	public void forward(IColor color) {
		if (rgb == null)
			color.remove();
		else
			color.rgb(rgb[0], rgb[1], rgb[2]);
	}

}
