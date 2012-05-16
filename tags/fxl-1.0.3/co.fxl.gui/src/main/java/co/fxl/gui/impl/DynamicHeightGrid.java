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

import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridClickListener;

public class DynamicHeightGrid implements IScrollGrid {

	private IGridPanel grid;
	private int columns;
	private int rows;
	private ILazyGridDecorator decorator;

	public DynamicHeightGrid(IContainer container) {
		grid = container.panel().grid();
	}

	@Override
	public IScrollGrid columns(int columns) {
		this.columns = columns;
		return this;
	}

	@Override
	public IScrollGrid rows(int rows) {
		this.rows = rows;
		return this;
	}

	@Override
	public IScrollGrid height(int height) {
		return this;
	}

	@Override
	public IScrollGrid decorator(ILazyGridDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public IScrollGrid visible(boolean visible) {
		decorator.decorate(grid);
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				decorator.decorate(x, y, grid.cell(x, y));
			}
		}
		return this;
	}

	@Override
	public IScrollGrid spacing(int spacing) {
		grid.spacing(spacing);
		return this;
	}

	@Override
	public IScrollGrid indent(int indent) {
		grid.indent(indent);
		return this;
	}

	@Override
	public IScrollGrid addGridClickListener(IGridClickListener listener,
			String key) {
		IKey<IGridPanel> k = grid.addGridClickListener(listener);
		if (key!=null && key.equals(SHIFT))
			k.shiftPressed();
		if (key!=null && key.equals(CTRL))
			k.ctrlPressed();
		return this;
	}

	@Override
	public int offsetY() {
		return grid.offsetY();
	}

}
