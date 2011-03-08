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

import co.fxl.gui.api.template.FieldTypeImpl;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.mdt.api.IProperty;

class PropertyImpl implements IProperty<Object, Object> {

	String name;
	IAdapter<Object, Object> adapter;
	boolean displayInTable = true;
	boolean displayInDetailView = true;
	FieldTypeImpl type = new FieldTypeImpl();
	boolean sortable = false;
	boolean required;
	boolean editable = false;
	IUpdateListener<Object> listener;
	boolean filterable;
	IConstraintAdapter<Object, Object> constraintAdapter;

	PropertyImpl(String name) {
		this.name = name;
	}

	@Override
	public IProperty<Object, Object> adapter(IAdapter<Object, Object> adapter) {
		this.adapter = adapter;
		return this;
	}

	@Override
	public IProperty<Object, Object> asDetail(boolean displayInDetailView) {
		this.displayInDetailView = displayInDetailView;
		return this;
	}

	@Override
	public IProperty<Object, Object> inTable(boolean displayInTable) {
		this.displayInTable = displayInTable;
		return this;
	}

	@Override
	public IFieldType type() {
		return type;
	}

	@Override
	public IProperty<Object, Object> sortable(boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	@Override
	public IProperty<Object, Object> required() {
		required = true;
		return this;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public IProperty<Object, Object> editable(boolean b) {
		editable = b;
		return this;
	}

	@Override
	public IProperty<Object, Object> updateListener(
			co.fxl.gui.mdt.api.IProperty.IUpdateListener<Object> listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public IProperty<Object, Object> filterable() {
		filterable = true;
		return this;
	}

	@Override
	public IProperty<Object, Object> constraintAdapter(
			co.fxl.gui.mdt.api.IProperty.IConstraintAdapter<Object, Object> adapter) {
		constraintAdapter = adapter;
		return this;
	}
}
