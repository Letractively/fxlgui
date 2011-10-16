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
import co.fxl.gui.impl.Display;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTSplitPane extends GWTElement<Widget, ISplitPane> implements
		ISplitPane, ISplitPaneResizeListener {

	public static ISplitPanelAdp adapter = new ISplitPanelAdp() {

		private boolean isScheduled = false;

		@Override
		public void setLeftWidget(Widget p, Widget component) {
			((HorizontalSplitPanel) p).setLeftWidget(component);
		}

		@Override
		public void setRightWidget(Widget p, Widget component) {
			((HorizontalSplitPanel) p).setRightWidget(component);
		}

		@Override
		public void addListener(final Widget widget,
				final ISplitPaneResizeListener listener) {
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					Element rootElement = widget.getElement();
					final EventListener oldListener = DOM
							.getEventListener(rootElement);
					DOM.setEventListener(rootElement, new EventListener() {
						public void onBrowserEvent(Event event) {
							final HorizontalSplitPanel horizontalSplitPanel = (HorizontalSplitPanel) widget;
							boolean fire = false;
							if ((event.getTypeInt() == Event.ONMOUSEUP || event
									.getTypeInt() == Event.ONMOUSEMOVE)
									&& horizontalSplitPanel.isResizing()) {
								fire = true;
							}
							oldListener.onBrowserEvent(event);
							if (fire && !isScheduled) {
								isScheduled = true;
								Display.instance().invokeLater(new Runnable() {
									@Override
									public void run() {
										listener.onResize(widget
												.getOffsetWidth()
												- horizontalSplitPanel
														.getRightWidget()
														.getOffsetWidth() - 2);
										isScheduled = false;
									}
								});
							}
						}
					});
				}
			});
		}

		@Override
		public void setSplitPosition(Widget p, String string) {
			((HorizontalSplitPanel) p).setSplitPosition(string);
		}

		@Override
		public Widget newSplitPanel() {
			return new HorizontalSplitPanel();
		}

		@Override
		public boolean supportsListeners() {
			return true;
		}

	};

	public interface ISplitPanelAdp {

		void setLeftWidget(Widget p, Widget component);

		void setRightWidget(Widget p, Widget component);

		void addListener(Widget widget, ISplitPaneResizeListener listener);

		boolean supportsListeners();

		void setSplitPosition(Widget p, String string);

		Widget newSplitPanel();

	}

	private final class LeftSideContainer extends GWTContainer<Widget> {
		private final Widget p;

		private LeftSideContainer(WidgetParent parent, Widget p) {
			super(parent);
			this.p = p;
		}

		@Override
		public void setComponent(Widget component) {
			widget = component;
			prepare(component);
			adapter.setLeftWidget(p, component);
			hideOverflow(component);
		}
	}

	private final class RightSideContainer extends GWTContainer<Widget> {
		private final Widget p;

		private RightSideContainer(WidgetParent parent, Widget p) {
			super(parent);
			this.p = p;
		}

		@Override
		public void setComponent(Widget component) {
			widget = component;
			prepare(component);
			adapter.setRightWidget(p, component);
		}
	}

	private List<ISplitPaneResizeListener> listeners = new LinkedList<ISplitPaneResizeListener>();
	private GWTContainer<Widget> leftContainer;
	private GWTContainer<Widget> rightContainer;
	private int splitPosition;
	private boolean holdNotify;

	GWTSplitPane(GWTContainer<Widget> container) {
		super(container);
		container.widget.setHeight("600px");
		setUpContainers(container);
		splitPosition(300);
		adapter.addListener(container.widget, this);
	}

	public void setUpContainers(final GWTContainer<Widget> container) {
		final Widget p = (Widget) container.widget;
		leftContainer = new LeftSideContainer(container.parent, p);
		rightContainer = new RightSideContainer(container.parent, p);
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
		throw new MethodNotImplementedException();
	}

	@Override
	public ISplitPane splitPosition(int pixel) {
		splitPosition = pixel;
		Widget p = (Widget) container.widget;
		holdNotify = true;
		adapter.setSplitPosition(p, pixel + "px");
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
		if (!adapter.supportsListeners())
			throw new MethodNotImplementedException();
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

	@Override
	public void onResize(int splitPosition) {
		this.splitPosition = splitPosition;
		notifyListeners();
	}
}