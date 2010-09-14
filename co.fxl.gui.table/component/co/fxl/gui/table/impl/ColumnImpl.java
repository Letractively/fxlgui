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

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.table.api.IColumn;

public class ColumnImpl implements IColumn {

	private class ClickListener implements IClickListener {

		@Override
		public void onClick() {
			RowComparator rc = new RowComparator(table.comparator,
					ColumnImpl.this);
			table.comparator = rc;
			addSortingSymbol(rc.sortUp);
			table.updateRowPresentation();
		}
	}

	private static final String ARROW_UP = "\u2191";
	private static final String ARROW_DOWN = "\u2193";
	private TableWidgetImpl table;
	public int columnIndex;
	private ILabel headerButton;
	public String name;
	private IGridCell headerButtonCell;
	IDecorator<Object, Object> decorator;
	public Class<?> contentType;
	private boolean sortable = false;

	ColumnImpl(TableWidgetImpl table, int columnIndex) {
		this.table = table;
		this.columnIndex = columnIndex;
	}

	void visible() {
		IGridPanel gridPanel = table.gridPanel;
		headerButtonCell = gridPanel.cell(columnIndex, 0);
		headerButton = headerButtonCell.label();
		IBorder border = headerButtonCell.border();
		border.width(2);
		border.style().bottom();
		headerButton.font().weight().bold().pixel(14);
		headerButton.text(name);
		if (sortable)
			headerButton.addClickListener(new ClickListener());
	}

	@Override
	public IColumn name(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IColumn sortable() {
		sortable = true;
		return this;
	}

	private void addSortingSymbol(boolean sortUp) {
		for (ColumnImpl column : table.columns) {
			if (column != this)
				column.removeSortingSymbol();
		}
		String buttonName = name + " " + (sortUp ? ARROW_DOWN : ARROW_UP);
		headerButton.text(buttonName);
	}

	void removeSortingSymbol() {
		headerButton.text(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IColumn decorator(IDecorator<?, ?> decorator) {
		this.decorator = (IDecorator<Object, Object>) decorator;
		return this;
	}

	@Override
	public IColumn type(Class<?> type) {
		contentType = type;
		return this;
	}
}
