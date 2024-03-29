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

import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget.IInputElementFactory;
import co.fxl.gui.style.impl.Style;

class FormTextAreaImpl extends FormFieldImpl<ITextArea, String> {

	FormTextAreaImpl(FormWidgetImpl widget, int index, String name,
			IInputElementFactory f) {
		super(false, widget, index, name, f);
	}

	@Override
	void createContentColumn(int index) {
		valueElement = widget.addFormValueTextArea(index, f);
		format(valueElement, 100);
		for (IResizeListener l : widget.resizeListeners)
			valueElement.addResizeListener(l);
		editable(widget.saveListener != null);
	}

	static void format(final ITextArea textArea, final int height) {
		textArea.height(height);
		Style.instance().form().textArea(textArea);
		// textArea.addFocusListener(new IUpdateListener<Boolean>() {
		//
		// @Override
		// public void onUpdate(Boolean value) {
		// if (value) {
		// int target = height * 2;
		// if (target > height)
		// textArea.height(target);
		// } else {
		// textArea.height(height);
		// }
		// }
		// });
	}

	@Override
	public IFormField<ITextArea, String> editable(boolean editable) {
		valueElement().editable(editable);
		return super.editable(editable);
	}

	@Override
	boolean requiresPaddingRight() {
		return true;
	}

	@Override
	void clearTextAreaWidth() {
		valueElement().width(1.0);
	}

}
