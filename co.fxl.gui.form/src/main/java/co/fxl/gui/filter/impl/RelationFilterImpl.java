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
package co.fxl.gui.filter.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;

class RelationFilterImpl extends FilterImpl implements
		IRelationFilter<Object, Object> {

	List<Object> preset;
	IAdapter<Object, Object> adapter;

	@Override
	public IRelationFilter<Object, Object> preset(List<Object> entities) {
		this.preset = entities;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IRelationFilter<Object, Object> name(String name) {
		return (IRelationFilter<Object, Object>) super.name(name);
	}

	@Override
	public IRelationFilter<Object, Object> adapter(
			IAdapter<Object, Object> adapter) {
		this.adapter = adapter;
		return this;
	}

	@Override
	public String toString() {
		return RelationFilter.toString(adapter, preset,
				new LinkedList<Object>());
	}
}
