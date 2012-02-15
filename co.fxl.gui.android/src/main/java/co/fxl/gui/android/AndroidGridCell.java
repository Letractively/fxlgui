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
package co.fxl.gui.android;

import android.view.View;
import android.widget.TableRow;
import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IGridPanel.IGridCell;

class AndroidGridCell extends AndroidContainer implements IGridCell {

	private TableRow row;
	private int columnIndex;

	AndroidGridCell(final AndroidDisplay androidDisplay,
			final AndroidGridPanel grid, final TableRow row,
			final int columnIndex) {
		super(new Parent() {

			@Override
			public void add(View view) {
				if (grid.spacing != 0) {
					// TODO adjust outer spacing
					view.setPadding(grid.spacing / 2, grid.spacing / 2,
							grid.spacing / 2, grid.spacing / 2);
				}
				TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
						columnIndex);
				row.addView(view, cellLp);
			}

			@Override
			public AndroidDisplay androidDisplay() {
				return androidDisplay;
			}

			@Override
			public View element() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void remove(View view) {
				throw new UnsupportedOperationException();
			}
		});
		this.row = row;
		this.columnIndex = columnIndex;
	}

	@Override
	public IAlignment<IGridCell> align() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBorder border() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IColor color() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IGridCell height(int height) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IAlignment<IGridCell> valign() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IGridCell visible(boolean visible) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IGridCell width(int width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IGridCell clear() {
		row.removeViewAt(columnIndex);
		return this;
	}

	@Override
	public int height() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int width() {
		throw new UnsupportedOperationException();
	}
}
