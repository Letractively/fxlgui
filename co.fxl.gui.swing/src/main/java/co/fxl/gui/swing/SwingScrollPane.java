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
package co.fxl.gui.swing;

import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IScrollPane;

class SwingScrollPane extends SwingElement<JScrollPane, IScrollPane> implements
		IScrollPane {

	private SwingContainer<JComponent> viewPort = new SwingContainer<JComponent>(
			container.parent) {

		void setComponent(JComponent component) {
			super.component = component;
			JViewport viewport = component().getViewport();
			viewport.setOpaque(false);
			viewport.setBackground(null);
			viewport.add(component);
		}
	};
	private List<IScrollListener> listeners = new LinkedList<IScrollListener>();
	boolean programmaticSet;

	SwingScrollPane(final SwingContainer<JScrollPane> container) {
		super(container);
		container.component.setAlignmentY(JScrollPane.TOP_ALIGNMENT);
		container.component.setOpaque(false);
		container.component.setBackground(null);
		container.component.setBorder(null);
		container.component.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {

					private int index = 0;

					@Override
					public void adjustmentValueChanged(AdjustmentEvent evt) {
						if (viewPort == null || viewPort.component == null)
							return;
						BoundedRangeModel model = container.component
								.getVerticalScrollBar().getModel();
						int max = model.getMaximum();
						int componentHeight = viewPort.component
								.getPreferredSize().height;
						if (componentHeight > max)
							return;
						int y = container.component.getVerticalScrollBar()
								.getValue();
						if (y != index) {
							index = y;
							fireScrollListeners(y);
						}
					}
				});
	}

	void fireScrollListeners(int y) {
		for (IScrollListener l : listeners)
			l.onScroll(y);
	}

	@Override
	public IContainer viewPort() {
		return viewPort;
	}

	@Override
	public IColor color() {
		return new SwingColor() {
			@Override
			protected void setColor(Color color) {
				component().setOpaque(true);
				component().setBackground(color);
			}
		};

	}

	@Override
	public IScrollPane addScrollListener(final IScrollListener listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public IScrollPane scrollTo(int pos) {
		programmaticSet = true;
		BoundedRangeModel model = component().getVerticalScrollBar()
				.getModel();
		int max = model.getMaximum();
		int componentHeight = viewPort.component.getPreferredSize().height;
		if (componentHeight > max) {
			model.setMaximum(componentHeight);
			model.setExtent(component().getHeight());
		}
		component().getVerticalScrollBar().setValue(pos);
		programmaticSet = false;
		return this;
	}

	@Override
	public IScrollPane scrollIntoView(IElement<?> element) {
		programmaticSet = true;
		JComponent c = (JComponent) element.nativeElement();
		viewPort.component.scrollRectToVisible(c.getBounds(null));
		component().repaint();
		programmaticSet = false;
		return this;
	}

	@Override
	public int scrollOffset() {
		return component().getVerticalScrollBar().getValue();
	}

	@Override
	public IScrollPane horizontal() {
		component()
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		component()
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
	public IScrollPane bidirectional() {
		component()
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		component()
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return this;
	}

	@Override
	public IScrollBars scrollBars() {
		return new IScrollBars() {

			@Override
			public IScrollPane always() {
				if (component().getVerticalScrollBarPolicy() == JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED) {
					component()
							.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				}
				if (component().getHorizontalScrollBarPolicy() == JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
					component()
							.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				}
				return SwingScrollPane.this;
			}

			@Override
			public IScrollPane never() {
				if (component().getVerticalScrollBarPolicy() == JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED) {
					component()
							.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				}
				if (component().getHorizontalScrollBarPolicy() == JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
					component()
							.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				}
				return SwingScrollPane.this;
			}

		};
	}

	private JScrollPane component() {
		return container.component;
	}
}
