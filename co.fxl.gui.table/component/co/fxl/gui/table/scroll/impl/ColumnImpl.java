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

import java.util.Comparator;

import co.fxl.gui.table.api.IColumn;

class ColumnImpl implements IColumn, Comparator<Object[]> {

	String name;
	boolean sortable = false;
	Class<?> type = String.class;
	int index;

	ColumnImpl(int index) {
		this.index = index;
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

	@Override
	public IColumn type(Class<?> type) {
		this.type = type;
		return this;
	}

	@Override
	public IColumn width(int width) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColumn decorator(IDecorator<?, ?> decorator) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColumn updateListener(IColumnUpdateListener<?, ?> updateListener) {
		throw new MethodNotImplementedException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object[] row1, Object[] row2) {
		Comparable<Object> o1 = (Comparable<Object>) row1[index];
		Comparable<Object> o2 = (Comparable<Object>) row2[index];
		if (o1 == null)
			if (o2 == null)
				return 0;
			else
				return -1;
		else if (o2 == null)
			return 1;
		return o1.compareTo(o2);
	}
}
