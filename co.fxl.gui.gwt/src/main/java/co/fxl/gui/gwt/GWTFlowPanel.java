/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.gwt;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IFlowPanel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTFlowPanel extends GWTPanel<FlowPanel, IFlowPanel> implements
		IFlowPanel {

	private static final String RIGHT = "right";
	public static boolean SET_FLOAT_LEFT = false;
	private int margin = 0;
	private String floatValue = "left";

	public GWTFlowPanel() {
		super(newContainer());
	}

	private static GWTContainer<FlowPanel> newContainer() {
		GWTContainer<FlowPanel> c = new GWTContainer<FlowPanel>();
		c.setComponent(new FlowPanel());
		return c;
	}

	@SuppressWarnings("unchecked")
	GWTFlowPanel(GWTContainer<?> container) {
		super((GWTContainer<FlowPanel>) container);
		widget().setWidth("100%");
	}

	@Override
	GWTClickHandler<IFlowPanel> newGWTClickHandler(IClickListener clickListener) {
		return new GWTClickHandler<IFlowPanel>(this, clickListener);
	}

	@Override
	public void add(Widget widget) {
		widget.getElement().getStyle().setProperty("display", "inline");
		updateFloatValueWidget(widget);
		// widget.setHeight("100%");
		setMargin(widget);
		container.widget.add(widget);
	}

	@Override
	public IFlowPanel spacing(int spacing) {
		this.margin = spacing / 2;
		// TODO Look: GWT: Firefox: Margin doesn't work if items are
		// displayed in more than one row
		if (!GWTDisplay.isChrome)
			container.widget.getElement().getStyle()
					.setPadding(spacing, Unit.PX);
		for (int i = 0; i < container.widget.getWidgetCount(); i++) {
			Widget w = container.widget.getWidget(i);
			setMargin(w);
		}
		return this;
	}

	@Override
	public IFlowPanel addSpace(int pixel) {
		Widget p = new AbsolutePanel();
		p.setSize(pixel + "px", "1px");
		container.widget.add(p);
		return this;
	}

	protected void setMargin(Widget w) {
		if (margin <= 0)
			return;
		w.getElement().getStyle().setMargin(margin, Unit.PX);
	}

	@Override
	public IAlignment<IFlowPanel> align() {
		return new IAlignment<IFlowPanel>() {

			@Override
			public IFlowPanel begin() {
				// TODO ... throw new UnsupportedOperationException();
				return GWTFlowPanel.this;
			}

			@Override
			public IFlowPanel center() {
				// TODO ... throw new UnsupportedOperationException();
				return GWTFlowPanel.this;
			}

			@Override
			public IFlowPanel end() {
				floatValue = RIGHT;
				updateFloatValueWidgets();
				// TODO ... throw new UnsupportedOperationException();
				return GWTFlowPanel.this;
			}

		};
	}

	private void updateFloatValueWidgets() {
		for (int i = 0; i < container.widget.getWidgetCount(); i++)
			updateFloatValueWidget(container.widget.getWidget(i));
	}

	private void updateFloatValueWidget(Widget widget) {
		if (SET_FLOAT_LEFT)
			widget.getElement().getStyle().setProperty("float", floatValue);
	}

	@Override
	public ILineBreak addLineBreak() {
		final ParagraphElement e = Document.get().createPElement();
		Style style = e.getStyle();
		style.setHeight(0, Unit.PX);
		style.setMargin(0, Unit.PX);
		container.widget.getElement().appendChild(e);
		return new ILineBreak() {
			@Override
			public void remove() {
				e.removeFromParent();
			}
		};
	}
}