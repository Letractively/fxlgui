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
package co.fxl.gui.table.scroll.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.table.impl.sort.IComparableList;
import co.fxl.gui.table.impl.sort.QuickSort;
import co.fxl.gui.table.scroll.api.IRows;

class RowAdapter implements IRows<Object>, IComparableList {

	private QuickSort quickSort = new QuickSort();
	IRows<Object> rows;
	private int[] indices;
	private boolean[] selected;
	private ScrollTableWidgetImpl widget;
	Map<Integer, String> cached = new HashMap<Integer, String>();

	RowAdapter(ScrollTableWidgetImpl widget, IRows<Object> rows) {
		this.widget = widget;
		this.rows = rows;
		indices = new int[rows.size()];
		selected = new boolean[rows.size()];
		for (int i = 0; i < indices.length; i++)
			indices[i] = i;
	}

	@Override
	public Object identifier(int i) {
		assert i != -1;
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
			Object identifier = identifier(i);
			if (object.equals(identifier)) {
				selected(i, true);
				return i;
			}
		}
		return -1;
	}

	void selected(List<Object> object) {
		for (Object o : object)
			selected(o);
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
		if (widget.sortColumn() != null) {
			widget.sortNegator = widget.sortColumn() == column ? widget.sortNegator
					* -1
					: 1;
		}
		sort();
		return widget.sortNegator;
	}

	void sort() {
		cached.clear();
		quickSort.sort(this);
	}

	@Override
	public int compare(int firstIndex, int secondIndex) {
		ScrollTableColumnImpl sortColumn = widget.sortColumn();
		Object[] l = row(firstIndex);
		Object[] r = row(secondIndex);
		return widget.sortNegator * sortColumn.compare(l, r);
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

	List<Integer> selectedIndices() {
		List<Integer> ids = new LinkedList<Integer>();
		for (int i = 0; i < size(); i++) {
			if (selected[i])
				ids.add(i);
		}
		return ids;
	}

	int find(Object object) {
		for (int i = 0; i < rows.size(); i++) {
			if (identifier(i).equals(object))
				return i;
		}
		return -1;
	}

	int find(List<Object> object) {
		for (int i = 0; i < rows.size(); i++) {
			if (object.contains(identifier(i)))
				return i;
		}
		return -1;
	}

	boolean selected(int preselectedIndex, Object preselected) {
		if (preselectedIndex < selected.length) {
			if (identifier(preselectedIndex).equals(preselected)) {
				selected[preselectedIndex] = true;
				return true;
			} else
				return false;
		} else
			return false;
	}

	@Override
	public boolean deletable(int i) {
		int index = indices[i];
		return rows.deletable(index);
	}
}