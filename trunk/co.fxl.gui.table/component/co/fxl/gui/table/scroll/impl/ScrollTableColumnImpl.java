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

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.template.AlignmentMemento;
import co.fxl.gui.api.template.AlignmentMemento.Type;
import co.fxl.gui.api.template.DateFormat;
import co.fxl.gui.api.template.FieldTypeImpl;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ICell;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IUpdateAdapter;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;

class ScrollTableColumnImpl implements IScrollTableColumn<Object>,
		Comparator<Object[]> {

	public class BooleanDecorator implements Decorator<Boolean> {

		@Override
		public void decorate(final Object identifier, ICell cell, Boolean value) {
			cell.checkBox(value);
			if (value != null)
				if (updateListener != null) {
					cell.updateListener(new IUpdateListener<Boolean>() {
						@Override
						public void onUpdate(Boolean value) {
							updateListener.onUpdate(identifier, value);
						}
					});
					cell.updateAdapter(new IUpdateAdapter<Boolean>() {

						@Override
						public boolean isEditable() {
							return updateListener.isEditable(identifier);
						}
					});
				}
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
			column.align().center();
		}
	}

	public class DateDecorator implements Decorator<Date> {

		@Override
		public void decorate(Object identifier, ICell cell, Date value) {
			cell.text(value == null ? null : FORMAT.format(value));
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
			if (index == 0)
				column.align().begin();
			else
				column.align().center();
		}
	}

	public class NumberDecorator implements Decorator<Number> {

		@Override
		public void decorate(Object identifier, ICell cell, Number value) {
			cell.text(String.valueOf(value));
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
			column.align().center();
		}
	}

	public interface Decorator<T> {

		void decorate(Object identifier, ICell cell, T value);

		void prepare(IBulkTableWidget.IColumn column);
	}

	public class StringDecorator implements Decorator<String> {

		@Override
		public void decorate(Object identifier, ICell cell, String value) {
			cell.text((String) value);
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
		}
	}

	private static DateFormat FORMAT = DateFormat.instance;
	String name;
	boolean sortable = false;
	FieldTypeImpl type = new FieldTypeImpl();
	int index;
	@SuppressWarnings("rawtypes")
	private Decorator decorator;
	private IColumnUpdateListener<Object, Object> updateListener;
	Boolean tagSortOrder;
	private ScrollTableWidgetImpl widget;
	List<IScrollTableListener<Object>> clickListeners = new LinkedList<IScrollTableListener<Object>>();
	boolean filterable;
	private AlignmentMemento<IScrollTableColumn<Object>> alignment = new AlignmentMemento<IScrollTableColumn<Object>>(
			this);
	boolean visible = true;
	int widthInt = -1;
	double widthDouble = -1;
	boolean editable = false;

	ScrollTableColumnImpl(ScrollTableWidgetImpl widget, int index) {
		this.widget = widget;
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	void decorate(Object identifier, ICell cell, Object value) {
		try {
			decorator().decorate(identifier, cell, value);
		} catch (ClassCastException e) {
			throw new RuntimeException("Column " + name + ", index: " + index
					+ ", received invalid value " + value + " ("
					+ value.getClass().getName() + ")");
		}
		if (!alignment.type.equals(Type.BEGIN))
			throw new MethodNotImplementedException();
	}

	@SuppressWarnings("rawtypes")
	Decorator decorator() {
		if (decorator == null) {
			if (type.clazz.equals(String.class)) {
				decorator = new StringDecorator();
			} else if (type.clazz.equals(Long.class)
					|| type.clazz.equals(Integer.class)) {
				decorator = new NumberDecorator();
			} else if (type.clazz.equals(Date.class)) {
				decorator = new DateDecorator();
			} else if (type.clazz.equals(Timestamp.class)) {
				decorator = new DateDecorator();
			} else if (type.clazz.equals(Boolean.class)) {
				decorator = new BooleanDecorator();
			} else
				throw new MethodNotImplementedException(type.clazz.getName());
		}
		return decorator;
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
	public IFieldType type() {
		return type;
	}

	@Override
	public IColumn<Object> width(int width) {
		this.widthInt = width;
		return this;
	}

	@Override
	public IColumn<Object> width(double width) {
		this.widthDouble = width;
		return this;
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

	@Override
	public IColumn<Object> tagSortOrder(boolean up) {
		tagSortOrder = up;
		return this;
	}

	@Override
	public IScrollTableColumn<Object> addClickListener(
			IScrollTableListener<Object> l) {
		widget.addClickListeners = true;
		clickListeners.add(l);
		return this;
	}

	@Override
	public IScrollTableColumn<Object> filterable() {
		filterable = true;
		return this;
	}

	@Override
	public IColumn<Object> type(IFieldType type) {
		this.type = (FieldTypeImpl) type;
		return this;
	}

	@Override
	public IAlignment<IScrollTableColumn<Object>> align() {
		return alignment;
	}

	@Override
	public IScrollTableColumn<Object> editable() {
		editable = true;
		return this;
	}
}
