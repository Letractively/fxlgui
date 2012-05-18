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
package co.fxl.gui.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IImage;

public class FieldTypeImpl implements IFieldType {

	public Class<?> clazz = String.class;
	public boolean isLong = false;
	private List<Object> values;
	public boolean isRelation = false;
	public int maxLength = -1;
	public boolean encryptedText = false;
	public boolean isShort = false;
	public boolean isColor = false;
	public boolean isHTML = false;

	public boolean equals(Object o) {
		FieldTypeImpl t = (FieldTypeImpl) o;
		if (!(clazz.equals(t.clazz) && isLong == t.isLong
				&& isRelation == t.isRelation && maxLength == t.maxLength
				&& encryptedText == t.encryptedText && isShort == t.isShort
				&& isColor == t.isColor && isHTML == t.isHTML))
			return false;
		if (values == null)
			return t.values == null;
		if (t.values == null)
			return values == null;
		if (values.size() != t.values.size())
			return false;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) == null) {
				return t.values.get(i) == null;
			} else if (!values.get(i).equals(t.values.get(i)))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return isHTML ? "HTML" : String.valueOf(clazz);
	}

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
	public IFieldType color() {
		isColor = true;
		return type(String.class);
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
		if (this.values == null)
			setHasConstraints();
		for (Object v : values)
			this.values.add(v);
		return this;
	}

	@Override
	public IFieldType setHasConstraints() {
		this.values = new LinkedList<Object>();
		return this;
	}

	public boolean hasConstraints() {
		return values != null;
	}

	public List<Object> getConstraints() {
		if (values == null)
			return new LinkedList<Object>();
		return values;
	}

	@Override
	public IFieldType text() {
		return type(String.class);
	}

	@Override
	public IFieldType shortText() {
		isShort = true;
		return type(String.class);
	}

	@Override
	public IFieldType longType() {
		return type(Long.class);
	}

	@Override
	public IFieldType logic() {
		return type(Boolean.class);
	}

	@Override
	public Class<?> clazz() {
		return clazz;
	}

	@Override
	public IFieldType relation() {
		isRelation = true;
		return this;
	}

	@Override
	public IFieldType maxLength(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	@Override
	public IFieldType encryptedText() {
		encryptedText = true;
		return this;
	}

	@Override
	public IFieldType clearConstraints() {
		if (values != null)
			values.clear();
		return this;
	}

	@Override
	public IFieldType time() {
		type(Date.class);
		isShort = true;
		return this;
	}

	@Override
	public IFieldType dateTime() {
		type(Date.class);
		isLong = true;
		return this;
	}

	@Override
	public IFieldType html() {
		isHTML = true;
		return this;
	}

	@Override
	public IFieldType doubleValue() {
		type(Double.class);
		return this;
	}

	@Override
	public boolean isRelation() {
		return isRelation;
	}

	@Override
	public boolean isText() {
		return clazz.equals(String.class) && values == null;
	}

	public boolean isImage() {
		return clazz.equals(IImage.class);
	}
}
