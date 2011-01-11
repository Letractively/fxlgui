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
import java.util.Date;

import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.template.SimpleDateFormat;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ICell;

class ColumnImpl implements IColumn<Object>, Comparator<Object[]> {

	public class BooleanDecorator implements Decorator<Boolean> {

		@Override
		public void decorate(final Object identifier, ICell cell, Boolean value) {
			cell.checkBox(value);
			if (updateListener != null)
				cell.updateListener(new IUpdateListener<Boolean>() {
					@Override
					public void onUpdate(Boolean value) {
						updateListener.onUpdate(identifier, value);
					}
				});
		}
	}

	private static SimpleDateFormat FORMAT = new SimpleDateFormat();

	public class DateDecorator implements Decorator<Date> {

		@Override
		public void decorate(Object identifier, ICell cell, Date value) {
			cell.text(FORMAT.format(value));
		}

	}

	public class NumberDecorator implements Decorator<Number> {

		@Override
		public void decorate(Object identifier, ICell cell, Number value) {
			cell.text(String.valueOf(value));
		}

	}

	public interface Decorator<T> {

		void decorate(Object identifier, ICell cell, T value);
	}

	public class StringDecorator implements Decorator<String> {

		@Override
		public void decorate(Object identifier, ICell cell, String value) {
			cell.text((String) value);
		}
	}

	String name;
	boolean sortable = false;
	Class<?> type = String.class;
	int index;
	@SuppressWarnings("rawtypes")
	private Decorator decorator;
	private IColumnUpdateListener<Object, Object> updateListener;

	ColumnImpl(int index) {
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	void decorate(Object identifier, ICell cell, Object value) {
		decorator.decorate(identifier, cell, value);
	}

	@Override
	public IColumn<Object> name(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IColumn<Object> sortable() {
		sortable = true;
		return this;
	}

	@Override
	public IColumn<Object> type(Class<?> type) {
		if (type.equals(String.class)) {
			decorator = new StringDecorator();
		} else if (type.equals(Long.class) || type.equals(Integer.class)) {
			decorator = new NumberDecorator();
		} else if (type.equals(Date.class)) {
			decorator = new DateDecorator();
		} else if (type.equals(Boolean.class)) {
			decorator = new BooleanDecorator();
		} else
			throw new MethodNotImplementedException(type.getName());
		return this;
	}

	@Override
	public IColumn<Object> width(int width) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColumn<Object> decorator(IDecorator<?, ?> decorator) {
		throw new MethodNotImplementedException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IColumn<Object> updateListener(
			IColumnUpdateListener<Object, ?> updateListener) {
		this.updateListener = (co.fxl.gui.table.api.IColumn.IColumnUpdateListener<Object, Object>) updateListener;
		return this;
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
