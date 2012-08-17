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

	// Line:
	// back.beginPath();
	// back.setStrokeStyle(penColor);
	// back.setLineWidth(penWidth);
	// back.moveTo(lastX, lastY);
	// back.lineTo(tx, ty);
	// back.closePath();
	// back.stroke();

	@Override
	public void draw() {
		context.setFillStyle(getColor());
		double x = canvas.offsetX(this);
		double y = canvas.offsetY(this);
		double w = canvas.width(this);
		double h = canvas.height(this);
		context.fillRect(x, y, w, h);
		if (border.active) {
			startLine();
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
				context.rect(x - 0.5, y - 0.5, w, h);
			}
			endLine();
		}
	}

	private void drawLine(double x, double y, double w, double h) {
		context.moveTo(x - (w == 1 ? 0.5 : 0), y - (h == 1 ? 0.5 : 0));
		context.lineTo(x + w - 1 - (w == 1 ? 0.5 : 0), y + h - 1
				- (h == 1 ? 0.5 : 0));
	}

	void endLine() {
		context.closePath();
		context.stroke();
	}

	void startLine() {
		context.beginPath();
		context.setStrokeStyle(getColor(new int[] { 0, 0, 0 }, 1.0));
	}

	private CssColor getColor() {
		int[] rgb = color.rgb;
		double opacity = this.opacity;
		return getColor(rgb, opacity);
	}

	CssColor getColor(int[] rgb, double opacity) {
		String cssColor = "rgba(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ","
				+ opacity + ")";
		return CssColor.make(cssColor);
	}
}
