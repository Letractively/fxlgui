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
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.util.api.ILazyScrollPane;

public class LazyScrollPaneImpl implements ILazyScrollPane, IScrollListener {

	public static final int HEIGHT_SCROLL_BAR = 17;
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
	private boolean adjustHeights = true;

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
	public ILazyScrollPane adjustHeights(boolean adjustHeights) {
		this.adjustHeights = adjustHeights;
		return this;
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
		final IVerticalPanel v = container.panel().vertical();
		final IDockPanel dock = v.add().panel().dock();
		dock.visible(false);
		dock.height(height);
		contentPanel = dock.center().panel().card();
		scrollPane = dock.right().scrollPane();
		scrollPane.size(35, height);
		scrollContentPanel = scrollPane.viewPort().panel().absolute();
		final ILabel token = scrollContentPanel.add().label().text("&#160;");
		int scrollPanelHeight = size * minRowHeight;
		scrollContentPanel.size(1, scrollPanelHeight);
		maxOffset = scrollPanelHeight - height;
		if (maxOffset <= 0)
			maxOffset = 1;
		final int firstIndex = size - rows2Paint;
		assert firstIndex >= 0;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (!v.visible())
					return;
				assert firstIndex >= 0;
				int h = adjustHeights ? decorator.rowHeight(lastIndex)
						: minRowHeight;
				maxRowIndex = lastIndex;
				for (int i = lastIndex - 1; i >= firstIndex && h < height; i--) {
					int rowHeight = Math.max(minRowHeight,
							adjustHeights ? decorator.rowHeight(i)
									: minRowHeight);
					h += rowHeight;
					if (h < height) {
						maxRowIndex = i;
					}
				}
				assert maxRowIndex >= 0;
				if (rowIndex > maxRowIndex)
					rowIndex = maxRowIndex;
				if (rowIndex > 0) {
					int y = convertRowIndex2ScrollOffset(rowIndex);
					token.offset(0, y);
					scrollPane.scrollTo(y);
					scrollPane.scrollIntoView(token);
				}
				update();
				scrollPane.addScrollListener(LazyScrollPaneImpl.this);
				dock.visible(true);
			}
		};
		if (adjustHeights) {
			update(firstIndex);
			dock.display().invokeLater(runnable);
		} else {
			lastIndex = size - 1;
			runnable.run();
		}
	}

	private void update() {
		assert rowIndex >= 0;
		update(rowIndex);
	}

	private int update(int rowIndex) {
		setLastIndex(rowIndex);
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
		return lastIndex;
	}

	private void setLastIndex(int rowIndex) {
		lastIndex = rowIndex + rows2Paint - 1;
		if (lastIndex >= size)
			lastIndex = size - 1;
		else if (lastIndex < 0)
			lastIndex = 0;
	}

	private int convertScrollOffset2RowIndex(int offset) {
		assert offset >= 0;
		assert maxOffset > 0 : maxOffset + " is scroll offset";
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

	@Override
	public void onUp(int turns) {
		scrollPane.onUp(turns);
	}

	@Override
	public void onDown(int turns) {
		scrollPane.onDown(turns);
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

	@Override
	public void remove() {
		container.clear();
	}
}
