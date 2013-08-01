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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.filter.api.ISuggestionAdp;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IRelationField;
import co.fxl.gui.impl.Display;

class FormRelationFieldImpl extends FormFieldImpl<ISuggestField, String>
		implements IRelationField {

	private ISuggestionAdp adp;

	FormRelationFieldImpl(FormWidgetImpl widget, int index, String name) {
		super(widget, index, name);
	}

	@Override
	public IFormField<ISuggestField, String> editable(boolean editable) {
		valueElement().editable(editable && adp != null);
		return super.editable(editable);
	}

	@Override
	void createContentColumn(int index) {
		valueElement = widget.addFormValueSuggestField(index);
		updateEditable();
	}

	void updateEditable() {
		editable(widget.saveListener != null);
	}

	@Override
	public void suggestionAdp(final ISuggestionAdp adp) {
		adp.suggestField(valueElement.source(adp));
		valueElement.addSuggestionListener(adp);
		this.adp = adp;
		updateEditable();
		valueElement.addFocusListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value) {
					String text = adp.text();
					valueElement.text(text);
				}
			}
		});
	}
}
