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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseWheelListener;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.filter.api.IMiniFilterWidget;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.impl.KeyAdapter;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ICell;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ILabelMouseListener;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IRow;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableClickListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableColumn.IScrollTableListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.util.api.IDragDropListener;
import co.fxl.gui.table.util.api.ILazyScrollPane;

class ScrollTableWidgetImpl implements IScrollTableWidget<Object>,
		ILabelMouseListener {

	// TODO Usability: Table jumps up and down when scrolling (fix on top)

	// TODO Swing: native implementation: required for automated testing

	// TODO 2DECIDE: Option: DESIGN: Look: Scroll-Table-Widget: use fixed column
	// widths / Look: use a heuristic on the client side
	// to determine column width

	// TODO FEATURE: Option: Usability: Table-Live-Filter: Implement live filter
	// on current table
	// content, every update notification in filter-widget on the side is
	// directly applied to the loaded table content

	// TODO FEATURE: Option: Usability: Enable Drag&Drop for main table (in
	// general: tables without reordering feature): moves displayed range up /
	// down

	// TODO Swing Scroll Panel block increment for single click on arrow is not
	// enough

	private static final boolean ALLOW_RESIZE = false;
	private static final int HEADER_ROW_HEIGHT = 24;
	private static final int ROW_HEIGHT = 22;
	static final String ARROW_UP = "\u2191";
	static final String ARROW_DOWN = "\u2193";
	protected static final int SCROLL_MULT = 33;
	protected static final int MAX_SORT_SIZE = 100;
	IVerticalPanel container;
	private int height = 400;
	private WidgetTitle widgetTitle;
	RowAdapter rows;
	private int paintedRows;
	List<ScrollTableColumnImpl> columns = new LinkedList<ScrollTableColumnImpl>();
	private SelectionImpl selection = new SelectionImpl(this);
	private IVerticalPanel contentPanel;
	private int sortColumn = -1;
	private int sortNegator = -1;
	IBulkTableWidget grid;
	int rowOffset = 0;
	List<IRow> highlighted = new LinkedList<IRow>();
	private IGridPanel statusPanel;
	private String tooltip = "";// Use CTRL + Click to select multiple rows.";
	private boolean visible;
	private IRows<Object> actualRows;
	private IContainer c0;
	private boolean addBorders;

	// private IVerticalPanel editPanel;

	ScrollTableWidgetImpl(IContainer container) {
		c0 = container;
	}

	private WidgetTitle widgetTitle() {
		if (widgetTitle == null) {
			widgetTitle = new WidgetTitle(c0.panel(), addBorders)
					.foldable(false);
			widgetTitle.addToContextMenu(true);
			widgetTitle.commandsOnTop();
			widgetTitle.hyperlinkVisible(false);
			this.container = widgetTitle.content().panel().vertical();
			// editPanel = container.add().panel().vertical().visible(false);
		}
		return widgetTitle;
	}

	private IVerticalPanel container() {
		if (container == null) {
			widgetTitle();
		}
		return container;
	}

	// @Override
	// public IVerticalPanel editPanel() {
	// return editPanel;
	// }

	@Override
	public int offsetY() {
		return container().offsetY();
	}

	private boolean drawing = false;
	private Integer nextHeight = null;

	@Override
	public IScrollTableWidget<Object> height(final int height) {
		this.height = height;
		if (visible) {
			if (drawing) {
				if (nextHeight == null) {
					nextHeight = height;
					c0.display().invokeLater(new Runnable() {
						@Override
						public void run() {
							int height = nextHeight;
							nextHeight = null;
							height(height);
						}
					});
				} else {
					nextHeight = height;
				}
				return this;
			} else {
				drawing = true;
				visible(true);
				drawing = false;
			}
		}
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

	IGridPanel topPanel;
	boolean showNoRowsFound = true;
	private IVerticalPanel contentPanel0;
	private boolean reduceHeightIfEmpty = false;
	private boolean showConfiguration = true;

	@Override
	public IScrollTableWidget<Object> reduceHeightIfEmpty(
			boolean reduceHeightIfEmpty) {
		this.reduceHeightIfEmpty = reduceHeightIfEmpty;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> visible(boolean visible) {
		if (visible) {
			boolean updateSelection = false;
			if (sp != null)
				sp.visible(false);
			rows = new RowAdapter(actualRows);
			for (ScrollTableColumnImpl c : columns)
				if (c.forceSort) {
					sortBy(c, false);
				}
			if (commandButtons != null)
				commandButtons.reset();
			if (!preselectedList.isEmpty()) {
				if (preselectedIndex != -1) {
					preselectedIndex = rows.find(preselectedList.get(0));
					if (preselectedIndex == -1) {
						preselectedList.clear();
					} else {
						rows.selected(preselectedIndex, preselectedList.get(0));
						if (commandButtons != null) {
							commandButtons.selection = preselectedList.get(0);
							commandButtons.selectionIndex = preselectedIndex;
						}
					}
				} else {
					rows.selected(preselectedList);
					if (commandButtons != null)
						commandButtons.selection = preselectedList.get(0);
				}
				updateSelection = true;
			}
			this.visible = true;
			if (!externalStatusPanel)
				statusPanel = null;
			else
				resetStatusPanel();
			topPanel = null;
			buttonColumn = 1;
			selectionIsSetup = false;
			container().clear();
			topPanel();
			viewInc = 0;
			if (rows.size() == 0
					&& (constraints == null
							|| !constraints.isConstraintSpecified() || !hasFilter())) {
				IVerticalPanel dock = container.add().panel().vertical();
				if (showNoRowsFound) {
					topPanelCell(viewInc + 0, 0).width(10).label()
							.text("&#160;");
					IGridCell begin = topPanelCell(1, 0).valign().begin()
							.align().begin();
					IVerticalPanel nef = begin.panel().vertical();
					nef.add().panel().vertical().spacing(4).add().label()
							.text("NO ENTITIES FOUND").font().pixel(10).color()
							.gray();
					if (constraints != null && constraints.isSpecified()) {
						// && constraints.isConstraintSpecified()) {
						List<String[]> description = constraints.description();
						if (constraints.configuration() != null)
							description.add(0, new String[] { "Query",
									constraints.configuration() });
						IGridPanel gp = nef.addSpace(4).add().panel()
								.horizontal().align().begin().add().panel()
								.horizontal().align().begin().add().panel()
								.grid().resize(3, description.size())
								.spacing(4);
						gp.cell(0, 0).label().text("FILTER").font().pixel(9)
								.color().gray();
						int i = 0;
						for (String[] d : description) {
							gp.cell(1, i).label().text(d[0] + ":").font()
									.pixel(9).color().gray();
							if (d[1].length() > 24)
								d[1] = d[1].substring(0, 24) + "...";
							gp.cell(2, i++).label().autoWrap(true).text(d[1])
									.font().weight().bold().pixel(9).color()
									.gray();
						}
					}
					buttonColumn++;
				} else
					addFilter();
				dock.add().label().text("&#160;");
				int height = heightMinusTopPanel();
				// if (showNoRowsFound) {
				// hack for IE
				if (reduceHeightIfEmpty && height >= 80)
					height -= 30;
				// }
				dock.height(height);
				if (!externalStatusPanel) {
					IGridPanel statusPanel2 = statusPanel();
					statusPanel2.cell(0, 0).label().text("&#160;");
					statusPanel2.height(32);
				}
				setUpTopPanel();
			} else if (rows.size() == 0
					&& (constraints != null
							&& constraints.isConstraintSpecified() && hasFilter())) {
				addFilter();
				setUpTopPanel();
			} else {
				addFilter();
				setUpTopPanel();
				contentPanel0 = container.add().panel().vertical();
				contentPanel0.height(heightMinusTopPanel());
				contentPanel = contentPanel0.add().panel().vertical();
				boolean tooLarge = update();
				if (tooLarge || paintedRows != rows.size()) {
					contentPanel0.clear();
					sp = (ILazyScrollPane) contentPanel0.add().widget(
							ILazyScrollPane.class);
					if (dragDropListener != null)
						sp.dragDropListener(allowInsertUnder, dragDropListener);
					sp.minRowHeight(getRowHeight());
					if (!preselectedList.isEmpty()) {
						int rowIndex = rows.find(preselectedList);
						if (rowIndex != -1)
							sp.rowIndex(rowIndex);
						if (this.rowIndex != -1) {
							sp.rowIndex(this.rowIndex);
							this.rowIndex = -1;
						}
					}
					sp.height(heightMinusTopPanel());
					sp.decorator(new ILazyScrollPane.IDecorator() {

						@Override
						public void decorate(IContainer container,
								int firstRow, int lastRow, boolean isCalibration) {
							rowOffset = firstRow;
							paintedRows = lastRow - firstRow + 1;
							contentPanel = container.panel().vertical();
							updateWithPaintedRowsSet(isCalibration);
						}

						@Override
						public boolean checkIndex(int rowIndex) {
							int visibleRowIndex = convert2GridRow(rowIndex);
							if (visibleRowIndex >= grid.rowCount()
									|| visibleRowIndex < 0)
								return false;
							return true;
						}

						@Override
						public int rowHeight(int rowIndex) {
							int visibleRowIndex = convert2GridRow(rowIndex);
							return grid.rowHeight(visibleRowIndex + 1);
						}

						@Override
						public IElement<?> elementAt(int index) {
							return grid.elementAt(index);
						}

						@Override
						public int headerHeight() {
							return grid.rowHeight(0);
						}
					});
					sp.size(rows.size());
					sp.visible(true);
				}
			}
			preselectedList.clear();
			if (updateSelection)
				selection.updateButtons();
		} else {
			this.visible = false;
			throw new MethodNotImplementedException();
		}
		return this;
	}

	void setUpTopPanel() {
		if (navigationDecorator != null) {
			navigationPanel(navigationDecorator);
		}
		if (buttonDecorator != null) {
			buttonPanel(buttonDecorator);
		}
		if (commandButtons != null) {
			commandButtons.updateButtons();
		}
	}

	public int getRowHeight() {
		for (ScrollTableColumnImpl c : columns) {
			if (c.type.clazz.equals(Boolean.class)) {
				return ROW_HEIGHT + 4;
			}
		}
		return ROW_HEIGHT;
	}

	boolean hasFilter() {
		// if (rows.size() >= (Integer) FilterWidgetImpl.DEFAULT_SIZES.get(0))
		// return true;
		for (ScrollTableColumnImpl c : columns)
			if (c.filterable)
				return true;
		return false;
	}

	protected void addFilter() {
		filter = null;
		setUpFilter();
	}

	protected void setUpFilter() {
		if (viewComboBoxText != null) {
			createFilter();
			IFilter vl = filter.addFilter().name("View").required();
			IFieldType type = vl.type().text();
			for (String s : viewComboBoxText) {
				type.addConstraint(s);
			}
			vl.text(viewComboBoxChoice);
			vl.updateListener(viewComboBoxUpdateListener);
		}
		for (ScrollTableColumnImpl c : columns) {
			if (c.filterable) {
				createFilter();
				filter.addFilter().name(c.name).type(c.type);
			}
		}
		if (filter != null) {
			if (filterListener != null)
				filter.addFilterListener(filterListener);
			if (filterSizeConstraint)
				filter.addSizeFilter();
			if (constraints != null)
				filter.constraints(constraints);
			filter.visible(true);
		}
	}

	void createFilter() {
		if (filter == null) {
			filter = (IMiniFilterWidget) topPanelCell(0, 0).panel()
					.horizontal().addSpace(8).add()
					.widget(IMiniFilterWidget.class);
			filter.showConfiguration(showConfiguration);
			if (showConfiguration)
				filter.firstConfiguration(viewComboBoxChoice);
			// if (!ToolbarImpl.ALLOW_ALIGN_END_FOR_FLOW_PANEL)
			// topPanel.column(0).expand();
		}
	}

	protected int heightMinusTopPanel() {
		if (buttonDecorator == null && navigationDecorator == null
				&& (topPanel == null || topPanel.rows() == 0))
			return height;
		else
			return height - topPanel.height();
	}

	private void topPanel() {
		if (topPanel == null) {
			topPanel = container().add().panel().grid();
		}
	}

	IGridPanel statusPanel() {
		if (statusPanel == null) {
			statusPanel = container().add().panel().grid().resize(3, 1);
			statusPanel.spacing(4);
			IBorder border2 = statusPanel.border();
			border2.color().rgb(172, 197, 213);
			border2.style().top();
			statusPanel.color().rgb(249, 249, 249).gradient()
					.fallback(240, 240, 240).vertical().rgb(216, 216, 216);
		}
		return statusPanel;
	}

	@Override
	public IScrollTableColumn<Object> addColumn() {
		ScrollTableColumnImpl column = new ScrollTableColumnImpl(this,
				columns.size());
		columns.add(column);
		return column;
	}

	private boolean updating = false;
	private Map<ITableClickListener, KeyAdapter<Object>> listeners = new HashMap<ITableClickListener, KeyAdapter<Object>>();
	boolean selectionIsSetup = false;
	private ILazyScrollPane sp;
	private ISortListener sortListener;
	boolean addClickListeners = false;
	IFilterWidget filter;
	private boolean allowColumnSelection = true;
	private IFilterConstraints constraints;
	private IFilterListener filterListener;
	private IButtonPanelDecorator buttonDecorator;
	private INavigationPanelDecorator navigationDecorator;
	private CommandButtonsImpl commandButtons;
	List<Object> preselectedList = new LinkedList<Object>();
	int preselectedIndex = -1;
	private boolean showDisplayedRange = true;
	private boolean externalStatusPanel;
	private IVerticalPanel bottom;
	private int buttonColumn = 1;
	String[] viewComboBoxText;
	private IUpdateListener<String> viewComboBoxUpdateListener;
	private int viewInc;
	private String viewComboBoxChoice;
	private List<IRowIndexListener> scrollListeners = new LinkedList<IRowIndexListener>();
	private int rowIndex = -1;
	private boolean addToContextMenu;
	private IUpdateListener<List<String>> hiddenColumnListener;
	private boolean filterSizeConstraint = true;
	private boolean allowInsertUnder;
	private IDragDropListener dragDropListener;

	boolean update() {
		paintedRows = computeRowsToPaint();
		return updateWithPaintedRowsSet();
	}

	boolean updateWithPaintedRowsSet() {
		return updateWithPaintedRowsSet(false);
	}

	boolean updateWithPaintedRowsSet(boolean isCalibration) {
		if (updating)
			return false;
		updating = true;
		highlighted.clear();
		contentPanel.clear();
		IBulkTableWidget lastGrid = grid;
		IVerticalPanel vpanel = contentPanel.add().panel().vertical();
		grid = (IBulkTableWidget) vpanel.spacing(6).add()
				.widget(IBulkTableWidget.class);
		grid.addToContextMenu(addToContextMenu);
		final int heightMinusTopPanel = heightMinusTopPanel();
		grid.height(heightMinusTopPanel);
		for (IRowIndexListener rowIndexL : scrollListeners)
			rowIndexL.onScroll(rowOffset);
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
					sp.onUp(turns);
				}

				@Override
				public void onDown(int turns) {
					sp.onDown(turns);
				}
			});
		if (addClickListeners) {
			int current = 0;
			for (ScrollTableColumnImpl c : columns)
				if (!c.clickListeners.isEmpty() && c.visible)
					grid.labelMouseListener(current++, this);
		}
		grid.element().tooltip(tooltip);
		if (lastGrid != null)
			lastGrid.remove();
		for (int r = 0; r < paintedRows; r++) {
			if (rows.selected(r + rowOffset)) {
				IRow row = grid.row(r);
				row.highlight(true);
				highlighted.add(row);
			}
		}
		if (!isCalibration)
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
		updating = false;
		if (hiddenColumnListener != null) {
			List<String> hiddenColumns = new LinkedList<String>();
			for (ScrollTableColumnImpl c : columns)
				if (!c.visible)
					hiddenColumns.add(c.name);
			hiddenColumnListener.onUpdate(hiddenColumns);
		}
		int h1 = grid.height();
		int h2 = grid.tableHeight();
		return h1 < h2;
	}

	private void updateSingleContentRow(IBulkTableWidget grid,
			int visibleGridIndex, int contentIndex) {
		Object[] row = rows.row(contentIndex);
		int current = 0;
		assert columns.size() == row.length : "Wrong number of columns: "
				+ columns.size() + " vs " + row.length + " ("
				+ Arrays.toString(row) + ")";
		for (ScrollTableColumnImpl c : columns)
			if (c.visible) {
				c.decorate(rows.identifier(contentIndex),
						grid.cell(current++, visibleGridIndex), row[c.index]);
			}
	}

	@SuppressWarnings("unused")
	private void updateHeaderRow(IBulkTableWidget grid) {
		adjustColumnWidths();
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
				name += " " + (sortNegator == 1 ? ARROW_UP : ARROW_DOWN);
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
			if (columnImpl.alignment.isSpecified()) {
				columnImpl.alignment.forward(column.align());
			}
		}
	}

	private void adjustColumnWidths() {
		double sum = 0;
		for (int c = 0; c < columns.size(); c++) {
			if (!columns.get(c).visible)
				continue;
			ScrollTableColumnImpl columnImpl = columns.get(c);
			if (columnImpl.widthDouble != -1 || columnImpl.widthInt != -1) {
				return;
			}
			sum += columnImpl.defaultWidth();
		}
		for (int c = 0; c < columns.size(); c++) {
			if (!columns.get(c).visible)
				continue;
			ScrollTableColumnImpl columnImpl = columns.get(c);
			columnImpl.widthDouble /= sum;
		}
	}

	private void addDisplayingNote() {
		if (!showDisplayedRange)
			return;
		IGridPanel.IGridCell clear = statusPanel().cell(2, 0).clear().valign()
				.center();
		clear.align().end();
		IHorizontalPanel p = clear.panel().horizontal().align().end().add()
				.panel().horizontal().align().end();
		ILabel label = p.add().label();
		boolean hasPrevious = constraints != null
				&& constraints.rowIterator().hasPrevious();
		boolean hasNext = constraints != null
				&& constraints.rowIterator().hasNext();
		ILabel l = null;
		if (hasPrevious) {
			l = label.text("<<").hyperlink().clickable(false);
			p.addSpace(4);
		}
		if (hasPrevious) {
			l.clickable(true);
			l.hyperlink().font().pixel(10);
			l.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					int nextPreviousRow = constraints.rowIterator()
							.nextPreviousRow();
					constraints.rowIterator().firstRow(nextPreviousRow);
					filterListener.onApply(constraints,
							DummyCallback.voidInstance());
				}
			});
		}
		int rt = rowOffset + paintedRows;
		if (rt > rows.size())
			rt = rows.size();
		int firstRow = constraints != null ? constraints.rowIterator()
				.firstRow() : 0;
		String status = +(firstRow + rowOffset + 1) + " - " + (firstRow + rt);
		String in = "DISPLAYING ROWS";
		label = p.add().label();
		label.text(in);
		label.font().pixel(10);
		addStatus(p, status);
		if (hasNext) {
			p.addSpace(4);
			l = p.add().label().text(">>");
			l.hyperlink().clickable(false);
		}
		if (hasNext) {
			l.clickable(true);
			l.hyperlink().font().pixel(10);
			l.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					constraints.rowIterator().firstRow(
							constraints.rowIterator().nextFirstRow());
					filterListener.onApply(constraints,
							DummyCallback.voidInstance());
				}
			});
		}
		p.addSpace(4);
	}

	void addStatus(IHorizontalPanel p, String status) {
		p.addSpace(4).add().label().text(status).font().weight().bold()
				.pixel(10);
	}

	private int computeRowsToPaint() {
		int paintedRows = 0;
		int prognosedHeight = HEADER_ROW_HEIGHT;
		int heightMinusTopPanel = heightMinusTopPanel();
		while (prognosedHeight < heightMinusTopPanel) {
			prognosedHeight += getRowHeight();
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
					sortBy(columnImpl, true);
				}
			});
		}
	}

	private int realColumn(int column) {
		int visibleColumns = 0;
		int c = column;
		for (int i = 0; visibleColumns <= column; i++)
			if (!columns.get(i).visible)
				c++;
			else
				visibleColumns++;
		return c;
	}

	@Override
	public ILabel addTitle(String text) {
		addBorders = true;
		return widgetTitle().addTitle(text);
	}

	@Override
	public IClickable<?> addButton(String name) {
		return widgetTitle().addHyperlink(name);
	}

	@Override
	public IClickable<?> addButton(String name, String imageResource) {
		return widgetTitle().addHyperlink(imageResource, name);
	}

	@Override
	public IClickable<?> addButton(String name, String imageResource,
			String toolTipClickable, String toolTipNotClickable) {
		return widgetTitle().addHyperlink(imageResource, name,
				toolTipClickable, toolTipNotClickable);
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
	public IKey<?> addTableClickListener(final IScrollTableClickListener l) {
		ITableClickListener l2 = new ITableClickListener() {

			@Override
			public void onClick(int column, int row) {
				int convert2TableRow = convert2TableRow(row);
				if (convert2TableRow == 0) {
					return;
				}
				int rowIndex = convert2TableRow - 1;
				if (!rows.selected(rowIndex)) {
					rows.selected(rowIndex, true);
					selection.notifySelection(rowIndex,
							rows.identifier(rowIndex));
				}
				l.onClick(rows.identifier(rowIndex), rowIndex);
			}
		};
		KeyAdapter<Object> keyAdapter = new KeyAdapter<Object>();
		listeners.put(l2, keyAdapter);
		return keyAdapter;
	}

	@Override
	public IScrollTableWidget<Object> addTooltip(String tooltip) {
		// TODO Swing tooltip doesn't work for BulkTableWidgetImpl
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
			public void onApply(IFilterConstraints constraints,
					ICallback<Void> cb) {
				ScrollTableWidgetImpl.this.constraints = constraints;
				l.onApply(constraints, cb);
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
			filter.apply(DummyCallback.voidInstance());
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
	public IScrollTableWidget<Object> navigationPanel(
			INavigationPanelDecorator dec) {
		if (topPanel != null) {
			int column = viewInc + buttonColumn++;
			dec.decorate(alignEnd(topPanelCell(column, 0), column));
		} else
			navigationDecorator = dec;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> buttonPanel(IButtonPanelDecorator dec) {
		if (topPanel != null) {
			int column = viewInc + buttonColumn;
			IGridCell cell = topPanelCell(column, 0);
			if (navigationDecorator != null)
				cell.width(100);
			dec.decorate(alignEnd(cell, column));
		} else
			buttonDecorator = dec;
		return this;
	}

	private IGridCell alignEnd(IGridCell cell, int column) {
		if (ToolbarImpl.ALLOW_ALIGN_END_FOR_FLOW_PANEL) {
			cell.align().end();

			// TODO align end for Chrome <= 13, sub-optimal design with icons in
			// the middle

		}
		return cell;
	}

	IGridCell topPanelCell(int i, int j) {
		return topPanel.cell(i, j);
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
	public co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons<Object> commandButtons() {
		if (commandButtons == null) {
			commandButtons = new CommandButtonsImpl(this);
			buttonDecorator = commandButtons;
		}
		return commandButtons;
	}

	void notifySelection(int selectionIndex, Object selection2) {
		this.selection.notifySelection(selectionIndex, selection2);
	}

	@Override
	public IScrollTableWidget<Object> showDisplayedRange(
			boolean showDisplayedRange) {
		this.showDisplayedRange = showDisplayedRange;
		return this;
	}

	@Override
	public boolean visible() {
		return container().visible();
	}

	@Override
	public IScrollTableWidget<Object> statusPanel(IVerticalPanel bottom) {
		this.bottom = bottom;
		externalStatusPanel = true;
		resetStatusPanel();
		return this;
	}

	private void resetStatusPanel() {
		bottom.clear();
		bottom.addSpace(2);
		statusPanel = bottom.add().panel().grid().resize(3, 1);
		statusPanel.spacing(4);
	}

	void visible(IRows<Object> result) {
		if (result != null)
			rows(result);
		visible(true);
	}

	@Override
	public IScrollTableWidget<Object> addViewComboBox(String[] texts,
			String viewComboBoxChoice, IUpdateListener<String> ul) {
		viewComboBoxText = texts;
		this.viewComboBoxChoice = viewComboBoxChoice;
		viewComboBoxUpdateListener = ul;
		showNoRowsFound = false;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> addScrollListener(
			co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowIndexListener scrollListener) {
		scrollListeners.add(scrollListener);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> rowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> addToContextMenu(boolean addToContextMenu) {
		this.addToContextMenu = addToContextMenu;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> hiddenColumnListener(
			IUpdateListener<List<String>> hiddenColumnListener) {
		this.hiddenColumnListener = hiddenColumnListener;
		return this;
	}

	void sortBy(ScrollTableColumnImpl columnImpl, boolean update) {
		if (columnImpl.sortable) {
			if (rows.size() < MAX_SORT_SIZE || sortListener == null) {
				sortColumn = columnImpl.index;
				sortNegator = rows.sort(columnImpl);
				if (sortListener != null)
					sortListener.onSort(columnImpl.name, sortNegator == 1,
							false);
				if (update)
					update();
			} else {
				if (sortColumn != -1) {
					sortNegator = sortColumn == columnImpl.index ? sortNegator
							* -1 : 1;
				}
				sortColumn = columnImpl.index;
				sortListener.onSort(columnImpl.name, sortNegator == 1, true);
			}
		}
	}

	@Override
	public IFilterConstraints constraints() {
		if (filter != null)
			return filter.constraints();
		return null;
	}

	@Override
	public IScrollTableWidget<Object> filterSizeConstraint(
			boolean filterSizeConstraint) {
		this.filterSizeConstraint = filterSizeConstraint;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> showConfiguration(
			boolean showConfiguration) {
		this.showConfiguration = showConfiguration;
		return this;
	}

	@Override
	public List<IScrollTableColumn<Object>> columns() {
		List<IScrollTableColumn<Object>> columns = new LinkedList<IScrollTableColumn<Object>>();
		columns.addAll(this.columns);
		return columns;
	}

	@Override
	public IScrollTableWidget<Object> dragDropListener(
			boolean allowInsertUnder, IDragDropListener l) {
		this.allowInsertUnder = allowInsertUnder;
		this.dragDropListener = l;
		return this;
	}
}
