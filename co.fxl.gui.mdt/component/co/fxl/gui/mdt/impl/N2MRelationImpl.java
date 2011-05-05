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

import co.fxl.gui.mdt.api.IN2MRelation;
import co.fxl.gui.n2m.api.IN2MWidget.IItemImageProvider;

class N2MRelationImpl implements IN2MRelation<Object, Object> {

	IAdapter<Object, Object> adapter;
	String name;
	Class<?> constrainType;
	String itemImage;
	IItemImageProvider<Object> itemImageProvider;

	N2MRelationImpl(String name) {
		this.name = name;
	}

	@Override
	public IN2MRelation<Object, Object> adapter(IAdapter<Object, Object> adapter) {
		this.adapter = adapter;
		return this;
	}

	@Override
	public IN2MRelation<Object, Object> constrainType(Class<?> class1) {
		constrainType = class1;
		return this;
	}

	boolean applies(Object node) {
		if (constrainType == null)
			return true;
		return constrainType.equals(node.getClass());
	}

	@Override
	public IN2MRelation<Object, Object> itemImage(String itemImage) {
		this.itemImage = itemImage;
		return this;
	}

	@Override
	public IN2MRelation<Object, Object> itemImageProvider(
			IItemImageProvider<Object> itemImageProvider) {
		this.itemImageProvider = itemImageProvider;
		return this;
	}
}
