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
package co.fxl.gui.api.template;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IImage;

public class FieldTypeImpl implements IFieldType {

	public Class<?> clazz = String.class;
	public boolean isLong = false;
	public List<Object> values = new LinkedList<Object>();

	@Override
	public IFieldType type(Class<?> clazz) {
		this.clazz = clazz;
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
	public IFieldType addConstraint(Object... values) {
		for (Object v : values)
			this.values.add(v);
		return this;
	}

	@Override
	public IFieldType text() {
		return type(String.class);
	}

	@Override
	public IFieldType longType() {
		return type(Long.class);
	}
}
