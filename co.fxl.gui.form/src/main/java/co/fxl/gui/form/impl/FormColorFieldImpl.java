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

import co.fxl.gui.api.ITextField;
import co.fxl.gui.form.api.IFormField;

class FormColorFieldImpl extends FormTextFieldImpl<String> {

	private ColorField colorField;

	FormColorFieldImpl(boolean newLine, final FormWidgetImpl widget, int index,
			String name) {
		super(newLine, widget, index, name, null);
		colorField = new ColorField(valueElement(), addContainer());
		editable(widget.saveListener != null);
	}

	@Override
	boolean isExpand() {
		return false;
	}

	@Override
	public IFormField<ITextField, String> editable(boolean editable) {
		if (colorField != null)
			colorField.clickable(editable);
		return super.editable(editable);
	}
}