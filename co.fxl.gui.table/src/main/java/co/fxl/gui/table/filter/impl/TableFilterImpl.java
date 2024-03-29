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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.table.filter.impl;

import java.util.Arrays;
import java.util.List;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.filter.api.IFilterTableWidget.ITableFilter;
import co.fxl.gui.table.impl.ColumnImpl;

class TableFilterImpl implements ITableFilter<Object>, IFilterListener {

	private FilterTableWidgetImpl widget;
	IFilterWidget filterWidget;

	TableFilterImpl(FilterTableWidgetImpl widget, ILayout layout) {
		this.widget = widget;
		filterWidget = (IFilterWidget) layout.vertical().add()
				.widget(IFilterWidget.class);
		filterWidget.addSizeFilter();
		filterWidget.addFilterListener(this);
	}

	@Override
	public ITableFilter<Object> apply() {
		filterWidget.apply(DummyCallback.voidInstance());
		return this;
	}

	@Override
	public ITableFilter<Object> filterable(IColumn<Object> column,
			Object... values) {
		ColumnImpl columnImpl = (ColumnImpl) column;
		return filterable(column, columnImpl.contentType, Arrays.asList(values));
	}

	@Override
	public ITableFilter<Object> filterable(IColumn<Object> column,
			Class<?> contentType, List<?> values) {
		ColumnImpl columnImpl = (ColumnImpl) column;
		String name = columnImpl.name;
		IFilter filter = filterWidget.addFilter();
		IFieldType type = filter.name(name).type();
		type.type(contentType);
		for (Object o : values)
			type.addConstraint(o);
		return this;
	}

	void holdFilterClicks(boolean b) {
		filterWidget.holdFilterClicks(b);
	}

	@Override
	public void onApply(IFilterConstraints constraints, ICallback<Void> cb) {
		widget.filter(constraints);
		cb.onSuccess(null);
	}

	@Override
	public ITableFilter<Object> constraints(IFilterConstraints constraints) {
		filterWidget.constraints(constraints);
		return this;
	}

	@Override
	public IFilter addFilter() {
		return filterWidget.addFilter();
	}

	@Override
	public IRelationFilter<?, ?> addRelationFilter() {
		return filterWidget.addRelationFilter();
	}

	@Override
	public ITableFilter<Object> addConfiguration(String configuration) {
		filterWidget.addConfiguration(configuration);
		return this;
	}
}
