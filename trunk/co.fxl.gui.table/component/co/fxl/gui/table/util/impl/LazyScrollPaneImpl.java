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
package co.fxl.gui.table.util.impl;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.table.util.api.ILazyScrollPane;

class LazyScrollPaneImpl implements ILazyScrollPane, IScrollListener {

	private IDecorator decorator;
	private int minRowHeight = 22;
	private int height = 400;
	private IContainer container;
	private ICardPanel contentPanel;
	private int rowIndex = 0;
	private IContainer lastCard = null;
	private IAbsolutePanel scrollContentPanel;
	private int size;
	private int rows2Paint = height / minRowHeight;
	private IScrollPane scrollPane;
	private int maxRowIndex;
	private int maxOffset;
	private int lastIndex;
	private boolean horizontalScrollPane = false;

	LazyScrollPaneImpl(IContainer container) {
		this.container = container;
	}

	@Override
	public ILazyScrollPane decorator(IDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public ILazyScrollPane minRowHeight(int minRowHeight) {
		this.minRowHeight = minRowHeight;
		return updateRows2Paint();
	}

	private ILazyScrollPane updateRows2Paint() {
		rows2Paint = height / minRowHeight;
		if (rows2Paint > size)
			rows2Paint = size;
		return this;
	}

	@Override
	public ILazyScrollPane height(int height) {
		this.height = height;
		return updateRows2Paint();
	}

	@Override
	public ILazyScrollPane rowIndex(int rowIndex) {
		assert rowIndex >= 0;
		this.rowIndex = rowIndex;
		return this;
	}

	@Override
	public ILazyScrollPane size(int size) {
		this.size = size;
		return updateRows2Paint();
	}

	@Override
	public ILazyScrollPane visible(boolean visible) {
		if (visible) {
			draw();
		} else
			throw new MethodNotImplementedException();
		return this;
	}

	private void draw() {
		final IDockPanel dock = container.panel().dock();
		dock.visible(false);
		dock.height(height);
		contentPanel = dock.center().panel().card();
		scrollPane = dock.right().scrollPane();
		scrollPane.size(35, height);
		scrollContentPanel = scrollPane.viewPort().panel().absolute();
		scrollContentPanel.add().label().text("&#160;");
		int scrollPanelHeight = size * minRowHeight;
		scrollContentPanel.size(1, scrollPanelHeight);
		maxOffset = scrollPanelHeight - height;
		if (maxOffset < 0)
			maxOffset = 0;
		final int firstIndex = size - rows2Paint;
		assert firstIndex >= 0;
		update(firstIndex);
		dock.display().invokeLater(new Runnable() {

			@Override
			public void run() {
				assert firstIndex >= 0;
				int h = decorator.rowHeight(lastIndex);
				maxRowIndex = lastIndex;
				for (int i = lastIndex - 1; i >= firstIndex && h < height; i--) {
					int rowHeight = Math.max(minRowHeight,
							decorator.rowHeight(i));
					h += rowHeight;
					if (h < height) {
						maxRowIndex = i;
					}
				}
				// if (maxRowIndex < lastIndex - 1)
				// maxRowIndex++;
				assert maxRowIndex >= 0;
				if (rowIndex > maxRowIndex)
					rowIndex = maxRowIndex;
				if (rowIndex > 0) {
					scrollPane.scrollTo(convertRowIndex2ScrollOffset(rowIndex));
				}
				update();
				scrollPane.addScrollListener(LazyScrollPaneImpl.this);
				dock.visible(true);
			}
		});
	}

	private void update() {
		assert rowIndex >= 0;
		update(rowIndex);
	}

	private void update(int rowIndex) {
		lastIndex = rowIndex + rows2Paint - 1;
		if (lastIndex >= size)
			lastIndex = size - 1;
		else if (lastIndex < 0)
			lastIndex = 0;
		IContainer invisibleCard = contentPanel.add();
		IContainer c = invisibleCard;
		if (horizontalScrollPane) {
			c = c.scrollPane().horizontal().viewPort();
		}
		decorator.decorate(c, rowIndex, lastIndex);
		contentPanel.show(invisibleCard.element());
		if (lastCard != null) {
			lastCard.element().remove();
		}
		lastCard = invisibleCard;
	}

	private int convertScrollOffset2RowIndex(int offset) {
		assert offset >= 0;
		assert maxOffset > 0;
		double r = offset;
		r /= maxOffset;
		r *= maxRowIndex;
		return (int) r;
	}

	private int convertRowIndex2ScrollOffset(int rowIndex) {
		double r = rowIndex;
		r *= maxOffset;
		r /= maxRowIndex;
		return (int) r;
	}

	@Override
	public void onScroll(int maxOffset) {
		rowIndex = convertScrollOffset2RowIndex(maxOffset);
		assert rowIndex >= 0 : maxOffset + " offset not valid: " + rowIndex;
		update();
	}

	private int increment(int turns) {
		double inc = maxOffset * turns;
		inc /= maxRowIndex;
		return (int) inc;
	}

	@Override
	public ILazyScrollPane onUp(int turns) {
		int scrollPanelIndex = scrollPane.scrollOffset() - increment(turns);
		scrollPane.scrollTo(scrollPanelIndex);
		return this;
	}

	@Override
	public ILazyScrollPane onDown(int turns) {
		int scrollPanelIndex = scrollPane.scrollOffset() + increment(turns);
		scrollPane.scrollTo(scrollPanelIndex);
		return this;
	}

	@Override
	public ILazyScrollPane horizontalScrollPane(boolean horizontalScrollPane) {
		this.horizontalScrollPane = horizontalScrollPane;
		return this;
	}

	@Override
	public ILazyScrollPane refresh() {
		update(rowIndex);
		return this;
	}
}
