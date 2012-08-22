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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.canvas.api.ICanvas;
import co.fxl.gui.canvas.api.IRectangle;
import co.fxl.gui.canvas.api.IText;
import co.fxl.gui.canvas.impl.LocatedImpl;
import co.fxl.gui.gwt.GWTContainer;
import co.fxl.gui.gwt.GWTDisplay;
import co.fxl.gui.gwt.GWTElement;
import co.fxl.gui.gwt.GWTFocusPanel;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

class GWTCanvas extends GWTElement<Canvas, ICanvas> implements ICanvas {

	private class GWTMouseAdapter implements IMouseEventType {

		private MouseEventType type = MouseEventType.DOWN;
		private IMouseListener listener;
		private Boolean shift = null;

		GWTMouseAdapter(IMouseListener l) {
			listener = l;
		}

		@Override
		public IMouseEventType down() {
			type = MouseEventType.DOWN;
			return this;
		}

		@Override
		public IMouseEventType move() {
			type = MouseEventType.MOVE;
			return this;
		}

		@Override
		public IMouseEventType up() {
			type = MouseEventType.UP;
			return this;
		}

		boolean matchesKeys(MouseEvent<?> event) {
			if (shift == null)
				return true;
			return shift == event.isShiftKeyDown();
		}

		@Override
		public IMouseEventType shift() {
			shift = true;
			return this;
		}

	}

	private enum MouseEventType {

		DOWN, UP, MOVE;
	}

	private Drawable drawable = null;
	Context2d context;
	private List<GWTMouseAdapter> adapters = new LinkedList<GWTMouseAdapter>();

	private GWTCanvas(GWTContainer<Canvas> c) {
		super(c);
		GWTFocusPanel.removeOutline(container.widget);
		context = container.widget.getContext2d();
		container.widget.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				notifyEvent(event, MouseEventType.DOWN);
			}
		});
		container.widget.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				notifyEvent(event, MouseEventType.UP);
			}
		});
		container.widget.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				notifyEvent(event, MouseEventType.MOVE);
			}
		});
	}

	private void notifyEvent(MouseEvent<?> event, MouseEventType type) {
		if (!GWTDisplay.waiting)
			for (GWTMouseAdapter m : adapters)
				if (m.type.equals(type) && m.matchesKeys(event))
					m.listener.onEvent(x(event), y(event));
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
		update();
		return super.visible(visible);
	}

	@Override
	public ICanvas update() {
		nextDrawable(null);
		return this;
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

	CssColor getGray(int i) {
		return getColor(new int[] { i, i, i }, 1.0);
	}

	@Override
	public IMouseEventType addMouseListener(final IMouseListener l) {
		GWTMouseAdapter adp = new GWTMouseAdapter(l);
		adapters.add(adp);
		return adp;
	}

	private int x(MouseEvent<?> event) {
		return event.getRelativeX(container.widget.getElement());
	}

	private int y(MouseEvent<?> event) {
		return event.getRelativeY(container.widget.getElement());
	}

	@Override
	public ICanvas clear() {
		context.clearRect(0, 0, width(), height());
		return this;
	}
}
