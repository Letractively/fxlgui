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

import co.fxl.gui.api.template.ImageButton;
import co.fxl.gui.mdt.api.INavigationLink;

class NavigationLinkImpl implements INavigationLink<Object, Object> {

	String name;
	boolean inTable = true;
	boolean asDetail = true;
	List<INavigationLinkListener<Object>> listeners = new LinkedList<INavigationLinkListener<Object>>();
	private List<INavigationLinkSelectionListener<Object, Object>> selectionListeners = new LinkedList<INavigationLinkSelectionListener<Object, Object>>();
	boolean requiresSelection = true;
	Class<?> typeConstraint;
	String imageResource;
	private ImageButton label;
	private boolean requiresRows = false;
	private Class<?>[] exclusionConstraint = null;
	private IEntityConstraint<Object> constraint;
	IBaseEntityConstraint<Object> baseEntityConstraint;

	NavigationLinkImpl(String name) {
		this.name = name;
	}

	void setLabel(ImageButton label) {
		this.label = label;
		if (requiresSelection || typeConstraint != null)
			label.clickable(false);
	}

	void updateLabel(MasterDetailTableWidgetImpl widget) {
		boolean clickable = (!requiresSelection && typeConstraint == null)
				|| (!widget.selection.isEmpty() && (typeConstraint == null || typeConstraint
						.equals(widget.selection.get(0).getClass())));
		if (requiresSelection && !widget.selection.isEmpty()
				&& exclusionConstraint != null) {
			for (Class<?> exclusionConstraintInstance : exclusionConstraint) {
				Class<? extends Object> c = widget.selection.get(0).getClass();
				clickable &= !exclusionConstraintInstance.equals(c);
			}
		}
		if (requiresSelection && !widget.selection.isEmpty()
				&& constraint != null) {
			for (Object o : widget.selection) {
				clickable &= constraint.applies(o);
			}
		}
		if (widget.activeView instanceof DetailView && !asDetail)
			clickable = false;
		if (widget.activeView instanceof TableView && !inTable)
			clickable = false;
		if (widget.rowsInTable == 0 && requiresRows)
			clickable = false;
		clickable(clickable);
	}

	@Override
	public INavigationLink<Object, Object> addClickListener(
			INavigationLinkListener<Object> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public INavigationLink<Object, Object> asDetail(boolean asDetail) {
		this.asDetail = asDetail;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> inTable(boolean inTable) {
		this.inTable = inTable;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> requiresSelection(
			boolean requiresSelection) {
		this.requiresSelection = requiresSelection;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> typeConstraint(
			Class<?> typeConstraint) {
		this.typeConstraint = typeConstraint;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> imageResource(String imageResource) {
		this.imageResource = imageResource;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> requiresRows(boolean requiresRows) {
		this.requiresRows = requiresRows;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> text(String text) {
		this.name = text;
		if (label != null)
			label.text(text);
		return this;
	}

	@Override
	public INavigationLink<Object, Object> addSelectionListener(
			co.fxl.gui.mdt.api.INavigationLink.INavigationLinkSelectionListener<Object, Object> l) {
		selectionListeners.add(l);
		return this;
	}

	void notifySelection(List<Object> selection) {
		for (INavigationLinkSelectionListener<Object, Object> l : selectionListeners)
			l.onUpdate(this, selection);
	}

	@Override
	public INavigationLink<Object, Object> clickable(boolean b) {
		label.clickable(b);
		return this;
	}

	@Override
	public INavigationLink<Object, Object> exclusionConstraint(
			Class<?>[] typeConstraint) {
		exclusionConstraint = typeConstraint;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> entityConstraint(
			co.fxl.gui.mdt.api.INavigationLink.IEntityConstraint<Object> constraint) {
		this.constraint = constraint;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> baseEntityConstraint(
			co.fxl.gui.mdt.api.INavigationLink.IBaseEntityConstraint<Object> constraint) {
		baseEntityConstraint = constraint;
		return this;
	}
}
