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

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IScrollListener;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IRow;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;

class ScrollTableWidgetImpl implements IScrollTableWidget<Object>,
		IScrollListener {

	// TODO show status line: showing rows 27-40 of 3000

	// TODO Swing Scroll Panel block increment for single click on arrow is not
	// enough

	private static final String ARROW_UP = "\u2191";
	private static final String ARROW_DOWN = "\u2193";
	IVerticalPanel container;
	private int height = 400;
	private WidgetTitle widgetTitle;
	RowAdapter rows;
	private int paintedRows;
	private List<ColumnImpl> columns = new LinkedList<ColumnImpl>();
	private SelectionImpl selection = new SelectionImpl(this);
	private int scrollPanelHeight;
	private int scrollOffset = -1;
	private ICardPanel contentPanel;
	private int sortColumn = -1;
	private int sortNegator = -1;
	IBulkTableWidget grid;
	int rowOffset;
	int visibleRows;
	List<IRow> highlighted = new LinkedList<IRow>();
	IGridPanel selectionPanel;

	ScrollTableWidgetImpl(IContainer container) {
		widgetTitle = new WidgetTitle(container.panel()).foldable(false);
		this.container = widgetTitle.content().panel().vertical();
	}

	@Override
	public int offsetY() {
		return container.offsetY();
	}

	@Override
	public IScrollTableWidget<Object> height(int height) {
		this.height = height;
		visible(true);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> rows(IRows<Object> rows) {
		this.rows = new RowAdapter(rows);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> visible(boolean visible) {
		if (visible) {
			selectionPanel = null;
			container.clear();
			container.addSpace(20);
			if (rows.size() == 0) {
				IVerticalPanel dock = container.add().panel().vertical();
				dock.height(height);
				dock.add().label().text("No rows found");
			} else {
				IDockPanel dock = container.add().panel().dock();
				dock.height(height);
				contentPanel = dock.center().panel().card();
				scrollOffset = 0;
				update();
				IScrollPane sp = dock.right().scrollPane();
				sp.size(35, height);
				IVerticalPanel v = sp.viewPort().panel().vertical();
				double spHeight = height * rows.size();
				scrollPanelHeight = (int) (spHeight / paintedRows);
				v.add().panel().horizontal().size(1, scrollPanelHeight);
				sp.addScrollListener(this);
			}
		} else {
			throw new MethodNotImplementedException();
		}
		return this;
	}

	@Override
	public void onScroll(int maxOffset) {
		boolean scroll = maxOffset != scrollOffset;
		scrollOffset = maxOffset;
		if (scroll)
			update();
	}

	@Override
	public IColumn addColumn() {
		ColumnImpl column = new ColumnImpl(columns.size());
		columns.add(column);
		return column;
	}

	private int convert(int maxOffset) {
		if (scrollPanelHeight == 0)
			return 0;
		double index = rows.size();
		index *= maxOffset;
		index /= scrollPanelHeight;
		return (int) index;
	}

	private boolean updating = false;

	private void update() {
		if (updating)
			return;
		updating = true;
		int usedScrollOffset;
		do {
			usedScrollOffset = scrollOffset;
			highlighted.clear();
			contentPanel.clear();
			IBulkTableWidget lastGrid = grid;
			grid = (IBulkTableWidget) contentPanel.add().widget(
					IBulkTableWidget.class);
			grid.height(height);
			paintedRows = Math.max(paintedRows, grid.visibleRows());
			rowOffset = convert(usedScrollOffset);
			for (int c = 0; c < columns.size(); c++) {
				String name = columns.get(c).name;
				if (sortColumn == c) {
					name += " " + (sortNegator == 1 ? ARROW_DOWN : ARROW_UP);
				}
				grid.column(c).title(name);
			}
			if (rowOffset + paintedRows >= rows.size()) {
				if (rowOffset == 0) {
					paintedRows = rows.size();
				} else
					paintedRows = rows.size() - rowOffset;
			}
			for (int r = 0; r < paintedRows; r++) {
				int index = r + rowOffset;
				Object[] row = rows.row(index);
				for (ColumnImpl c : columns)
					c.decorate(grid.cell(c.index, r), row[c.index]);
			}
			visibleRows = paintedRows;
			grid.visible(true);
			contentPanel.show(grid.element());
			if (lastGrid != null)
				lastGrid.remove();
			for (int r = 0; r < visibleRows; r++) {
				if (rows.selected(r + rowOffset)) {
					IRow row = grid.row(r);
					row.highlight(true);
					highlighted.add(row);
				}
			}
			paintedRows = Math.max(paintedRows, grid.visibleRows());
			updateSorting();
			selection.update();
		} while (usedScrollOffset != scrollOffset);
		updating = false;
	}

	public void highlightAll() {
		highlighted.clear();
		for (int r = 0; r < visibleRows; r++) {
			IRow row = grid.row(r);
			row.highlight(true);
			highlighted.add(row);
		}
	}

	private void updateSorting() {
		boolean sortable = false;
		for (ColumnImpl c : columns) {
			if (c.sortable) {
				sortable |= c.sortable;
			}
		}
		if (sortable) {
			grid.addTableListener(new ITableListener() {

				@Override
				public void onClick(int column, int row) {
					if (row != 0)
						return;
					ColumnImpl columnImpl = columns.get(column);
					if (columnImpl.sortable) {
						sortColumn = columnImpl.index;
						sortNegator = rows.sort(columnImpl);
						update();
					}
				}
			});
		}
	}

	@Override
	public ILabel addTitle(String text) {
		return widgetTitle.addTitle(text);
	}

	@Override
	public IClickable<?> addButton(String name) {
		return widgetTitle.addHyperlink(name);
	}

	@Override
	public ISelection<Object> selection() {
		return selection;
	}

	int convert2TableRow(int row) {
		return rowOffset + row;
	}

	int convert2GridRow(int row) {
		return row - rowOffset;
	}
}
