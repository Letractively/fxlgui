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

import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.impl.FieldTypeImpl;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.mdt.api.IMDTFilterList.IMDTFilter;

class MDTFilterImpl implements IMDTFilter {

	String name;
	FieldTypeImpl type = new FieldTypeImpl();
	PropertyImpl property;
	boolean asDetail = false;
	boolean inTable = false;

	MDTFilterImpl() {
	}

	MDTFilterImpl(PropertyImpl property) {
		this.property = property;
		asDetail = property.displayInDetailView;
		inTable = property.displayInTable;
	}

	@Override
	public IFilter name(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IFieldType type() {
		return type;
	}

	@Override
	public IMDTFilter inTable(boolean inTable) {
		this.inTable = inTable;
		return this;
	}

	@Override
	public IMDTFilter asDetail(boolean asDetail) {
		this.asDetail = asDetail;
		return this;
	}

	@Override
	public IFilter type(IFieldType type) {
		this.type = (FieldTypeImpl) type;
		return this;
	}
}
