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
package co.fxl.gui.table.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.DynamicHeightGrid;
import co.fxl.gui.api.template.IScrollGrid;
import co.fxl.gui.api.template.IScrollGrid.ILazyGridDecorator;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.api.ITableWidget;
import co.fxl.gui.table.impl.sort.IComparableList;
import co.fxl.gui.table.impl.sort.ISort;
import co.fxl.gui.table.impl.sort.QuickSort;

public class TableWidgetImpl implements ITableWidget<Object>,
		ILazyGridDecorator {

	// TODO show status line: showing rows 27-40 of 3000

	// TODO nice-2-have: shift-click: no text marking

	private class ComparableList implements IComparableList {

		@Override
		public int compare(int firstIndex, int secondIndex) {
			RowImpl row1 = rows.get(firstIndex);
			RowImpl row2 = rows.get(secondIndex);
			if (!row1.content.visible)
				return 1;
			else if (!row2.content.visible)
				return -1;
			return comparator.compare(row1, row2);
		}

		@Override
		public int size() {
			return rows.size();
		}

		@Override
		public void swap(int firstIndex, int secondIndex) {
			RowImpl row1 = rows.get(firstIndex);
			RowImpl row2 = rows.get(secondIndex);
			row1.swap(row2);
		}
	}

	private WidgetTitle widgetTitle;
	protected IVerticalPanel mainPanel;
	protected IScrollGrid gridPanel;
	private IHorizontalPanel headerPanel;
	protected IGridPanel selectionPanel = null;
	public List<ColumnImpl> columns = new LinkedList<ColumnImpl>();
	protected List<RowImpl> rows = new LinkedList<RowImpl>();
	SelectionImpl selection;
	protected RowListener rowListener;
	Comparator<RowImpl> comparator = new Comparator<RowImpl>() {
		@Override
		public int compare(RowImpl arg0, RowImpl arg1) {
			return arg0.content.index - arg1.content.index;
		}
	};
	protected boolean init = false;
	private boolean addedSpace = false;
	Map<Object, RowImpl> object2row = new HashMap<Object, RowImpl>();
	private IRows<Object> source;

	protected TableWidgetImpl(ILayout layout) {
		widgetTitle = new WidgetTitle(layout);
		widgetTitle.foldable(false);
	}

	public void init() {
		if (init)
			return;
		if (mainPanel == null) {
			mainPanel = widgetTitle.content().panel().vertical().stretch(true);
		}
		init = true;
		mainPanel.visible(false);
		mainPanel.clear();
		headerPanel = mainPanel.add().panel().horizontal().add().panel()
				.horizontal();
		headerPanel.align().begin();
		gridPanel = new DynamicHeightGrid(mainPanel.add());
		gridPanel.spacing(0);
		gridPanel.indent(3);
		gridPanel.decorator(this);
		mainPanel.addSpace(6);
		if (rowListener != null) {
			rowListener.visible();
			gridPanel.addGridClickListener(new IGridClickListener() {
				@Override
				public void onClick(int column, int row) {
					if (row > 0)
						rowListener.notifyClick(rows.get(row - 1));
				}
			}, null);
			gridPanel.addGridClickListener(new IGridClickListener() {
				@Override
				public void onClick(int column, int row) {
					if (row > 0)
						rowListener.notifyCtrlClick(rows.get(row - 1));
				}
			}, IScrollGrid.CTRL);
			gridPanel.addGridClickListener(new IGridClickListener() {
				@Override
				public void onClick(int column, int row) {
					if (row > 0)
						rowListener.notifyShiftClick(rows.get(row - 1));
				}
			}, IScrollGrid.SHIFT);
		}
	}

	@Override
	public ILabel addTitle(String text) {
		ILabel title = widgetTitle.addTitle(text);
		return title;
	}

	@Override
	public IColumn<Object> addColumn() {
		ColumnImpl column = new ColumnImpl(this, columns.size());
		columns.add(column);
		return column;
	}

	protected void updateRowPresentation() {
		ISort sort = new QuickSort();
		sort.sort(new ComparableList());
		for (RowImpl row : rows)
			row.update();
	}

	@Override
	public ISelection<Object> selection() {
		if (selection == null) {
			selection = new SelectionImpl(this);
		}
		return selection;
	}

	@Override
	public RowImpl addRow() {
		init();
		if (rowListener != null)
			rowListener.update();
		RowImpl row = newRowImpl(this, rows.size() + 1);
		rows.add(row);
		return row;
	}

	protected RowImpl newRowImpl(TableWidgetImpl tableWidgetImpl, int i) {
		return new RowImpl(tableWidgetImpl, i);
	}

	public IGridPanel selectionPanel() {
		if (selectionPanel == null) {
			init();
			selectionPanel = mainPanel.add().panel().grid();
		}
		return selectionPanel;
	}

	@Override
	public TableWidgetImpl visible(boolean visible) {
		init();
		int rowCount = rows.size() + 1;
		if (source != null)
			rowCount = source.size();
		gridPanel.rows(rowCount);
		gridPanel.columns(columns.size());
		gridPanel.visible(true);
		mainPanel.visible(visible);
		if (!addedSpace)
			mainPanel.addSpace(12);
		addedSpace = true;
		return this;
	}

	@Override
	public IClickable<?> addButton(String name) {
		return widgetTitle.addHyperlink(name);
	}

	@Override
	public void decorate(int column, int row, IGridCell cell) {
		if (row == 0) {
			columns.get(column).visible(cell);
		} else {
			cell.valign().center();
			while (rows.size() <= row) {
				addRow();
			}
			RowImpl rowImpl = rows.get(row - 1);
			if (rowImpl.content.values == null) {
				rowImpl.set(source.row(row - 1));
				rowImpl.identifier(source.identifier(row - 1));
			}
			Object comparable = rowImpl.content.values[column];
			Cell<?> c = CellFactory.createCellContent(this, rowImpl, column,
					cell, comparable);
			if (rowImpl.cells == null)
				rowImpl.cells = new LinkedList<Cell<?>>();
			if (rowImpl.cells.size() == column) {
				rowImpl.cells.add(c);
			} else
				throw new MethodNotImplementedException();
			ColumnImpl ccolumn = columns.get(column);
			if (ccolumn.decorator != null)
				ccolumn.decorator.decorate(c.element, comparable);
		}
	}

	@Override
	public int offsetY() {
		return gridPanel.offsetY();
	}

	@Override
	public ITableWidget<Object> height(int height) {
		gridPanel.height(height);
		return this;
	}

	@Override
	public ITableWidget<Object> source(IRows<Object> rows) {
		this.source = rows;
		return this;
	}

	@Override
	public void decorate(IGridPanel grid) {
	}
}
