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

	GWTSplitPane(GWTContainer<Widget> container) {
		super(container);
		container.widget.setHeight("600px");
		container.widget.getElement().getStyle().setOverflow(Overflow.HIDDEN);
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
		return this;
	}

	@Override
	public IBorder border() {
		return new GWTWidgetBorder(container.widget);
	}

	private boolean dragging = false;

	@Override
	public ISplitPane addResizeListener(final ISplitPaneResizeListener l) {
		container.widget.addHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				dragging = true;
				onResize(l);
			}
		}, MouseDownEvent.getType());
		container.widget.addHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (dragging)
					onResize(l);
			}
		}, MouseMoveEvent.getType());
		container.widget.addHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				dragging = false;
				onResize(l);
			}
		}, MouseUpEvent.getType());
		return this;
	}

	private int oWidth1 = -1;
	private int oWidth2 = -1;

	private void onResize(final ISplitPaneResizeListener l) {
		// display().invokeLater(new Runnable() {
		// public void run() {
		final HorizontalSplitPanel p = (HorizontalSplitPanel) GWTSplitPane.super.container.widget;
		int offsetWidth1 = p.getLeftWidget().getOffsetWidth();
		int offsetWidth2 = p.getRightWidget().getOffsetWidth();
		if (oWidth1 != offsetWidth1 || oWidth2 != offsetWidth2) {
			oWidth1 = offsetWidth1;
			oWidth2 = offsetWidth2;
			l.onResize(offsetWidth1, offsetWidth2);
		}
		// }
		// });
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