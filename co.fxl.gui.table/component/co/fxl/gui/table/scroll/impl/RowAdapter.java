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

import co.fxl.gui.table.impl.sort.IComparableList;
import co.fxl.gui.table.impl.sort.QuickSort;
import co.fxl.gui.table.scroll.api.IRows;

class RowAdapter implements IRows<Object>, IComparableList {

	private QuickSort quickSort = new QuickSort();
	private IRows<Object> rows;
	private int[] indices;
	private ScrollTableColumnImpl comparator;
	private int negator = 1;
	private boolean[] selected;

	RowAdapter(IRows<Object> rows) {
		this.rows = rows;
		indices = new int[rows.size()];
		selected = new boolean[rows.size()];
		for (int i = 0; i < indices.length; i++)
			indices[i] = i;
	}

	@Override
	public Object identifier(int i) {
		return rows.identifier(indices[i]);
	}

	boolean selected(int i) {
		return selected[indices[i]];
	}

	void selected(int i, boolean b) {
		selected[indices[i]] = b;
	}

	void selected(Object o, boolean b) {
		for (int i = 0; i < rows.size(); i++) {
			if (o.equals(identifier(i))) {
				selected(i, b);
			}
		}
	}

	int selected(Object object) {
		for (int i = 0; i < rows.size(); i++) {
			if (identifier(i).equals(object)) {
				selected(i, true);
				return i;
			}
		}
		return -1;
	}

	void clearSelection() {
		selected = new boolean[rows.size()];
	}

	@Override
	public Object[] row(int i) {
		return rows.row(indices[i]);
	}

	@Override
	public int size() {
		return indices.length;
	}

	int sort(ScrollTableColumnImpl column) {
		if (comparator != null) {
			negator = comparator == column ? negator * -1 : 1;
		}
		comparator = column;
		quickSort.sort(this);
		return negator;
	}

	@Override
	public int compare(int firstIndex, int secondIndex) {
		return negator * comparator.compare(row(firstIndex), row(secondIndex));
	}

	@Override
	public void swap(int firstIndex, int secondIndex) {
		int tmp = indices[firstIndex];
		indices[firstIndex] = indices[secondIndex];
		indices[secondIndex] = tmp;
	}

	List<Object> selectedIdentifiers() {
		List<Object> ids = new LinkedList<Object>();
		for (int i = 0; i < size(); i++) {
			if (selected[i])
				ids.add(rows.identifier(i));
		}
		return ids;
	}
}