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
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IScrollPane;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTScrollPane extends GWTElement<ScrollPanel, IScrollPane> implements
		IScrollPane {

	private int initialScrollPosition = -1;
	boolean programmaticSet;
	private List<IScrollListener> listeners = new LinkedList<IScrollListener>();

	// private boolean vertical = true;
	// private boolean horizontal = false;

	GWTScrollPane(final GWTContainer<ScrollPanel> container) {
		super(container);
		widget().setWidth("100%");
		widget().setAlwaysShowScrollBars(false);
		widget().getElement().getStyle().setProperty("overflowX", "hidden");
		container.widget.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				final int scrollPosition = container.widget.getScrollPosition();
				if (initialScrollPosition != -1
						&& scrollPosition != initialScrollPosition) {
					int lp = initialScrollPosition;
					initialScrollPosition = -1;
					container.widget.setScrollPosition(lp);
				} else
					fireScrollListeners(scrollPosition);
			}
		});
	}

	@Override
	public IContainer viewPort() {
		return new GWTContainer<Widget>(container.parent) {
			public void setComponent(Widget component) {
				super.widget = component;
				component.setWidth("100%");
				container.widget.setWidget(component);
			}
		};
	}

	@Override
	public IBorder border() {
		return new GWTWidgetBorder(container.widget);
	}

	@Override
	public IColor color() {
		GWTWidgetStyle style = new GWTWidgetStyle("background-color-",
				container.widget);
		return new GWTStyleColor(style) {
			@Override
			void setColor(String color, com.google.gwt.dom.client.Style stylable) {
				stylable.setBackgroundColor(color);
			}
		};
	}

	@Override
	public IScrollPane addScrollListener(IScrollListener listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public IScrollPane scrollTo(int pos) {
		programmaticSet = true;
		// if (container.widget.getWidget() instanceof AbsolutePanel) {
		// AbsolutePanel ap = (AbsolutePanel) container.widget.getWidget();
		// ap.getElement().getStyle().setOverflow(Overflow.AUTO);
		// Label l = new Label("&#160;");
		// FocusPanel p = new FocusPanel(l);
		// ap.add(p, 0, pos);
		// p.getElement().scrollIntoView();
		// ap.remove(p);
		// } else
		// if (container.widget.isVisible())
		container.widget.setScrollPosition(pos);
		// else
		initialScrollPosition = pos;
		programmaticSet = false;
		return this;
	}

	@Override
	public IScrollPane scrollIntoView(IElement<?> element) {
		programmaticSet = true;
		Widget w = (Widget) element.nativeElement();
		Element e = w.getElement();
		e.scrollIntoView();
		programmaticSet = false;
		return this;
	}

	@Override
	public int scrollOffset() {
		return container.widget.getScrollPosition();
	}

	@Override
	public IScrollPane horizontal() {
		// vertical = false;
		// horizontal = true;
		container.widget.getElement().getStyle()
				.setProperty("overflowX", "auto");
		container.widget.getElement().getStyle()
				.setProperty("overflowY", "hidden");
		return this;
	}

	@Override
	public IScrollPane bidirectional() {
		// vertical = true;
		// horizontal = true;
		container.widget.getElement().getStyle()
				.setProperty("overflowX", "auto");
		container.widget.getElement().getStyle()
				.setProperty("overflowY", "auto");
		return this;
	}

	// @Override
	// public void onUp(int turns) {
	// onScrollTurns(-turns);
	// }
	//
	// private void onScrollTurns(int i) {
	// int newOffset = scrollOffset() + i * BLOCK_INCREMENT;
	// if (newOffset < 0)
	// newOffset = 0;
	// scrollTo(newOffset);
	// }
	//
	// @Override
	// public void onDown(int turns) {
	// onScrollTurns(turns);
	// }

	@Override
	public IScrollPane showScrollbarsAlways(boolean showScrollbarsAlways) {
		container.widget.setAlwaysShowScrollBars(showScrollbarsAlways);
		return this;
	}

	//
	// @Override
	// public IScrollbar scrollbar() {
	// return new IScrollbar() {
	//
	// @Override
	// public IScrollPane always() {
	// if (horizontal) {
	// container.widget.getElement().getStyle()
	// .setOverflowX(Overflow.SCROLL);
	// }
	// if (vertical) {
	// container.widget.getElement().getStyle()
	// .setOverflowY(Overflow.SCROLL);
	// }
	// return GWTScrollPane.this;
	// }
	// };
	// }

	void fireScrollListeners(int scrollPosition) {
		for (IScrollListener listener : listeners)
			listener.onScroll(scrollPosition);
	}
}