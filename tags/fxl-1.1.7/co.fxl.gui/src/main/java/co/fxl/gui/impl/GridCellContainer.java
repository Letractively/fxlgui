/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

import java.util.HashMap;
import java.util.Map;

public class GridCellContainer<T> extends HashMap<Integer, Map<Integer, T>> {

	private static final long serialVersionUID = 5133726159517072973L;

	public T getCell(int columnIndex, int rowIndex) {
		Map<Integer, T> row = get(rowIndex);
		if (row == null)
			return null;
		return row.get(columnIndex);
	}

	public void putCell(int columnIndex, int rowIndex, T gridCell) {
		Map<Integer, T> row = get(rowIndex);
		if (row == null) {
			row = new HashMap<Integer, T>();
			put(rowIndex, row);
		}
		row.put(columnIndex, gridCell);
	}

	public void removeRow(int rowIndex) {
		Map<Integer, T> row = get(rowIndex);
		if (row != null) {
			for (int i = 0; i < row.size(); i++) {
				handleRemovedCell(row.get(i));
			}
			remove(rowIndex);
		}
		int rows = size();
		for (int r = rowIndex + 1; r < rows; r++) {
			int columns = columns(r);
			for (int c = 0; c < columns; c++) {
				T cell = getCell(c, r);
				moveCellDown(cell, -1);
			}
			row = remove(r);
			put(r - 1, row);
		}
	}

	private int columns(int row) {
		Map<Integer, T> map = get(row);
		if (map == null)
			return 0;
		return map.size();
	}

	public void insertRow(int rowIndex) {
		int rows = size();
		for (int r = rows - 1; r >= rowIndex; r--) {
			int columns = columns(r);
			for (int c = 0; c < columns; c++) {
				T cell = getCell(c, r);
				moveCellDown(cell, 1);
			}
			Map<Integer, T> row = remove(r);
			put(r + 1, row);
		}
	}

	protected void moveCellDown(T cell, int i) {
	}

	protected void handleRemovedCell(T t) {
	}

	public void removeCell(int rowIndex, int columnIndex) {
		Map<Integer, T> map = get(rowIndex);
		if (map == null)
			return;
		map.remove(columnIndex);
		if (map.isEmpty())
			remove(rowIndex);
	}
}