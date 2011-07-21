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
import co.fxl.gui.mdt.api.IPropertyGroup;

public class PropertyGroupImpl implements IPropertyGroup<Object> {

	String name;
	List<PropertyImpl> properties = new LinkedList<PropertyImpl>();
	boolean asDetail = true;
	boolean inTable = true;
	Class<?>[] constrainType;
	
	@Override
	public String toString() {
		return getClass().getName() + ": " + name;
	}

	public PropertyGroupImpl(String name) {
		this.name = name;
	}

	@Override
	public IProperty<Object, ?> addProperty(String name) {
		PropertyImpl property = new PropertyImpl(name);
		properties.add(property);
		return property;
	}

	@Override
	public IPropertyGroup<Object> inTable(boolean show) {
		inTable = show;
		return this;
	}

	@Override
	public IPropertyGroup<Object> asDetail(boolean show) {
		asDetail = show;
		return this;
	}

	@Override
	public IPropertyGroup<Object> constrainType(Class<?> type) {
		constrainType = new Class<?>[] { type };
		return this;
	}

	boolean applies(Object node) {
		if (constrainType == null)
			return true;
		for (Class<?> c : constrainType)
			if (c == null || c.equals(node.getClass()))
				return true;
		return false;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public IPropertyGroup<Object> constrainType(Class<?>[] type) {
		constrainType = type;
		return this;
	}

	@Override
	public IPropertyGroup<Object> clear() {
		properties.clear();
		return this;
	}
}
