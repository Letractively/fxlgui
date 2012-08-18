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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.canvas.api.ICanvas;
import co.fxl.gui.canvas.api.IRectangle;
import co.fxl.gui.canvas.api.IText;
import co.fxl.gui.canvas.impl.LocatedImpl;
import co.fxl.gui.gwt.GWTContainer;
import co.fxl.gui.gwt.GWTElement;
import co.fxl.gui.gwt.GWTFocusPanel;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

class GWTCanvas extends GWTElement<Canvas, ICanvas> implements ICanvas {

	private Drawable drawable = null;
	Context2d context;

	private GWTCanvas(GWTContainer<Canvas> c) {
		super(c);
		GWTFocusPanel.removeOutline(container.widget);
		context = container.widget.getContext2d();
	}

	@SuppressWarnings("unchecked")
	static ICanvas create(IContainer c) {
		Canvas canvas = Canvas.createIfSupported();
		c.nativeElement(canvas);
		return new GWTCanvas((GWTContainer<Canvas>) c);
	}

	@Override
	public ICanvas width(int width) {
		container.widget.setCoordinateSpaceWidth(width);
		return super.width(width);
	}

	@Override
	public ICanvas height(int height) {
		container.widget.setCoordinateSpaceHeight(height);
		return super.height(height);
	}

	@Override
	public IRectangle addRectangle() {
		return nextDrawable(new GWTRectangle(this));
	}

	private <T> T nextDrawable(T newtDrawable) {
		if (drawable != null) {
			drawable.draw();
		}
		this.drawable = (Drawable) newtDrawable;
		return newtDrawable;
	}

	double width(LocatedImpl<?> l) {
		if (l.width() == -1)
			return (double) container.widget.getOffsetWidth() * l.widthDouble;
		return (double) l.width();
	}

	double height(LocatedImpl<?> l) {
		if (l.height() == -1)
			return (double) container.widget.getOffsetHeight() * l.heightDouble;
		return (double) l.height();
	}

	double offsetX(LocatedImpl<?> l) {
		return (double) l.offsetX();
	}

	double offsetY(LocatedImpl<?> l) {
		return (double) l.offsetY();
	}

	@Override
	public ICanvas visible(boolean visible) {
		nextDrawable(null);
		test();
		return super.visible(visible);
	}

	private void test() {
		CssColor color = CssColor.make("rgba(0,0,255,1.0)");
		context.setStrokeStyle(color);
		context.rect(100, 100, 100, 100);
	}

	@Override
	public IText addText() {
		return nextDrawable(new GWTText(this));
	}

	CssColor getBlack() {
		return getColor(new int[] { 0, 0, 0 }, 1.0);
	}

	CssColor getColor(int[] rgb, double opacity) {
		String cssColor = "rgba(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ","
				+ opacity + ")";
		return CssColor.make(cssColor);
	}
}
