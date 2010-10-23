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
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.filter.api.IFilterTableWidget;
import co.fxl.gui.table.filter.api.ILazyTableWidget;

class LazyTableWidgetImpl extends FilterTableWidgetImpl implements
		ILazyTableWidget<Object> {

	class RowModel implements IRowModel<Object> {

		private boolean reset = false;

		private void reset() {
			mainPanel.visible(false);
			if (rowListener != null)
				rowListener.clear();
			init = false;
			selectionPanel = null;
			rows.clear();
			reset = true;
		}

		RowModel() {
			notifyServerCall();
		}

		@Override
		public IFilterTableWidget<Object> notifyServerCall() {
			filter.holdFilterClicks = true;
			return LazyTableWidgetImpl.this;
		}

		@Override
		public IRow<Object> addRow() {
			if (!reset) {
				reset();
			}
			return LazyTableWidgetImpl.this.addRow();
		}

		@Override
		public void onSuccess() {
			if (!reset) {
				reset();
				init();
				if (rowListener != null)
					rowListener.update();
			}
			mainPanel.visible(true);
			filter.holdFilterClicks = false;
		}

		@Override
		public void onFail() {
			throw new MethodNotImplementedException();
		}
	}

	private boolean setUpFilter = false;
	IFilterListener<Object> filterListener;
	private ComboBoxIntegerFilter sizeFilter;

	LazyTableWidgetImpl(ILayout layout) {
		super(layout);
	}

	@Override
	public ILazyTableWidget<Object> addFilterListener(
			IFilterListener<Object> listener) {
		this.filterListener = listener;
		return this;
	}

	@Override
	void filter(List<FilterTemplate<Object>> activeFilters) {
		if (filterListener != null) {
			Constraints constraints = new Constraints();
			for (FilterTemplate<Object> filter : activeFilters) {
				constraints.add(filter.asConstraint());
			}
			constraints.add(sizeFilter.asConstraint());
			filterListener.onRefresh(resetRowModel(), constraints);
		} else
			super.filter(activeFilters);
	}

	@Override
	void removeFilters() {
		if (filterListener != null) {
			filter(new LinkedList<FilterTemplate<Object>>());
		} else
			super.removeFilters();
	}

	@Override
	public LazyTableWidgetImpl visible(boolean visible) {
		super.visible(visible);
		initFilter();
		if (rows.isEmpty())
			filter.apply();
		removeFilters();
		return this;
	}

	@Override
	void initFilter() {
		if (setUpFilter)
			return;
		sizeFilter = (ComboBoxIntegerFilter) filter.addFilter(Integer.class,
				"Size", -1, Arrays
						.asList(new Object[] { 20, 50, 100, 500, 1000 }));
		sizeFilter.validate(filter.validation);
		setUpFilter = true;
	}

	@Override
	public IRowModel<Object> resetRowModel() {
		return new RowModel();
	}
}
