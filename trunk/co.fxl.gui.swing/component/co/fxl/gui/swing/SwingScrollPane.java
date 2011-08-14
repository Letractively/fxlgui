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

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IScrollPane;

class SwingScrollPane extends SwingElement<JScrollPane, IScrollPane> implements
		IScrollPane {

	private static final int BLOCK_INCREMENT = 22;
	private SwingContainer<JComponent> viewPort = new SwingContainer<JComponent>(
			container.parent) {

		void setComponent(JComponent component) {
			super.component = component;
			JViewport viewport = container.component.getViewport();
			viewport.setOpaque(false);
			viewport.setBackground(null);
			viewport.add(component);
		}
	};

	SwingScrollPane(SwingContainer<JScrollPane> container) {
		super(container);
		container.component.setAlignmentY(JScrollPane.TOP_ALIGNMENT);
		container.component.setOpaque(false);
		container.component.setBackground(null);
		container.component.setBorder(null);
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
				container.component.setOpaque(true);
				container.component.setBackground(color);
			}
		};

	}

	@Override
	public IScrollPane addScrollListener(final IScrollListener listener) {
		container.component.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {

					private int index = -1;

					@Override
					public void adjustmentValueChanged(AdjustmentEvent evt) {
						BoundedRangeModel model = container.component
								.getVerticalScrollBar().getModel();
						int max = model.getMaximum();
						int componentHeight = viewPort.component
								.getPreferredSize().height;
						if (componentHeight > max)
							return;
						// if (evt.getValueIsAdjusting()) {
						// return;
						// }
						int y = container.component.getVerticalScrollBar()
								.getValue();
						if (y != index) {
							index = y;
							listener.onScroll(y);
						}
						// int type = evt.getAdjustmentType();
						// int value = evt.getValue();
						// switch (type) {
						// case AdjustmentEvent.UNIT_INCREMENT:
						// listener.onScroll(value);
						// break;
						// case AdjustmentEvent.UNIT_DECREMENT:
						// break;
						// case AdjustmentEvent.BLOCK_INCREMENT:
						// listener.onScroll(value);
						// break;
						// case AdjustmentEvent.BLOCK_DECREMENT:
						// break;
						// case AdjustmentEvent.TRACK:
						// break;
						// }
					}
				});
		return this;
	}

	@Override
	public IScrollPane scrollTo(int pos) {
		BoundedRangeModel model = container.component.getVerticalScrollBar()
				.getModel();
		int max = model.getMaximum();
		int componentHeight = viewPort.component.getPreferredSize().height;
		if (componentHeight > max) {
			model.setMaximum(componentHeight);
			model.setExtent(container.component.getHeight());
		}
		container.component.getVerticalScrollBar().setValue(pos);
		return this;
	}

	@Override
	public IScrollPane scrollIntoView(IElement<?> element) {
		JComponent c = (JComponent) element.nativeElement();
		viewPort.component.scrollRectToVisible(c.getBounds(null));
		container.component.repaint();
		return this;
	}

	@Override
	public int scrollOffset() {
		return container.component.getVerticalScrollBar().getValue();
	}

	@Override
	public IScrollPane horizontal() {
		container.component
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		container.component
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return this;
	}

	@Override
	public void onUp(int turns) {
		onScrollTurns(-turns);
	}

	private void onScrollTurns(int i) {
		int newOffset = scrollOffset() + i * BLOCK_INCREMENT;
		if (newOffset < 0)
			newOffset = 0;
		scrollTo(newOffset);
	}

	@Override
	public void onDown(int turns) {
		onScrollTurns(turns);
	}

	@Override
	public IScrollPane showScrollbarsAlways(boolean showScrollbarsAlways) {
		if (container.component.getVerticalScrollBarPolicy() == JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED) {
			container.component
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		if (container.component.getHorizontalScrollBarPolicy() == JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
			container.component
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		return this;
	}
}
