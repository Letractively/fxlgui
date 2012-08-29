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
import co.fxl.gui.api.ISplitPane.ISplitPaneResizeListener;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTSplitPane extends GWTElement<ObservableSplitLayoutPanel, ISplitPane>
		implements ISplitPane, ISplitPaneResizeListener {

	private final class LeftSideContainer extends GWTContainer<Widget> {

		private LeftSideContainer(WidgetParent parent) {
			super(parent);
		}

		@Override
		public void setComponent(Widget component) {
			widget = component;
			prepare(component);
			container.widget.addWest(component, splitPosition);
			hideOverflow(component);
		}
	}

	private final class RightSideContainer extends GWTContainer<Widget> {

		private RightSideContainer(WidgetParent parent) {
			super(parent);
		}

		@Override
		public void setComponent(Widget component) {
			widget = component;
			prepare(component);
			container.widget.add(component);
		}
	}

	private List<ISplitPaneResizeListener> listeners = new LinkedList<ISplitPaneResizeListener>();
	private GWTContainer<Widget> leftContainer;
	private GWTContainer<Widget> rightContainer;
	int splitPosition;
	private boolean holdNotify;

	GWTSplitPane(GWTContainer<ObservableSplitLayoutPanel> container) {
		super(container);
		widget().setHeight("600px");
		leftContainer = new LeftSideContainer(container.parent);
		rightContainer = new RightSideContainer(container.parent);
		splitPosition(300);
		container.widget.owner = this;
	}

	@Override
	public void onResize(int splitPosition) {
		this.splitPosition = splitPosition;
		notifyListeners();
	}

	static SplitLayoutPanel newSplitPanel() {
		return new ObservableSplitLayoutPanel();
	}

	@Override
	public IContainer first() {
		return leftContainer;
	}

	@Override
	public IContainer second() {
		return rightContainer;
	}

	@Override
	public ISplitPane vertical() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ISplitPane splitPosition(int pixel) {
		if (pixel < 1)
			pixel = 300;
		splitPosition = pixel;
		holdNotify = true;
		container.widget.updatePosition();
		holdNotify = false;
		notifyListeners();
		return this;
	}

	private void notifyListeners() {
		if (holdNotify)
			return;
		for (ISplitPaneResizeListener l : listeners)
			l.onResize(splitPosition);
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

	private void prepare(Widget widget) {
		widget.setWidth("100%");
		widget.setHeight("100%");
	}

	@Override
	public int splitPosition() {
		return splitPosition;
	}

	public void hideOverflow(Widget widget) {
		widget.getElement().getParentElement().getStyle()
				.setOverflow(Overflow.HIDDEN);
	}
}