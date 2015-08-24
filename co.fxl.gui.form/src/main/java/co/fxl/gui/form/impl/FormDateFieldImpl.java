/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.form.impl;

import java.util.Date;

import co.fxl.data.format.api.IFormat;
import co.fxl.data.format.impl.Format;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.impl.FieldTypeImpl;
import co.fxl.gui.impl.IFieldType;

class FormDateFieldImpl extends FormTextFieldImpl<Date> {

	private DateField dateField;

	FormDateFieldImpl(boolean newLine, final FormWidgetImpl widget, int index,
			String name, boolean addCalendar, IFormat<Date> f) {
		super(newLine, widget, index, name, null);
		dateField = new DateField(valueElement(), addCalendar ? addContainer()
				: null, addCalendar, f);
		editable(widget.saveListener != null);
		type = new FieldTypeImpl() {

			@Override
			public IFieldType dateTime() {
				dateField.format(Format.dateTime());
				return super.dateTime();
			}

			@Override
			public IFieldType time() {
				dateField.format(Format.time());
				return super.time();
			}
		};
	}

	@Override
	boolean isExpand() {
		return false;
	}

	@Override
	public IFormField<ITextField, Date> editable(boolean editable) {
		if (dateField != null)
			dateField.clickable(editable);
		return super.editable(editable);
	}
}