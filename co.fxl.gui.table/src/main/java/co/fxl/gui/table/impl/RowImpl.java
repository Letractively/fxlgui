/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import java.util.Arrays;

import co.fxl.gui.table.api.IRow;

public class RowImpl implements IRow<Object> {

	static final Comparable<?> EMPTY = null;

	public class Content {

		int index;
		public boolean selected = false;
		public boolean visible = true;
		public Object[] values = new Object[table.columns.size()];
		Object identifier;
		int currentColumn = 0;
		Cell<?>[] cells = new Cell<?>[table.columns.size()];
	}

	private TableWidgetImpl table;
	int rowIndex;
	public Content content;

	protected RowImpl(TableWidgetImpl table, int rowIndex) {
		this.table = table;
		this.rowIndex = rowIndex;
		content = new Content();
		content.index = rowIndex;
		content.identifier = rowIndex;
	}

	@Override
	public IRow<Object> add(Object... content) {
		for (Object comparable : content) {
			if (comparable == null || comparable.equals(""))
				comparable = EMPTY;
			this.content.values[this.content.currentColumn++] = comparable;
		}
		return this;
	}

	@Override
	public IRow<Object> set(Object[] values) {
		content.values = values;
		return this;
	}

	void swap(RowImpl rowImpl) {
		Content tmp = content;
		content = rowImpl.content;
		rowImpl.content = tmp;
	}

	@SuppressWarnings("unchecked")
	public void update() {
		for (content.currentColumn = 0; content.currentColumn < content.values.length; content.currentColumn++) {
			Object value = content.values[content.currentColumn];
			Cell<Object> cell = (Cell<Object>) content.cells[content.currentColumn];
			cell.update(value);
			cell.highlight(rowIndex, content.selected);
			ColumnImpl column = table.columns.get(content.currentColumn);
			if (column.decorator != null && !content.selected)
				column.decorator.decorate(cell.element, value);
			cell.visible(content.visible);
		}
	}

	@Override
	public String toString() {
		return Arrays.toString(content.values);
	}

	@Override
	public IRow<Object> identifier(Object identifier) {
		content.identifier = identifier;
		table.object2row.put(identifier, this);
		return this;
	}

	void selected(boolean selected) {
		content.selected = selected;
		for (int i = 0; i < content.values.length; i++) {
			content.cells[i].highlight(rowIndex, content.selected);
		}
	}

	@Override
	public IRow<Object> select() {
		selected(true);
		table.selection.add(this, content.identifier);
		return this;
	}

	boolean selected() {
		return content.selected;
	}
}
