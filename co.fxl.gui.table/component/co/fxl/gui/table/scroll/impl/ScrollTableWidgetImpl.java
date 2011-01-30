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

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.KeyAdapter;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.filter.api.IMiniFilterWidget;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ICell;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ILabelMouseListener;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IMouseWheelListener;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IRow;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableClickListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableColumn.IScrollTableListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;

class ScrollTableWidgetImpl implements IScrollTableWidget<Object>,
		IScrollListener, ILabelMouseListener {

	// TODO Swing Scroll Panel block increment for single click on arrow is not
	// enough

	private static final boolean ALLOW_RESIZE = false;
	private static final int HEADER_ROW_HEIGHT = 24;
	private static final int ROW_HEIGHT = 22;
	static final String ARROW_UP = "\u2191";
	static final String ARROW_DOWN = "\u2193";
	protected static final int SCROLL_MULT = 33;
	protected static final int MAX_SORT_SIZE = 100;
	private boolean adjustHeight = true;
	IVerticalPanel container;
	private int height = 400;
	private WidgetTitle widgetTitle;
	RowAdapter rows;
	private int paintedRows;
	List<ScrollTableColumnImpl> columns = new LinkedList<ScrollTableColumnImpl>();
	private SelectionImpl selection = new SelectionImpl(this);
	private int scrollPanelHeight;
	private int scrollOffset = -1;
	private ICardPanel contentPanel;
	private int sortColumn = -1;
	private int sortNegator = -1;
	IBulkTableWidget grid;
	int rowOffset = 0;
	List<IRow> highlighted = new LinkedList<IRow>();
	private IGridPanel statusPanel;
	private String tooltip = "";// Use CTRL + Click to select multiple rows.";
	private boolean visible;
	private int initialRowOffset = 100;
	private int initialPaintedRows;
	private int maxRowIndex;
	private IRows<Object> actualRows;

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
		this.actualRows = rows;
		if (visible) {
			visible(true);
		}
		return this;
	}

	private IGridPanel topPanel;

	@Override
	public IScrollTableWidget<Object> visible(boolean visible) {
		if (visible) {
			rows = new RowAdapter(actualRows);
			if (preselected != null) {
				rows.selected(preselected);
				initialRowOffset(preselected);
				preselected = null;
			}
			adjustHeight = true;
			this.visible = true;
			statusPanel = null;
			selectionIsSetup = false;
			container.clear();
			filter = null;
			if (rows.size() == 0) {
				IVerticalPanel dock = container.add().panel().vertical()
						.spacing(10);
				dock.height(height);
				topPanel = dock.add().panel().grid();
				topPanel.cell(0, 0).label().text("No rows found").font()
						.pixel(10).color().gray();
			} else {
				for (ScrollTableColumnImpl c : columns) {
					if (c.filterable) {
						if (filter == null) {
							topPanel = container.add().panel().grid();
							// container.addSpace(10);
							filter = (IMiniFilterWidget) topPanel.cell(0, 0)
									.widget(IMiniFilterWidget.class);
						}
						filter.addFilter().name(c.name).type(c.type);
						// TODO value constraint: choice of x elements
					}
				}
				if (filter != null) {
					filter.addSizeFilter();
					filter.visible(true);
					container.addSpace(10);
				}
				IDockPanel dock = container.add().panel().dock();
				dock.height(height);
				contentPanel = dock.center().panel().card();
				scrollOffset = 0;
				update();
				if (paintedRows != rows.size()) {
					initialPaintedRows = paintedRows;
					maxRowIndex = rows.size() - paintedRows;
					sp = dock.right().scrollPane();
					sp.size(35, height);
					h = sp.viewPort().panel().absolute();
					spHeight = height * (rows.size() + paintedRows);
					scrollPanelHeight = (int) (spHeight / paintedRows);
					h.size(1, scrollPanelHeight);
					sp.addScrollListener(this);
					ILabel blankToken = h.add().label().text("&#160;");
					if (initialRowOffset != -1) {
						int convertFromRowOffset = convertFromRowOffset(initialRowOffset);
						h.offset(blankToken, 0, convertFromRowOffset);
						sp.scrollIntoView(blankToken);
						initialRowOffset = -1;
					}
				}
			}
			if (buttonDecorator != null)
				buttonPanel(buttonDecorator);
		} else {
			this.visible = false;
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
	public IScrollTableColumn<Object> addColumn() {
		ScrollTableColumnImpl column = new ScrollTableColumnImpl(this,
				columns.size());
		columns.add(column);
		return column;
	}

	private int convertToRowOffset(int maxOffset) {
		if (scrollPanelHeight == 0 || height == 0)
			return 0;
		double rowHeight = height / initialPaintedRows;
		double index = maxOffset / rowHeight; // rows.size() * (maxOffset +
												// height) / scrollPanelHeight;
		int index2 = (int) index;
		if (index2 > maxRowIndex)
			index2 = maxRowIndex;
		return index2;
	}

	private int convertFromRowOffset(int rowOffset) {
		if (scrollPanelHeight == 0 || height == 0)
			return 0;
		double index = rowOffset;
		index *= scrollPanelHeight;
		index /= rows.size();
		return (int) index;
	}

	private boolean updating = false;
	private Map<ITableClickListener, KeyAdapter<Object>> listeners = new HashMap<ITableClickListener, KeyAdapter<Object>>();
	boolean selectionIsSetup = false;
	private IScrollPane sp;
	private IAbsolutePanel h;
	private double spHeight;
	private ISortListener sortListener;
	boolean addClickListeners = false;
	private IMiniFilterWidget filter;
	private boolean allowColumnSelection = true;
	private IFilterConstraints constraints;
	private IFilterListener filterListener;
	private IButtonPanelDecorator buttonDecorator;
	private ICommandButtons commandButtons;
	Object preselected;

	void initialRowOffset(Object object) {
		initialRowOffset = rows.find(object);
	}

	void update() {
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
			rowOffset = convertToRowOffset(usedScrollOffset);
			paintedRows = computeRowsToPaint();
			updateHeaderRow(grid);
			for (int r = 0; r < paintedRows; r++) {
				int index = r + rowOffset;
				updateSingleContentRow(grid, r, index);
			}
			grid.visible(true);
			if (sp != null)
				grid.addMouseWheelListener(new IMouseWheelListener() {

					@Override
					public void onUp(int turns) {
						int pos = Math.max(0, scrollOffset - turns
								* SCROLL_MULT);
						sp.scrollTo(pos);
					}

					@Override
					public void onDown(int turns) {
						int pos = Math.min(scrollPanelHeight, scrollOffset
								+ turns * SCROLL_MULT);
						sp.scrollTo(pos);
					}
				});
			if (addClickListeners) {
				int current = 0;
				for (ScrollTableColumnImpl c : columns)
					if (!c.clickListeners.isEmpty() && c.visible)
						grid.labelMouseListener(current++, this);
			}
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
			addDisplayingNote();
			if (allowColumnSelection) {
				if (ALLOW_RESIZE)
					new ResizableColumnSelection(this);
				else
					new ColumnSelection(this);
			}
			addSorting();
			selection.update();
			for (ITableClickListener l : listeners.keySet()) {
				KeyAdapter<Object> adp = listeners.get(l);
				@SuppressWarnings("unchecked")
				IKey<Object> key = (IKey<Object>) grid.addTableListener(l);
				adp.forward(key);
			}
			if (adjustHeight && h != null
					&& rowOffset >= maxRowIndex - initialPaintedRows) {
				dynamicallyGrowHeight();
			}
		} while (usedScrollOffset != scrollOffset);
		updating = false;
	}

	private void updateSingleContentRow(IBulkTableWidget grid,
			int visibleGridIndex, int contentIndex) {
		Object[] row = rows.row(contentIndex);
		int current = 0;
		for (ScrollTableColumnImpl c : columns)
			if (c.visible)
				c.decorate(rows.identifier(contentIndex),
						grid.cell(current++, visibleGridIndex), row[c.index]);
	}

	@SuppressWarnings("unused")
	private void updateHeaderRow(IBulkTableWidget grid) {
		int current = 0;
		for (int c = 0; c < columns.size(); c++) {
			if (!columns.get(c).visible)
				continue;
			ScrollTableColumnImpl columnImpl = columns.get(c);
			if (columnImpl.tagSortOrder != null) {
				sortColumn = columnImpl.index;
				sortNegator = columnImpl.tagSortOrder ? -1 : 1;
				columnImpl.tagSortOrder = null;
			}
			String name = columnImpl.name;
			if (sortColumn == c) {
				name += " " + (sortNegator == 1 ? ARROW_DOWN : ARROW_UP);
			}
			IColumn column = grid.column(current++);
			columnImpl.decorator().prepare(column);
			column.title(name);
			if (columnImpl.widthInt != -1)
				column.width(columnImpl.widthInt);
			else {
				if (columnImpl.widthDouble == -1 && ALLOW_RESIZE)
					columnImpl.widthDouble = 1d / columns.size();
				if (columnImpl.widthDouble != -1)
					column.width(columnImpl.widthDouble);
			}
		}
	}

	private void dynamicallyGrowHeight() {
		adjustHeight = false;
		grid.deferr(new Runnable() {
			public void run() {
				int gridRow = 1;
				int overflow = grid.tableHeight() - height;
				do {
					int rowHeight = grid.rowHeight(gridRow++);
					overflow -= rowHeight;
					maxRowIndex++;
				} while (overflow > 0 && maxRowIndex < rows.size());
				maxRowIndex++;
				scrollPanelHeight += ROW_HEIGHT;
				h.size(1, scrollPanelHeight);
			}
		});
	}

	private void addDisplayingNote() {
		IGridPanel.IGridCell clear = statusPanel().cell(2, 0).clear().valign()
				.center();
		clear.align().end();
		IHorizontalPanel p = clear.panel().horizontal().align().end().add()
				.panel().horizontal().align().end();
		if (constraints != null && constraints.rowIterator().hasPrevious()) {
			ILabel l = p.add().label().text("<<");
			l.hyperlink().font().pixel(10);
			p.addSpace(4);
			l.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					int nextPreviousRow = constraints.rowIterator()
							.nextPreviousRow();
					constraints.rowIterator().firstRow(nextPreviousRow);
					filterListener.onApply(constraints);
				}
			});
		}
		int rt = rowOffset + paintedRows;
		if (rt > rows.size())
			rt = rows.size();
		int firstRow = constraints != null ? constraints.rowIterator()
				.firstRow() : 0;
		String status = "Displaying rows " + (firstRow + rowOffset + 1) + " - "
				+ (firstRow + rt) + " of " + rows.size();
		p.add().label().text(status).font().pixel(10);
		if (constraints != null && constraints.rowIterator().hasNext()) {
			p.addSpace(4);
			ILabel l = p.add().label().text(">>");
			l.hyperlink().font().pixel(10);
			l.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					constraints.rowIterator().firstRow(
							constraints.rowIterator().nextFirstRow());
					filterListener.onApply(constraints);
				}
			});
		}
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

	private void addSorting() {
		boolean sortable = false;
		for (ScrollTableColumnImpl c : columns) {
			if (c.sortable) {
				sortable |= c.sortable;
			}
		}
		if (sortable) {
			grid.addTableListener(new ITableClickListener() {

				@Override
				public void onClick(int column, int row) {
					if (row != 0)
						return;

					ScrollTableColumnImpl columnImpl = columns
							.get(realColumn(column));
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

	private int realColumn(int column) {
		int c = column;
		for (int i = 0; i < column; i++)
			if (!columns.get(i).visible)
				c++;
		return c;
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
	public IKey<?> addTableClickListener(final ITableClickListener l) {
		ITableClickListener l2 = new ITableClickListener() {

			@Override
			public void onClick(int column, int row) {
				int convert2TableRow = convert2TableRow(row);
				l.onClick(realColumn(column), convert2TableRow);
			}
		};
		KeyAdapter<Object> keyAdapter = new KeyAdapter<Object>();
		listeners.put(l2, keyAdapter);
		return keyAdapter;
	}

	@Override
	public IScrollTableWidget<Object> addTooltip(String tooltip) {

		// TODO tooltip doesn't work for BulkTableWidgetImpl

		this.tooltip += (this.tooltip.equals("") ? "" : "\n") + tooltip;
		if (grid != null)
			grid.element().tooltip(tooltip);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> sortListener(
			co.fxl.gui.table.scroll.api.IScrollTableWidget.ISortListener l) {
		sortListener = l;
		return this;
	}

	@Override
	public void onOver(int column, int row) {
		if (row > 0 && !clickListeners(column).isEmpty()) {
			grid.showAsLink(column, row, true);
		}
	}

	@Override
	public void onOut(int column, int row) {
		if (row > 0 && !clickListeners(column).isEmpty()) {
			grid.showAsLink(column, row, false);
		}
	}

	@Override
	public void onClick(int column, int row) {
		if (!clickListeners(column).isEmpty()) {
			for (IScrollTableListener<Object> l : clickListeners(column))
				l.onClick(rows.identifier(convert2TableRow(row)));
		}
	}

	private List<IScrollTableListener<Object>> clickListeners(int column) {
		return columns.get(realColumn(column)).clickListeners;
	}

	@Override
	public IScrollTableWidget<Object> addFilterListener(final IFilterListener l) {
		filterListener = new IFilterListener() {

			@Override
			public void onApply(IFilterConstraints constraints) {
				ScrollTableWidgetImpl.this.constraints = constraints;
				l.onApply(constraints);
			}
		};
		if (filter != null) {
			filter.addFilterListener(filterListener);
		}
		return this;
	}

	@Override
	public IScrollTableWidget<Object> refresh() {
		if (filter != null)
			filter.apply();
		else
			visible(true);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> allowColumnSelection(
			boolean allowColumnSelection) {
		this.allowColumnSelection = allowColumnSelection;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> constraints(IFilterConstraints constraints) {
		this.constraints = constraints;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> buttonPanel(IButtonPanelDecorator dec) {
		if (topPanel != null) {
			dec.decorate(topPanel.cell(1, 0).align().end());
		} else
			buttonDecorator = dec;
		return this;
	}

	void editable(int gridRowIndex, boolean editable) {
		for (ScrollTableColumnImpl c : columns) {
			if (c.editable) {
				ICell cell = grid.cell(c.index, gridRowIndex);
				IContainer container = cell.container().clear();
				if (editable) {
					container.textField().text("edit");
				} else {
					int dataRow = convert2TableRow(gridRowIndex);
					c.decorate(rows.identifier(dataRow), cell,
							rows.row(dataRow)[c.index]);
				}
			}
		}
	}

	@Override
	public co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons commandButtons() {
		if (commandButtons == null) {
			commandButtons = new CommandButtonsImpl(this);
		}
		return commandButtons;
	}
}
