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

import co.fxl.gui.canvas.impl.RectangleImpl;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

class GWTRectangle extends RectangleImpl implements Drawable {

	private GWTCanvas canvas;
	private Context2d context;

	GWTRectangle(GWTCanvas canvas) {
		this.canvas = canvas;
		context = canvas.context;
	}

	@Override
	public void draw() {
		double x = canvas.offsetX(this);
		double y = canvas.offsetY(this);
		double w = canvas.width(this);
		double h = canvas.height(this);
		if (color.active) {
			context.setFillStyle(getColor());
			context.fillRect(x, y, w, h);
		}
		if (border.active) {
			context.beginPath();
			context.setStrokeStyle(canvas.getColor(border.color.rgb, 1.0));
			if (border.style.isPartial) {
				if (border.style.top)
					drawLine(x, y, w, 1d);
				if (border.style.bottom)
					drawLine(x, y + h, w, 1d);
				if (border.style.left)
					drawLine(x, y, 1d, h);
				if (border.style.right)
					drawLine(x + w, y, 1d, h);
			} else {
				context.rect(x + mod(1), y + mod(1), w, h);
			}
			context.closePath();
			context.stroke();
		}
	}

	private void drawLine(double x, double y, double w, double h) {
		context.moveTo(x + mod(w), y + mod(h));
		context.lineTo(x + w - 1 + mod(w), y + h - 1 + mod(h));
	}

	double mod(double w) {
		return w == 1 ? 0.5 : 0;
	}

	private CssColor getColor() {
		int[] rgb = color.rgb;
		double opacity = this.opacity;
		return canvas.getColor(rgb, opacity);
	}
}
