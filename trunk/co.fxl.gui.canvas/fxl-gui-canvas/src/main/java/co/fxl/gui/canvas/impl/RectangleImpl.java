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

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.canvas.api.IRectangle;
import co.fxl.gui.impl.BorderMemento;
import co.fxl.gui.impl.ColorMemento;

public class RectangleImpl extends LocatedImpl<IRectangle> implements
		IRectangle {

	protected ColorMemento color = new ColorMemento();
	protected BorderMemento border = new BorderMemento();
	protected double opacity = 1.0;

	@Override
	public IColor color() {
		return color;
	}

	@Override
	public IBorder border() {
		border.active = true;
		return border;
	}

	@Override
	public IRectangle opacity(double opacity) {
		this.opacity = opacity;
		return this;
	}

}
