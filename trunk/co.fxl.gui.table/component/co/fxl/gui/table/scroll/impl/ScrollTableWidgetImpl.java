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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.KeyAdapter;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IMouseWheelListener;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IRow;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;

class ScrollTableWidgetImpl implements IScrollTableWidget<Object>,
		IScrollListener {

	// TODO show status line: showing rows 27-40 of 3000

	// TODO Swing Scroll Panel block increment for single click on arrow is not
	// enough

	private static final int HEADER_ROW_HEIGHT = 24;
	private static final int ROW_HEIGHT = 22;
	private static final String ARROW_UP = "\u2191";
	private static final String ARROW_DOWN = "\u2193";
	protected static final int SCROLL_MULT = 33;
	protected static final int MAX_SORT_SIZE = 100;
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
	List<IRow> highlighted = new LinkedList<IRow>();
	private IGridPanel statusPanel;
	private String tooltip = "Use CTRL + Click to select multiple rows.";

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
			statusPanel = null;
			selectionIsSetup = false;
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
				if (paintedRows == rows.size())
					return this;
				sp = dock.right().scrollPane();
				sp.size(35, height);
				h = sp.viewPort().panel().vertical();
				spHeight = height * (rows.size() + 1);
				scrollPanelHeight = (int) (spHeight / paintedRows);
				h.size(1, scrollPanelHeight);
				sp.addScrollListener(this);
				h.add().label().text("&#160;");
			}
		} else {
			throw new MethodNotImplementedException();
		}
		return this;
	}

	IGridPanel statusPanel() {
		if (statusPanel == null) {
			statusPanel = container.addSpace(10).add().panel().grid();
		}
		return statusPanel;
	}

	@Override
	public void onScroll(int maxOffset) {
		boolean scroll = maxOffset != scrollOffset;
		if (scroll) {
			// TODO Swing if (maxOffset > scrollOffset
			// && maxOffset - scrollOffset < MIN_SCROLL_INCREMENT) {
			// maxOffset = Math.min(scrollOffset + MIN_SCROLL_INCREMENT,
			// scrollPanelHeight);
			// }
			// if (maxOffset < scrollOffset
			// && maxOffset - scrollOffset < MIN_SCROLL_INCREMENT) {
			// maxOffset = Math.min(scrollOffset + MIN_SCROLL_INCREMENT,
			// scrollPanelHeight);
			// }
			scrollOffset = maxOffset;
			update();
			// if (paintedRows <= rows.size()) {
			// scrollPanelHeight = 1;
			// } else {
			// scrollPanelHeight = (int) (spHeight / paintedRows);
			// // }
			// h.size(1, scrollPanelHeight);
		}
	}

	@Override
	public IColumn<Object> addColumn() {
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
	private Map<ITableListener, KeyAdapter<Object>> listeners = new HashMap<ITableListener, KeyAdapter<Object>>();
	boolean selectionIsSetup = false;
	private IScrollPane sp;
	private IPanel<?> h;
	private double spHeight;
	private ISortListener sortListener;

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
			paintedRows = computeRowsToPaint();
			rowOffset = convert(usedScrollOffset);
			for (int c = 0; c < columns.size(); c++) {
				ColumnImpl columnImpl = columns.get(c);
				if (columnImpl.tagSortOrder != null) {
					sortColumn = columnImpl.index;
					sortNegator = columnImpl.tagSortOrder ? -1 : 1;
					columnImpl.tagSortOrder = null;
				}
				String name = columnImpl.name;
				if (sortColumn == c) {
					name += " " + (sortNegator == 1 ? ARROW_DOWN : ARROW_UP);
				}
				grid.column(c).title(name);
			}
			for (int r = 0; r < paintedRows; r++) {
				int index = r + rowOffset;
				Object[] row = rows.row(index);
				for (ColumnImpl c : columns)
					c.decorate(rows.identifier(index), grid.cell(c.index, r),
							row[c.index]);
			}
			grid.visible(true);
			grid.addMouseWheelListener(new IMouseWheelListener() {

				@Override
				public void onUp(int turns) {
					int pos = Math.max(0, scrollOffset - turns * SCROLL_MULT);
					sp.scrollTo(pos);
					// onScroll(pos);
				}

				@Override
				public void onDown(int turns) {
					int pos = Math.min(scrollPanelHeight, scrollOffset + turns
							* SCROLL_MULT);
					sp.scrollTo(pos);
					// onScroll(pos);
				}
			});
			grid.element().tooltip(tooltip);
			contentPanel.show(grid.element());
			if (lastGrid != null)
				lastGrid.remove();
			for (int r = 0; r < paintedRows; r++) {
				if (rows.selected(r + rowOffset)) {
					IRow row = grid.row(r);
					row.highlight(true);
					highlighted.add(row);
				}
			}
			IGridCell clear = statusPanel().cell(1, 0).clear();
			int rt = rowOffset + paintedRows;
			if (rowOffset > 0 || rt < rows.size()) {
				if (rt > rows.size())
					rt = rows.size();
				String status = "Displaying rows " + (rowOffset + 1) + " - "
						+ rt + " of " + rows.size();
				clear.align().end().label().text(status).font().pixel(10);
			}
			updateSorting();
			selection.update();
			for (ITableListener l : listeners.keySet()) {
				KeyAdapter<Object> adp = listeners.get(l);
				@SuppressWarnings("unchecked")
				IKey<Object> key = (IKey<Object>) grid.addTableListener(l);
				adp.forward(key);
			}
		} while (usedScrollOffset != scrollOffset);
		updating = false;
	}

	private int computeRowsToPaint() {
		int paintedRows = 0;
		int prognosedHeight = HEADER_ROW_HEIGHT;
		while (prognosedHeight < height) {
			prognosedHeight += ROW_HEIGHT;
			paintedRows++;
		}
		paintedRows--;
		if (rowOffset + paintedRows >= rows.size())
			paintedRows = rows.size() - rowOffset;
		return paintedRows;
	}

	public void highlightAll() {
		highlighted.clear();
		for (int r = 0; r < paintedRows; r++) {
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
						if (rows.size() < MAX_SORT_SIZE || sortListener == null) {
							sortColumn = columnImpl.index;
							sortNegator = rows.sort(columnImpl);
							update();
						} else {
							if (sortColumn != -1) {
								sortNegator = sortColumn == column ? sortNegator
										* -1
										: 1;
							}
							sortColumn = columnImpl.index;
							sortListener.onSort(columnImpl.name,
									sortNegator == 1);
						}
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

	@Override
	public IKey<?> addTableListener(final ITableListener l) {
		ITableListener l2 = new ITableListener() {

			@Override
			public void onClick(int column, int row) {
				int convert2TableRow = convert2TableRow(row);
				l.onClick(column, convert2TableRow);
			}
		};
		KeyAdapter<Object> keyAdapter = new KeyAdapter<Object>();
		listeners.put(l2, keyAdapter);
		return keyAdapter;
	}

	@Override
	public IScrollTableWidget<?> addTooltip(String tooltip) {

		// TODO tooltip doesn't work for BulkTableWidgetImpl

		this.tooltip += "\n" + tooltip;
		if (grid != null)
			grid.element().tooltip(tooltip);
		return this;
	}

	@Override
	public IScrollTableWidget<?> sortListener(
			co.fxl.gui.table.scroll.api.IScrollTableWidget.ISortListener l) {
		sortListener = l;
		return this;
	}
}
