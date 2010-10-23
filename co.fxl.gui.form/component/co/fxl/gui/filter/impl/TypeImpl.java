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

import java.util.Date;

import co.fxl.gui.api.IImage;
import co.fxl.gui.filter.api.IFieldType;

class TypeImpl implements IFieldType {

	Class<?> type = String.class;
	boolean isLong = false;
	Object[] values = new Object[0];

	private IFieldType type(Class<?> clazz) {
		this.type = clazz;
		return this;
	}

	@Override
	public IFieldType date() {
		return type(Date.class);
	}

	@Override
	public IFieldType image() {
		return type(IImage.class);
	}

	@Override
	public IFieldType integer() {
		return type(Integer.class);
	}

	@Override
	public IFieldType longText() {
		isLong = true;
		return type(String.class);
	}

	@Override
	public IFieldType selection(Object... values) {
		this.values = values;
		return this;
	}

	@Override
	public IFieldType text() {
		return type(String.class);
	}
}
