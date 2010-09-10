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
package co.fxl.gui.table.filter.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.filter.api.IFilter;
import co.fxl.gui.table.filter.impl.FilterTemplate.FilterListener;
import co.fxl.gui.table.impl.ColumnImpl;

class FilterImpl implements IFilter {

	private class ClearClickListener implements IClickListener {

		@Override
		public void onClick() {
			for (IFilterPart<?> filter : filters) {
				filter.clear();
			}
			widget.removeFilters();
		}
	}

	private class ApplyClickListener implements IClickListener {

		@SuppressWarnings("unchecked")
		@Override
		public void onClick() {
			List<FilterTemplate<Object>> activeFilters = new LinkedList<FilterTemplate<Object>>();
			for (IFilterPart<?> filter : filters) {
				boolean active = filter.update();
				if (active)
					activeFilters.add((FilterTemplate<Object>) filter);
			}
			if (!activeFilters.isEmpty()) {
				widget.filter(activeFilters);
			}
		}
	}

	private List<IFilterPart<?>> filters = new LinkedList<IFilterPart<?>>();
	private IClickable<?> apply;
	private IClickable<?> clear;
	private List<Boolean> activeFlags = new LinkedList<Boolean>();
	private FilterTableWidgetImpl widget;
	private IGridPanel grid;

	FilterImpl(FilterTableWidgetImpl widget, ILayout panel) {
		this.widget = widget;
		WidgetTitle title = new WidgetTitle(panel);
		title.addTitle("Filter");
		apply = title.addHyperlink("Apply");
		clear = title.addHyperlink("Clear");
		apply.addClickListener(new ApplyClickListener());
		apply.clickable(false);
		clear.addClickListener(new ClearClickListener());
		clear.clickable(false);
		this.grid = title.content().panel().grid().indent(3);
	}

	@Override
	public IFilter filterable(IColumn column, Object... values) {
		ColumnImpl columnImpl = (ColumnImpl) column;
		return filterable(column, columnImpl.contentType, values);
	}

	@Override
	public IFilter filterable(IColumn column, Class<?> contentType,
			Object... values) {
		ColumnImpl columnImpl = (ColumnImpl) column;
		String name = columnImpl.name;
		int columnIndex = columnImpl.columnIndex;
		List<Object> list = new LinkedList<Object>(Arrays.asList(values));
		if (!list.isEmpty()) {
			list.add(0, null);
		}
		addFilter(contentType, name, columnIndex, list);
		return this;
	}

	IFilterPart<?> addFilter(Class<?> contentType, String name,
			int columnIndex, List<Object> values) {
		IFilterPart<?> filter;
		if (!values.isEmpty()) {
			if (contentType.equals(String.class)) {
				filter = new ComboBoxStringFilter(columnIndex, grid, name,
						values, filters.size());
			} else if (contentType.equals(Integer.class)) {
				filter = new ComboBoxIntegerFilter(columnIndex, grid, name,
						values, filters.size());
			} else if (contentType.equals(IImage.class)) {
				filter = new ComboBoxStringFilter(columnIndex, grid, name,
						values, filters.size());
			} else
				throw new MethodNotImplementedException();
		} else if (contentType.equals(String.class))
			filter = new StringFilter(columnIndex, grid, name, filters.size());
		else if (contentType.equals(Date.class))
			filter = new DateFilter(columnIndex, grid, name, filters.size());
		else if (contentType.equals(Integer.class))
			filter = new NumberFilter(columnIndex, grid, name, filters.size());
		else
			throw new MethodNotImplementedException(contentType.getName());
		activeFlags.add(false);
		final int filterIndex = filters.size();
		filter.addUpdateListener(new FilterListener() {

			@Override
			public void onActive(boolean isActive) {
				activeFlags.set(filterIndex, isActive);
				boolean overallActive = false;
				for (boolean flag : activeFlags)
					overallActive |= flag;
				apply.clickable(overallActive);
				clear.clickable(overallActive);
			}
		});
		filters.add(filter);
		return filter;
	}
}
