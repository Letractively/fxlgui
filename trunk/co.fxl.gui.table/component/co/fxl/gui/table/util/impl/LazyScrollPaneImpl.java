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
	private int minRowHeight = 20;
	private int height = 400;
	private IContainer container;
	private ICardPanel contentPanel;
	private int rowIndex = 0;
	private IContainer lastCard = null;
	private int scrollPanelHeight;
	private IAbsolutePanel scrollContentPanel;
	private int size;
	private int rows2Paint = height / minRowHeight;
	private IScrollPane scrollPane;
	private int maxRowIndex;
	private int maxOffset;

	LazyScrollPaneImpl(IContainer container) {
		this.container = container;
	}

	@Override
	public ILazyScrollPane decorator(IDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public ILazyScrollPane minRowHeight(int height) {
		minRowHeight = height;
		rows2Paint = height / minRowHeight;
		return this;
	}

	@Override
	public ILazyScrollPane height(int height) {
		this.height = height;
		rows2Paint = height / minRowHeight;
		return this;
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
		final IDockPanel dock = container.panel().dock();
		dock.visible(false);
		dock.height(height);
		contentPanel = dock.center().panel().card();
		scrollPane = dock.right().scrollPane();
		scrollPane.size(35, height);
		scrollContentPanel = scrollPane.viewPort().panel().absolute();
		scrollContentPanel.add().label().text("&#160;");
		scrollPanelHeight = size * minRowHeight;
		scrollContentPanel.size(1, scrollPanelHeight);
		maxOffset = scrollPanelHeight - height;
		update(size - rows2Paint);
		dock.display().invokeLater(new Runnable() {

			@Override
			public void run() {
				int h = decorator.rowHeight(size - 1);
				maxRowIndex = size - 1;
				for (int i = size - 1; i >= rowIndex && h < height; i--) {
					int rowHeight = decorator.rowHeight(i);
					if (rowHeight < minRowHeight)
						rowHeight = minRowHeight;
					h += rowHeight;
					if (h < height) {
						maxRowIndex = i;
					}
				}
				update();
				dock.visible(true);
				scrollPane.addScrollListener(LazyScrollPaneImpl.this);
			}
		});
	}

	private void update() {
		assert rowIndex >= 0;
		update(rowIndex);
	}

	private void update(int rowIndex) {
		int lastIndex = rowIndex + rows2Paint - 1;
		if (lastIndex >= size)
			lastIndex = size - 1;
		IContainer invisibleCard = contentPanel.add();
		decorator.decorate(invisibleCard, rowIndex, lastIndex);
		contentPanel.show(invisibleCard.element());
		if (lastCard != null) {
			lastCard.element().remove();
		}
		lastCard = invisibleCard;
	}

	private int convertScrollOffset2RowIndex(int offset) {
		assert offset >= 0;
		double r = offset;
		r /= maxOffset;
		r *= maxRowIndex;
		return (int) r;
	}

	@Override
	public void onScroll(int maxOffset) {
		rowIndex = convertScrollOffset2RowIndex(maxOffset);
		update();
	}

	@Override
	public ILazyScrollPane scrollUp(int turns) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ILazyScrollPane scrollDown(int turns) {
		throw new MethodNotImplementedException();
	}
}
