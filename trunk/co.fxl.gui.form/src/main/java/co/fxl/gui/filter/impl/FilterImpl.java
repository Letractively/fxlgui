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
package co.fxl.gui.filter.impl;

import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.impl.ComboBox.IColorAdapter;
import co.fxl.gui.impl.FieldTypeImpl;
import co.fxl.gui.impl.IFieldType;

class FilterImpl implements IFilter {

	FieldTypeImpl type = new FieldTypeImpl();
	String name;
	IUpdateListener<String> updateListener;
	boolean required = false;
	String text;
	boolean directApply = true;
	IGlobalValue value;
	IColorAdapter colorAdapter;

	@Override
	public IFilter name(String name) {
		assert name != null;
		this.name = name;
		return this;
	}

	@Override
	public IFieldType type() {
		return type;
	}

	@Override
	public IFilter type(IFieldType type) {
		this.type = (FieldTypeImpl) type;
		return this;
	}

	@Override
	public IFilter updateListener(IUpdateListener<String> l) {
		updateListener = l;
		return this;
	}

	@Override
	public IFilter required() {
		required = true;
		return this;
	}

	@Override
	public IFilter text(String c) {
		this.text = c;
		return this;
	}

	@Override
	public IFilter directApply(boolean directApply) {
		this.directApply = directApply;
		return this;
	}

	@Override
	public IFilter globalValue(IGlobalValue value) {
		this.value = value;
		return this;
	}

	@Override
	public IFilter colorAdapter(IColorAdapter colorAdapter) {
		this.colorAdapter = colorAdapter;
		return this;
	}
}
