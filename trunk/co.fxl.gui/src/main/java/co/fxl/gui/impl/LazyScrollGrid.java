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
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.api.IScrollPane.IScrollListener;

public class LazyScrollGrid extends ScrollGridTemplate implements
		IScrollListener {

	private static final int RESIZE_INTERVALL = 50;
	private static final int INC = 400;
	private int paintedRows = 0;
	private boolean painting = false;
	private int scrollPosition;
	private int offsetHeight = 0;

	public LazyScrollGrid(IContainer container) {
		super(container);
	}

	@Override
	void setUp() {
		grid = scrollPanel.viewPort().panel().vertical().add().panel().grid();
		decorator.decorate(grid);
		scrollPanel.height(height);
		grid.spacing(0);
		grid.indent(0);
		grid.resize(maxColumns, Math.min(maxRows, RESIZE_INTERVALL));
		scrollPanel.addScrollListener(this);
		grid.spacing(spacing);
		grid.indent(indent);
		for (IGridClickListener l : listeners.keySet()) {
			String key = listeners.get(l);
			IKey<IGridPanel> keyCB = grid.addGridClickListener(l);
			if (SHIFT.equals(key)) {
				keyCB.shiftPressed();
			} else if (CTRL.equals(key))
				keyCB.ctrlPressed();
		}
		onScroll(0);
	}

	@Override
	public void onScroll(int max) {
		scrollPosition = max;
		if (painting)
			return;
		painting = true;
		while (check()) {
			int inc = 16;
			resize();
			for (int column = 0; column < maxColumns; column++) {
				IGridCell cell = grid.cell(column, paintedRows);
				decorator.decorate(column, paintedRows, cell);
				inc = Math.max(inc, cell.height());
			}
			offsetHeight += inc;
			paintedRows++;
		}
		painting = false;
	}

	private boolean check() {
		return paintedRows < maxRows
				&& offsetHeight < scrollPosition + height + INC;
	}

	private void resize() {
		if (grid.rows() > paintedRows)
			return;
		grid.resize(maxColumns,
				Math.min(paintedRows + RESIZE_INTERVALL, maxRows));
	}
}
