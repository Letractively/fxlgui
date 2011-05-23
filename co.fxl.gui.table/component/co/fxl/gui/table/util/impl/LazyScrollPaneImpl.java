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
	private int[] rowHeight;
	private int height = 400;
	private IContainer container;
	private ICardPanel contentPanel;
	private int rowIndex = 0;
	private IContainer lastCard = null;
	private int scrollPanelHeight;
	private IAbsolutePanel scrollContentPanel;

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
		assert rowHeight != null : "size not set";
		for (int i = 0; i < rowHeight.length; i++)
			if (rowHeight[i] == 0)
				rowHeight[i] = height;
		return this;
	}

	@Override
	public ILazyScrollPane rowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		return this;
	}

	@Override
	public ILazyScrollPane rowHeight(int row, int height) {
		if (height >= rowHeight[row]) {
			scrollPanelHeight += height - rowHeight[row];
		} else
			throw new MethodNotImplementedException();
		rowHeight[row] = height;
		return this;
	}

	@Override
	public ILazyScrollPane size(int size) {
		rowHeight = new int[size];
		return this;
	}

	@Override
	public ILazyScrollPane visible(boolean visible) {
		if (visible) {
			IDockPanel dock = container.panel().dock();
			dock.height(height);
			contentPanel = dock.center().panel().card();
			update();
			IScrollPane sp = dock.right().scrollPane();
			sp.size(35, height);
			scrollContentPanel = sp.viewPort().panel().absolute();
			scrollPanelHeight = 0;
			for (int r : rowHeight)
				scrollPanelHeight += rowHeight[r];
			scrollContentPanel.size(1, scrollPanelHeight);
			sp.addScrollListener(this);
			scrollContentPanel.add().label().text("&#160;");
		} else
			throw new MethodNotImplementedException();
		return this;
	}

	private void update() {
		if (scrollContentPanel != null
				&& scrollPanelHeight != scrollContentPanel.height())
			scrollContentPanel.height(scrollPanelHeight);
		int lastIndex = rowIndex;
		for (int paintedHeight = 0; paintedHeight < height
				&& lastIndex < rowHeight.length; lastIndex++) {
			paintedHeight += rowHeight[lastIndex];
		}
		if (lastIndex >= rowHeight.length)
			lastIndex--;
		IContainer invisibleCard = contentPanel.add();
		decorator.decorate(invisibleCard, rowIndex, lastIndex);
		contentPanel.show(invisibleCard.element());
		if (lastCard != null) {
			lastCard.element().remove();
		}
		lastCard = invisibleCard;
	}

	private int convertScrollOffset2RowIndex(int maxOffset) {
		int offset = 0;
		for (int i = 0; i < rowHeight.length; i++) {
			offset += rowHeight[i];
			if (offset > maxOffset)
				return i;
		}
		return rowHeight.length - 1;
	}

	@Override
	public void onScroll(int maxOffset) {
		rowIndex = convertScrollOffset2RowIndex(maxOffset);
		update();
	}

	@Override
	public ILazyScrollPane height(int height) {
		this.height = height;
		return this;
	}
}
