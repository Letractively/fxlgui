/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
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
package co.fxl.gui.table.scroll.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.ISplitPane;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

class ResizableColumnSelection {

	private static final int DIVIDER_WIDTH = 7;
	private static final int MINIMUM = 50;

	private final class SplitListener implements IUpdateListener<Integer> {

		private ISplitPane split;
		private int width = -1;

		private SplitListener(ISplitPane split, int initialWidth) {
			this.split = split;
			this.width = initialWidth;
			listeners.add(this);
		}

		@Override
		public void onUpdate(Integer value) {
			if (value < MINIMUM) {
				split.splitPosition(width);
			} else
				width = value;
		}
	}

	private List<SplitListener> listeners = new LinkedList<SplitListener>();

	ResizableColumnSelection(final ScrollTableWidgetImpl widget) {
		IGridCell clear = widget.statusPanel().cell(1, 0).clear().align()
				.center();
		IDockPanel dock = clear.panel().dock();
		dock.left().label().text("Columns:").font().pixel(11).weight().bold();
		int width = 800;
		final int initialWidth = (width - (widget.columns.size() - 1) * 7)
				/ widget.columns.size();
		IContainer last = dock.center();
		for (int i = 0; i < widget.columns.size(); i++) {
			final ScrollTableColumnImpl c = widget.columns.get(i);
			ISplitPane split = null;
			IPanel<?> b = null;
			if (i == widget.columns.size() - 1) {
				b = last.panel().vertical().align().center();
			} else {
				split = last.splitPane().height(28);// .mininumSize(MINIMUM);
				split.splitPosition(initialWidth);
				if (i == 0)
					split.width(width);
				b = split.first().panel().vertical().align().center();
				// int remaining = widget.columns.size() - i - 1;
				// int minSize = remaining * MINIMUM + (remaining - 1)
				// * DIVIDER_WIDTH;
				// split.mininumSize(minSize);
			}
			if (c.visible)
				b.color().gray();
			ILabel l = b.add().label().text(c.name).autoWrap(true);
			l.font().pixel(11);
			if (c.visible)
				l.font().color().white();
			else
				l.font().color().gray();
			b.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					c.visible = !c.visible;
					boolean allInvisible = true;
					for (ScrollTableColumnImpl c1 : widget.columns)
						allInvisible &= !c1.visible;
					if (allInvisible)
						c.visible = true;
					else
						widget.update();
				}
			});
			if (split != null) {
				width -= initialWidth - DIVIDER_WIDTH;
				last = split.second();
			}
		}
	}
}
