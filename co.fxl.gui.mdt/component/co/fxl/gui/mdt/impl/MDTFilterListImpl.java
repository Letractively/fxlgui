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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.mdt.api.IMDTFilterList;
import co.fxl.gui.mdt.api.IProperty;

class MDTFilterListImpl implements IMDTFilterList<Object> {

	private MasterDetailTableWidgetImpl mdt;
	List<MDTFilterImpl> filters = new LinkedList<MDTFilterImpl>();
	Map<Integer, String> configuration2index = new HashMap<Integer, String>();
	IFilterConstraints constraints;

	MDTFilterListImpl(MasterDetailTableWidgetImpl mdt) {
		this.mdt = mdt;
	}

	@Override
	public IMDTFilter addFilter() {
		MDTFilterImpl filter = new MDTFilterImpl();
		filters.add(filter);
		return filter;
	}

	@Override
	public IMDTFilterList<Object> addPropertyFilter(
			IProperty<Object, ?> property) {
		MDTFilterImpl filter = new MDTFilterImpl((PropertyImpl) property);
		filters.add(filter);
		return this;
	}

	@Override
	public IMDTFilterList<Object> constraints(IFilterConstraints constraints) {
		this.constraints = constraints;
		return this;
	}

	@Override
	public IMDTRelationFilter<Object, ?> addRelationFilter() {
		MDTRelationFilterImpl filter = new MDTRelationFilterImpl();
		filters.add(filter);
		return filter;
	}

	@Override
	public IMDTFilterList<Object> addConfiguration(String configuration) {
		mdt.addConfiguration(configuration);
		configuration2index.put(filters.size(), configuration);
		return this;
	}
}
