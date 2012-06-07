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

import co.fxl.data.format.api.IFormat;
import co.fxl.data.format.impl.Format;
import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.impl.AlignmentMemento;
import co.fxl.gui.impl.AlignmentMemento.Type;
import co.fxl.gui.impl.FieldTypeImpl;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IUpdateAdapter;
import co.fxl.gui.table.bulk.api.IBulkTableCell;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;

class ScrollTableColumnImpl implements IScrollTableColumn<Object>,
		Comparator<Object[]> {

	private static final double WEIGHT_BOOLEAN = 1;
	private static final double WEIGHT_DATE = 1;
	private static final double WEIGHT_LONG_DATE = 2;
	private static final double WEIGHT_NUMBER = 1;
	private static final double WEIGHT_CUSTOM_LIST_SELECTION = 1;
	private static final double WEIGHT_IMAGE = 1;
	private static final double WEIGHT_TEXT = 2;
	private static final double WEIGHT_HTML = 2;
	private static final double WEIGHT_LONG_TEXT = 3;

	public class BooleanDecorator implements Decorator<Boolean> {

		@Override
		public void decorate(final Object identifier, final IBulkTableCell cell,
				Boolean value) {
			cell.checkBox(value);
			if (value != null && updateListener != null
					&& updateListener.isEditable(identifier)) {
				cell.updateListener(new IUpdateListener<Boolean>() {

					private boolean ignore = false;

					@Override
					public void onUpdate(Boolean value) {
						if (ignore)
							return;
						if (!updateListener.isEditable(identifier)) {
							ignore = true;
							cell.checkBox(!value);
							ignore = false;
							return;
						}
						boolean refresh = updateListener.onUpdate(identifier,
								value);
						if (refresh) {
							widget.refresh();
						}
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
			if (alignment.isSpecified()) {
				alignment.forward(column.align());
				return;
			}
			if (index == 0)
				column.align().begin();
			else
				column.align().center();
		}

		@Override
		public double defaultWeight() {
			return WEIGHT_BOOLEAN;
		}
	}

	class DateDecorator implements Decorator<Date> {

		private IFormat<Date> format;
		private double weight = WEIGHT_DATE;

		DateDecorator(boolean isLong, boolean isShort) {
			if (isLong) {
				format = Format.dateTime();
				weight = WEIGHT_LONG_DATE;
			} else if (isShort)
				format = Format.time();
			else
				format = Format.date();
		}

		@Override
		public void decorate(Object identifier, IBulkTableCell cell, Date value) {
			String text = value == null ? null : format.format(value);
			// TODO injectColor(identifier, cell);
			cell.text(text);
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
			if (alignment.isSpecified()) {
				alignment.forward(column.align());
				return;
			}
			if (index == 0)
				column.align().begin();
			else
				column.align().center();
		}

		@Override
		public double defaultWeight() {
			return weight;
		}
	}

	public class NumberDecorator implements Decorator<Number> {

		@Override
		public void decorate(Object identifier, IBulkTableCell cell, Number value) {
			String text = String.valueOf(value);
			// TODO injectColor(identifier, cell);
			cell.text(text);
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
			if (alignment.isSpecified()) {
				alignment.forward(column.align());
				return;
			}
			if (index == 0)
				column.align().begin();
			else
				column.align().center();
		}

		@Override
		public double defaultWeight() {
			return WEIGHT_NUMBER;
		}
	}

	public interface Decorator<T> {

		void decorate(Object identifier, IBulkTableCell cell, T value);

		void prepare(IBulkTableWidget.IColumn column);

		double defaultWeight();
	}

	public class StringDecorator implements Decorator<String> {

		private double weight = WEIGHT_TEXT;

		public StringDecorator(boolean isShort, boolean isLong) {
			if (isShort)
				weight = WEIGHT_CUSTOM_LIST_SELECTION;
			if (isLong)
				weight = WEIGHT_LONG_TEXT;
		}

		@Override
		public void decorate(final Object identifier, IBulkTableCell cell, String value) {
			String text = (String) value;
			injectColor(identifier, cell, text);
			cell.text(text);
			if (updateListener != null && updateListener.isEditable(identifier)) {
				cell.updateListener(new IUpdateListener<String>() {

					@Override
					public void onUpdate(String value) {
						updateListener.onUpdate(identifier, value);
					}
				});
			}
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
		}

		@Override
		public double defaultWeight() {
			return weight;
		}
	}

	public class HTMLDecorator implements Decorator<String> {

		@Override
		public void decorate(Object identifier, IBulkTableCell cell, String value) {
			String text = (String) value;
			cell.html(text);
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
		}

		@Override
		public double defaultWeight() {
			return WEIGHT_HTML;
		}
	}

	public class ImageDecorator implements Decorator<String> {

		@Override
		public void decorate(Object identifier, IBulkTableCell cell, String value) {
			cell.image((String) value);
		}

		@Override
		public void prepare(
				co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn column) {
		}

		@Override
		public double defaultWeight() {
			return WEIGHT_IMAGE;
		}
	}

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
	AlignmentMemento<IScrollTableColumn<Object>> alignment = new AlignmentMemento<IScrollTableColumn<Object>>(
			this);
	boolean visible = true;
	int widthInt = -1;
	double widthDouble = -1;
	boolean editable = false;
	private IColorAdapter<Object, Object> colorAdapter;
	boolean forceSort = false;

	ScrollTableColumnImpl(ScrollTableWidgetImpl widget, int index) {
		this.widget = widget;
		this.index = index;
	}

	private void injectColor(Object identifier, IBulkTableCell cell, Object value) {
		if (colorAdapter != null) {
			String color = colorAdapter.color(identifier, value);
			cell.color(color);
		}
	}

	@SuppressWarnings("unchecked")
	void decorate(Object identifier, IBulkTableCell cell, Object value) {
		try {
			decorator().decorate(identifier, cell, value);
		} catch (ClassCastException e) {
			throw new RuntimeException("Column " + name + ", index: " + index
					+ ", received invalid value " + value + " ("
					+ value.getClass().getName() + ")");
		}
		if (!alignment.type.equals(Type.BEGIN)) {
			// TODO ... throw new MethodNotImplementedException();
		}
	}

	@SuppressWarnings("rawtypes")
	Decorator decorator() {
		if (decorator == null) {
			if (type.isHTML) {
				decorator = new HTMLDecorator();
			} else if (type.clazz.equals(String.class)) {
				decorator = new StringDecorator(type.isShort, type.isLong);
			} else if (type.clazz.equals(IImage.class)) {
				decorator = new ImageDecorator();
			} else if (type.clazz.equals(Long.class)
					|| type.clazz.equals(Integer.class)) {
				decorator = new NumberDecorator();
			} else if (type.clazz.equals(Date.class)) {
				decorator = new DateDecorator(type.isLong, type.isShort);
			} else if (type.clazz.equals(Timestamp.class)) {
				decorator = new DateDecorator(true, false);
			} else if (type.clazz.equals(Boolean.class)) {
				decorator = new BooleanDecorator();
			} else if (type.clazz.equals(Double.class)) {
				decorator = new NumberDecorator();
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
	public IColumn<Object> tagSortOrder(boolean ascending) {
		tagSortOrder = !ascending;
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

	@Override
	public IScrollTableColumn<Object> visible(boolean visible) {
		this.visible = visible;
		return this;
	}

	@Override
	public boolean visible() {
		return visible;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public IScrollTableColumn<Object> colorAdapter(
			co.fxl.gui.table.scroll.api.IScrollTableColumn.IColorAdapter<Object, Object> colorAdapter) {
		this.colorAdapter = colorAdapter;
		return this;
	}

	@Override
	public IScrollTableColumn<Object> forceSort() {
		forceSort = true;
		return this;
	}

	double defaultWidth() {
		return widthDouble = decorator().defaultWeight();
	}
}