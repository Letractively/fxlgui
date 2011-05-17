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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.template.FieldTypeImpl;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.impl.FormWidgetImpl.FormEntryLabel;

public abstract class FormFieldImpl<T, R> implements IFormField<T, R> {

	FormWidgetImpl widget;
	private ILabel label;
	private IGridCell cell;
	FieldTypeImpl type = new FieldTypeImpl();
	boolean required = false;
	private int row;
	private int column = 2;

	public FormFieldImpl(FormWidgetImpl widget, int index, String name) {
		this.widget = widget;
		widget.fields.add(this);
		FormEntryLabel formEntryLabel = widget.addFormEntryLabel(name);
		cell = formEntryLabel.cell;
		label = formEntryLabel.formEntryLabel;
		widget.addFillColumn();
		this.row = index;
	}

	@Override
	public IFormField<T, R> editable(boolean editable) {
		if (editable)
			label.font().color().black();
		else
			label.font().color().gray();
		checkFocus(editable);
		return this;
	}

	void checkFocus(boolean editable) {
		if (!editable)
			widget.looseFocus(valueElement());
	}

	@Override
	public ILabel addButton(String title) {
		ILabel l = addContainer().label().hyperlink().text(title);
		l.font().pixel(11);
		return l;
	}

	@Override
	public IContainer addContainer() {
		return widget.grid().cell(column, row).valign().center().align()
				.begin().panel().horizontal().align().begin().add().panel()
				.horizontal().align().begin().addSpace(1).add();
	}

	@Override
	public ILabel titleElement() {
		return label;
	}

	@Override
	public IGridCell cell() {
		return cell;
	}

	@Override
	public IFormField<T, R> required() {
		required = true;
		widget.hasRequiredAttributes = true;
		label.font().weight().bold();
		label.text(label.text() + " *");
		return this;
	}

	@Override
	public IFieldType type() {
		return type;
	}

	@Override
	public void remove() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IUpdateable<R> addUpdateListener(IUpdateListener<R> listener) {
		throw new MethodNotImplementedException();
	}
}
