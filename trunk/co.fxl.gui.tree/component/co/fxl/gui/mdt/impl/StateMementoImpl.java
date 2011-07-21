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
import co.fxl.gui.mdt.api.IMasterDetailTableWidget.IStatePacker;
import co.fxl.gui.mdt.api.IStateMemento;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.tree.api.ITreeWidget.IViewID;
import co.fxl.gui.tree.impl.ViewID;

class StateMementoImpl implements IStateMemento {

	boolean showDetailView;
	IFilterConstraints constraints;
	String configuration;
	LinkedList<Object> selection;
	IViewID registerSelection;
	Object relationRegisterSelection;
	List<String> hiddenColumns = new LinkedList<String>();
	boolean isPacked = false;

	StateMementoImpl(MasterDetailTableWidgetImpl widget,
			IStatePacker<Object> packer) {
		showDetailView = widget.activeView instanceof DetailView;
		if (widget.activeView instanceof DetailView) {
			registerSelection = ((DetailView) widget.activeView).tree
					.activeDetailView();
			relationRegisterSelection = widget.relationRegisterSelection
					.get(registerSelection);
		} else {
			TableView tv = (TableView) widget.activeView;
			if (tv != null) {
				for (IScrollTableColumn<Object> c : tv.property2column.values()) {
					if (!c.visible()) {
						hiddenColumns.add(c.name());
					}
				}
			}
		}
		constraints = widget.constraints;
		configuration = widget.configuration;
		if (packer == null || widget.selection.size() <= 1)
			selection = new LinkedList<Object>(widget.selection);
		else {
			selection = new LinkedList<Object>();
			for (Object o : widget.selection)
				selection.add(packer.pack(o));
			isPacked = true;
		}
	}

	StateMementoImpl() {
	}

	@Override
	public IStateMemento selection(Object selection) {
		this.selection = new LinkedList<Object>();
		this.selection.add(selection);
		return this;
	}

	@Override
	public IStateMemento constraints(IFilterConstraints constraints) {
		this.constraints = constraints;
		return this;
	}

	@Override
	public IStateMemento showDetailView() {
		this.showDetailView = true;
		return this;
	}

	@Override
	public IFilterConstraints constraints() {
		return constraints;
	}

	@Override
	public IStateMemento configuration(String configuration) {
		this.configuration = configuration;
		return this;
	}

	@Override
	public IStateMemento register(String register, Class<?>[] constrainType) {
		registerSelection = new ViewID(register, constrainType);
		return this;
	}
}
