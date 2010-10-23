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
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.mdt.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.mdt.api.IFilterList;
import co.fxl.gui.mdt.api.IProperty;

class FilterListImpl implements IFilterList<Object> {

	List<FilterImpl> filters = new LinkedList<FilterImpl>();
	IFilterConstraints constraints;

	@Override
	public IFilter addFilter() {
		FilterImpl filter = new FilterImpl();
		filters.add(filter);
		return filter;
	}

	@Override
	public IFilterList<Object> addPropertyFilter(IProperty<Object, ?> property) {
		FilterImpl filter = new FilterImpl((PropertyImpl) property);
		filters.add(filter);
		return this;
	}

	@Override
	public IFilterList<Object> constraints(IFilterConstraints constraints) {
		this.constraints = constraints;
		return this;
	}
}
