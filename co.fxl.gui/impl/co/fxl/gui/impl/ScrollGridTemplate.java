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
package co.fxl.gui.impl;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.api.IScrollPane;

abstract class ScrollGridTemplate implements IScrollGrid {

	int height = 600;
	int maxColumns;
	int maxRows;
	IScrollPane scrollPanel;
	IGridPanel grid;
	ILazyGridDecorator decorator;
	int spacing = 0;
	int indent = 0;
	Map<IGridClickListener, String> listeners = new HashMap<IGridClickListener, String>();

	public ScrollGridTemplate(IContainer container) {
		scrollPanel = container.scrollPane();
	}

	@Override
	public IScrollGrid columns(int columns) {
		maxColumns = columns;
		return this;
	}

	@Override
	public IScrollGrid rows(int rows) {
		maxRows = rows;
		return this;
	}

	@Override
	public IScrollGrid height(int height) {
		if (scrollPanel != null)
			scrollPanel.height(height);
		this.height = height;
		return this;
	}

	@Override
	public IScrollGrid decorator(ILazyGridDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public IScrollGrid visible(boolean visible) {
		if (visible) {
			setUp();
			return this;
		} else
			throw new MethodNotImplementedException();
	}

	abstract void setUp();

	@Override
	public IScrollGrid spacing(int spacing) {
		this.spacing = spacing;
		return this;
	}

	@Override
	public IScrollGrid indent(int indent) {
		this.indent = indent;
		return this;
	}

	@Override
	public IScrollGrid addGridClickListener(IGridClickListener listener,
			String key) {
		listeners.put(listener, key);
		return this;
	}

	@Override
	public int offsetY() {
		return scrollPanel.offsetY();
	}
}
