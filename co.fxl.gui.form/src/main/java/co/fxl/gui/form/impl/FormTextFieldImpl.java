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

import co.fxl.gui.api.ITextField;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.impl.IFieldType;

class FormTextFieldImpl<R> extends FormFieldImpl<ITextField, R> {

	FormTextFieldImpl(FormWidgetImpl widget, int index, String name,
			IFieldType type) {
		super(widget, index, name, type, null);
	}

	@Override
	public IFormField<ITextField, R> editable(boolean editable) {
		valueElement().editable(editable && !useAssignButton);
		return super.editable(editable);
	}

	boolean isExpand() {
		boolean b = !type.isShort && !type.isRelation
				&& !type.clazz.equals(Long.class)
				&& !type.clazz.equals(Integer.class);
		return b;
	}

	boolean withFocus() {
		return true;
	}

	@Override
	boolean requiresPaddingRight() {
		return true;
	}

	@Override
	void createContentColumn(int index) {
		valueElement = widget.addFormValueTextField(index, withFocus(),
				isExpand(), type.template);
		editable(widget.saveListener != null);
	}
}
