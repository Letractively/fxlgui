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
package co.fxl.gui.canvas.gwt;

import co.fxl.gui.canvas.api.IText;
import co.fxl.gui.canvas.impl.LocatedImpl;

class GWTText extends LocatedImpl<IText> implements IText, Drawable {

	private GWTCanvas canvas;
	private String text;
	private double fontSize = 12;

	GWTText(GWTCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public IText text(String text) {
		this.text = text;
		return this;
	}

	@Override
	public void draw() {
		double x = canvas.offsetX(this) + 0.5;
		double y = canvas.offsetY(this) + 0.5;
		canvas.context.setTextBaseline("top");
		canvas.context.setStrokeStyle(canvas.getBlack());
		canvas.context.setFillStyle(canvas.getBlack());
		canvas.context.setFont(fontSize + "px Arial");
		canvas.context.fillText(text, x, y);
	}
}
