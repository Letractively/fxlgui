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

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.impl.ITooltipResolver;

class FormComboBoxImpl extends FormFieldImpl<IComboBox, String> {

	private IComboBox comboBox;

	// private boolean withFocus = true;

	FormComboBoxImpl(FormWidgetImpl widget, int index, String name) {
		super(widget, index, name);
	}

	// FormComboBoxImpl(FormWidgetImpl widget, int index, String name,
	// boolean withFocus) {
	// super(widget, index, name);
	// this.withFocus = withFocus;
	// }

	@Override
	public IFormField<IComboBox, String> editable(boolean editable) {
		valueElement().editable(editable);
		return super.editable(editable);
	}

	@Override
	public IComboBox valueElement() {
		return comboBox;
	}

	@Override
	void createContentColumn(int index) {
		comboBox = widget.addFormValueComboBox(index, true);// withFocus);
		editable(widget.saveListener != null);
	}

	@Override
	public IFormField<IComboBox, String> tooltip(final ITooltipResolver tooltip) {
		comboBox.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				comboBox.tooltip(tooltip.tooltip(value));
			}
		});
		return this;
	}
}
