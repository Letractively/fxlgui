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

class RowComparator implements Comparator<RowImpl> {

	boolean sortUp = true;
	private ColumnImpl column;
	private int multiplicator;

	RowComparator(Comparator<RowImpl> comparator, ColumnImpl columnImpl) {
		column = columnImpl;
		if (comparator instanceof RowComparator) {
			RowComparator rowComparator = (RowComparator) comparator;
			if (rowComparator.column == column) {
				sortUp = !rowComparator.sortUp;
			}
		}
		multiplicator = sortUp ? 1 : -1;
	}

	@Override
	public int compare(RowImpl o1, RowImpl o2) {
		Comparable<Object> c1 = (Comparable<Object>) o1.content.values
				.get(column.columnIndex);
		Comparable<Object> c2 = (Comparable<Object>) o2.content.values
				.get(column.columnIndex);
		if (c1 == null)
			return 1;
		else if (c2 == null)
			return -1;
		return multiplicator * c1.compareTo(c2);
	}
}
