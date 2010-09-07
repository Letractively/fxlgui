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
import java.util.LinkedList;
import java.util.List;


import co.fxl.gui.api.ILayout;
import co.fxl.gui.table.filter.api.ILazyTableWidget;
import co.fxl.gui.table.filter.api.IRowModel;
import co.fxl.gui.table.filter.api.IRowModel.Constraint;
import co.fxl.gui.table.filter.api.IRowModel.Row;
import co.fxl.gui.table.impl.RowImpl;

class LazyTableWidgetImpl extends FilterTableWidgetImpl implements
		ILazyTableWidget<Object> {

	private IRowModel<Object> rowModel;
	private boolean setUpFilter = false;
	private ComboBoxIntegerFilter sizeFilter;

	LazyTableWidgetImpl(ILayout layout) {
		super(layout);
	}

	@Override
	public ILazyTableWidget<Object> rowSource(IRowModel<Object> rowModel) {
		this.rowModel = rowModel;
		return this;
	}

	@Override
	public LazyTableWidgetImpl visible(boolean visible) {
		super.visible(visible);
		initFilter();
		removeFilters();
		return this;
	}

	private void initFilter() {
		if (setUpFilter)
			return;
		sizeFilter = (ComboBoxIntegerFilter) filter.addFilter(Integer.class,
				"Size", -1, Arrays
						.asList(new Object[] { 20, 50, 100, 500, 1000 }));
		setUpFilter = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	void filter(List<FilterTemplate<Object>> filters) {
		List<Constraint> constraints = new LinkedList<Constraint>(filters);
		constraints.remove(sizeFilter);
		List<Row<Object>> modelRows = rowModel.query(constraints,
				(Integer) sizeFilter.values()[0]);
		int i = 0;
		for (; i < modelRows.size(); i++) {
			Row<Object> modelRow = modelRows.get(i);
			if (i < rows.size()) {
				RowImpl row = rows.get(i);
				row.content.values.clear();
				for (int j = 0; j < columns.size(); j++) {
					Comparable<Object> value = (Comparable<Object>) modelRow
							.valueAt(j);
					row.content.values.add(value);
				}
				row.content.selected = false;
				row.content.visible = true;
			} else {
				RowImpl row = addRow();
				for (int j = 0; j < columns.size(); j++) {
					Comparable<Object> value = (Comparable<Object>) modelRow
							.valueAt(j);
					row.add(value);
				}
			}
		}
		while (i < rows.size()) {
			rows.get(i).content.visible = false;
			i++;
		}
		updateRowPresentation();
	}

	@Override
	void removeFilters() {
		filter(new LinkedList<FilterTemplate<Object>>());
		updateRowPresentation();
	}
}
