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

import co.fxl.gui.mdt.api.IProperty;
import co.fxl.gui.mdt.api.IRelation;
import co.fxl.gui.tree.api.ITreeWidget.IViewID;

class RelationImpl extends PropertyGroupImpl implements
		IRelation<Object, Object> {

	IAdapter<Object, Object> adapter;
	IShowListener<Object> showListener;
	IAddListener<Object, Object> addRemoveListener;
	boolean allowColumnSelection = true;
	boolean sortable = true;
	IUpDownListener<Object, Object> upDownListener;
	boolean editable = false;
	IEditListener<Object, Object> editListener;
	IEditableAdapter<Object> editableAdapter;
	List<IProperty<Object, ?>> hidden = new LinkedList<IProperty<Object, ?>>();
	IViewID viewID;

	RelationImpl(String name) {
		super(name);
	}

	@Override
	public IRelation<Object, Object> adapter(IAdapter<Object, Object> adapter) {
		this.adapter = adapter;
		return this;
	}

	@Override
	public IRelation<Object, Object> showListener(
			co.fxl.gui.mdt.api.IRelation.IShowListener<Object> l) {
		this.showListener = l;
		return this;
	}

	@Override
	public IRelation<Object, Object> addListener(
			co.fxl.gui.mdt.api.IRelation.IAddListener<Object, Object> l) {
		this.addRemoveListener = l;
		return this;
	}

	@Override
	public IRelation<Object, Object> allowColumnSelection(
			boolean allowColumnSelection) {
		this.allowColumnSelection = allowColumnSelection;
		return this;
	}

	@Override
	public IRelation<Object, Object> sortable(boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	@Override
	public IRelation<Object, Object> upDownListener(
			co.fxl.gui.mdt.api.IRelation.IUpDownListener<Object, Object> l) {
		upDownListener = l;
		return this;
	}

	@Override
	public IRelation<Object, Object> editable(boolean editable) {
		this.editable = editable;
		return this;
	}

	@Override
	public IRelation<Object, Object> editListener(
			co.fxl.gui.mdt.api.IRelation.IEditListener<Object, Object> editListener) {
		this.editListener = editListener;
		return this;
	}

	@Override
	public IRelation<Object, Object> editableAdapter(
			co.fxl.gui.mdt.api.IRelation.IEditableAdapter<Object> editableAdapter) {
		this.editableAdapter = editableAdapter;
		return this;
	}

	@Override
	public IRelation<Object, Object> hide(IProperty<Object, ?> property) {
		hidden.add(property);
		return this;
	}
}
