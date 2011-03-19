/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
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

import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.form.api.IDateField;
import co.fxl.gui.input.api.IDateTextFieldWidget;

class FormDateFieldImpl extends FormFieldImpl<IDateField> implements IDateField {

	IDateTextFieldWidget textField;

	FormDateFieldImpl(FormWidgetImpl widget, int index, String name) {
		super(widget, index, name);
		textField = addTextField(widget);
		textField.border().color().gray();
		widget.addFillColumn();
	}

	IDateTextFieldWidget addTextField(FormWidgetImpl widget) {
		return widget.addFormValueDateField();
	}

	@Override
	public IDateField valueElement() {
		return this;
	}

	@Override
	public IUpdateable<Date> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<Date> listener) {
		textField.addUpdateListener(listener);
		return this;
	}

	@Override
	public IDateField date(Date date) {
		textField.date(date);
		return this;
	}

	@Override
	public Date date() {
		return textField.date();
	}
}
