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
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ISplitPane;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTSplitPane extends GWTElement<Widget, ISplitPane> implements ISplitPane {

	private class MouseHandler implements MouseDownHandler, MouseMoveHandler,
			MouseUpHandler {

		private int x = -1;
		private int y = -1;
		private boolean down = false;
		private boolean move = false;

		@Override
		public void onMouseDown(MouseDownEvent event) {
			down = true;
			move = false;
			x = event.getClientX();
			y = event.getClientY();
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			if (down) {
				move = true;
				if (event.getClientX() != x || event.getClientY() != y)
					onResize(false);
			}
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			if (down && move) {
				down = false;
				move = false;
				onResize(true);
			}
		}

	}

	private boolean dragging = false;
	private boolean moving = false;
	private int oWidth1 = -1;
	private int oWidth2 = -1;
	private List<ISplitPaneResizeListener> listeners = new LinkedList<ISplitPaneResizeListener>();

	GWTSplitPane(GWTContainer<Widget> container) {
		super(container);
		container.widget.setHeight("600px");
		container.widget.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		container.widget.addHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				dragging = true;
				onResize(false);
			}
		}, MouseDownEvent.getType());
		container.widget.addHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (dragging) {
					moving = true;
					onResize(false);
				}
			}
		}, MouseMoveEvent.getType());
		container.widget.addHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (dragging & moving) {
					onResize(true);
				}
				dragging = false;
				moving = false;
			}
		}, MouseUpEvent.getType());
	}

	@Override
	public IContainer first() {
		return new GWTContainer<Widget>(container.parent) {
			public void setComponent(Widget component) {
				super.widget = component;
				prepare(component);
				HorizontalSplitPanel p = (HorizontalSplitPanel) container.widget;
				p.setLeftWidget(component);
				component.getElement().getParentElement().getStyle()
						.setOverflow(Overflow.HIDDEN);
			}
		};
	}

	@Override
	public IContainer second() {
		return new GWTContainer<Widget>(container.parent) {
			public void setComponent(Widget component) {
				super.widget = component;
				prepare(component);
				HorizontalSplitPanel p = (HorizontalSplitPanel) container.widget;
				p.setRightWidget(component);
			}
		};
	}

	@Override
	public ISplitPane vertical() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ISplitPane splitPosition(int pixel) {
		HorizontalSplitPanel p = (HorizontalSplitPanel) container.widget;
		p.setSplitPosition(pixel + "px");
		oWidth1 = pixel;
		onResize(true);
		return this;
	}

	@Override
	public IBorder border() {
		return new GWTWidgetBorder(container.widget);
	}

	@Override
	public ISplitPane addResizeListener(final ISplitPaneResizeListener l) {
		listeners.add(l);
		return this;
	}

	private void onResize(boolean finished) {
		HorizontalSplitPanel p = (HorizontalSplitPanel) GWTSplitPane.super.container.widget;
		Widget leftWidget = p.getLeftWidget();
		Widget rightWidget = p.getRightWidget();
		if (leftWidget == null || rightWidget == null)
			return;
		int offsetWidth1 = leftWidget.getOffsetWidth();
		int offsetWidth2 = rightWidget.getOffsetWidth();
		if (finished || oWidth1 != offsetWidth1 || oWidth2 != offsetWidth2) {
			oWidth1 = offsetWidth1;
			oWidth2 = offsetWidth2;
			for (ISplitPaneResizeListener l : listeners)
				l.onResize(finished, offsetWidth1, offsetWidth2);
		}
	}

	private void prepare(Widget widget) {
		widget.setWidth("100%");
		widget.setHeight("100%");
	}

	@Override
	public int splitPosition() {
		return oWidth1;
	}
}