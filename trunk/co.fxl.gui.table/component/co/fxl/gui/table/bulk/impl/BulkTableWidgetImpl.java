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
package co.fxl.gui.table.bulk.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;

class BulkTableWidgetImpl implements IBulkTableWidget {

	private final class ColumnImpl implements IColumn {
		private final int column;
		private ILabel label;

		private ColumnImpl(int column) {
			this.column = column;
		}

		@Override
		public IColumn title(String title) {
			rowOffset = 1;
			if (label == null) {
				IGridCell cell = grid.cell(column, 0);
				IBorder b = cell.border();
				b.color().black();
				b.style().bottom();
				label = cell.label();
				label.font().pixel(14).weight().bold();
			}
			label.text(title);
			return this;
		}

		@Override
		public IColumn width(double width) {
			throw new MethodNotImplementedException();
		}

		@Override
		public IColumn width(int width) {
			throw new MethodNotImplementedException();
		}
	}

	private static final int ROW_HEIGHT = 22;
	private IGridPanel grid;
	private int rowOffset = 0;
	private IVerticalPanel mainPanel;
	private int numRows = 0;
	private int visibleRows = 30;
	private List<ColumnImpl> columns = new LinkedList<ColumnImpl>();
	private Map<Integer, Boolean> highlighted = new HashMap<Integer, Boolean>();
	private int height;

	BulkTableWidgetImpl(IContainer container) {
		mainPanel = container.panel().vertical();
		grid = mainPanel.add().panel().grid();
		grid.spacing(0);
		grid.indent(3);
	}

	@Override
	public void remove() {
		mainPanel.remove();
	}

	@Override
	public IColumn column(final int column) {
		while (columns.size() <= column) {
			ColumnImpl c = new ColumnImpl(columns.size());
			columns.add(c);
		}
		return columns.get(column);
	}

	@Override
	public ICell cell(final int column, final int row) {
		return new ICell() {

			@Override
			public ICell text(String text) {
				IGridCell cell = grid.cell(column, row + rowOffset);
				cell.label().text(text);
				IBorder b = cell.border();
				b.color().lightgray();
				b.style().bottom();
				if (row + 1 > numRows)
					numRows = row + 1;
				return this;
			}
		};
	}

	@Override
	public IRow row(final int row) {
		return new IRow() {

			@Override
			public IRow highlight(boolean highlight) {
				for (int c = 0; c < grid.columns(); c++) {
					IColor color = grid.cell(c, row + rowOffset).color();
					if (highlight) {
						color.mix().white().lightgray();
					} else
						color.remove();
					highlighted.put(row, highlight);
				}
				return this;
			}

			@Override
			public boolean isHighlight() {
				Boolean highlight = highlighted.get(row);
				return highlight != null && highlight;
			}
		};
	}

	@Override
	public IKey<?> addTableListener(final ITableListener l) {
		return grid.addGridClickListener(new IGridClickListener() {

			@Override
			public void onClick(int column, int row) {
				l.onClick(column, row);
			}
		});
	}

	@Override
	public int height() {
		return mainPanel.height();
	}

	@Override
	public IBulkTableWidget visible(boolean visible) {
		if (!visible)
			throw new MethodNotImplementedException();
		return this;
	}

	@Override
	public IBulkTableWidget height(int height) {
		this.height = height;
		mainPanel.height(height);
		if (numRows != 0) {
			double d = numRows;
			int mainPanelHeight = mainPanel.height();
			d *= mainPanelHeight;
			int gridHeight = grid.height();
			d /= gridHeight;
			visibleRows = (int) d;
		} else {
			visibleRows = height / ROW_HEIGHT;
		}
		return this;
	}

	@Override
	public int visibleRows() {
		height(height);
		return visibleRows;
	}

	@Override
	public IElement<?> element() {
		return mainPanel;
	}
}
