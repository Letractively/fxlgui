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

import java.util.List;

import co.fxl.gui.api.ILayout;
import co.fxl.gui.table.filter.api.IFilterTableWidget;
import co.fxl.gui.table.impl.RowImpl;
import co.fxl.gui.table.impl.TableWidgetImpl;

class FilterTableWidgetImpl extends TableWidgetImpl implements
		IFilterTableWidget<Object> {

	FilterImpl filter;

	FilterTableWidgetImpl(ILayout layout) {
		super(layout);
	}

	void filter(List<FilterTemplate<Object>> activeFilters) {
		for (RowImpl row : rows) {
			((FilterRowImpl) row).filter(activeFilters);
		}
		updateRowPresentation();
	}

	void removeFilters() {
		for (RowImpl row : rows) {
			row.content.visible = true;
			row.update();
		}
	}

	@Override
	public IFilter filterPanel(ILayout layout) {
		if (filter == null) {
			filter = new FilterImpl(this, layout);
		}
		return filter;
	}

	@Override
	public IFilter filter() {
		if (filter == null) {
			throw new MethodNotImplementedException("filter not set");
		}
		return filter;
	}

	@Override
	public FilterTableWidgetImpl visible(boolean visible) {
		return (FilterTableWidgetImpl) super.visible(visible);
	}

	@Override
	protected RowImpl newRowImpl(TableWidgetImpl tableWidgetImpl, int i) {
		return new FilterRowImpl(tableWidgetImpl, i);
	}

	void initFilter() {
	}
}
