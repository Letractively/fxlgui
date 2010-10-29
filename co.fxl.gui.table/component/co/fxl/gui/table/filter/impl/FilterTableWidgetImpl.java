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

import co.fxl.gui.api.ILayout;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.filter.api.IFilterTableWidget;
import co.fxl.gui.table.impl.TableWidgetImpl;

class FilterTableWidgetImpl extends TableWidgetImpl implements
		IFilterTableWidget<Object> {

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
			filter.holdFilterClicks(true);
			return FilterTableWidgetImpl.this;
		}

		@Override
		public IRow<Object> addRow() {
			if (!reset) {
				reset();
			}
			return FilterTableWidgetImpl.this.addRow();
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
			filter.holdFilterClicks(false);
		}

		@Override
		public void onFail() {
			throw new MethodNotImplementedException();
		}
	}

	TableFilterImpl filter;
	IFilterListener<Object> filterListener;

	FilterTableWidgetImpl(ILayout layout) {
		super(layout);
	}

	@Override
	public ITableFilter<Object> filterPanel(ILayout layout) {
		if (filter == null) {
			filter = new TableFilterImpl(this, layout);
		}
		return filter;
	}

	@Override
	public IFilterTableWidget<Object> addFilterListener(IFilterListener<Object> listener) {
		this.filterListener = listener;
		return this;
	}

	void filter(IFilterConstraints constraints) {
		if (filterListener != null) {
			filterListener.onRefresh(resetRowModel(), constraints);
		} else
			throw new MethodNotImplementedException();
	}

	@Override
	public FilterTableWidgetImpl visible(boolean visible) {
		super.visible(visible);
		if (filter != null)
			filter.filterWidget.visible(visible);
		if (rows.isEmpty())
			filter.apply();
		return this;
	}

	@Override
	public IRowModel<Object> resetRowModel() {
		return new RowModel();
	}
}
