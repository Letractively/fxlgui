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

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.api.IImageField;

class FormWidgetImpl implements IFormWidget {

	class FormEntryLabel {

		IGridCell cell;
		ILabel formEntryLabel;

		FormEntryLabel(IGridCell cell, ILabel formEntryLabel) {
			this.cell = cell;
			this.formEntryLabel = formEntryLabel;
		}
	}

	private int gridIndex = 0;
	private WidgetTitle widgetTitle;
	private IGridPanel gridPanel;

	FormWidgetImpl(ILayout panel) {
		widgetTitle = new WidgetTitle(panel);
		widgetTitle.foldable(false);
	}

	FormEntryLabel addFormEntryLabel(String name) {
		IGridCell cell = grid().cell(0, gridIndex).align().end().valign()
				.center().height(30);
		ILabel formEntryLabel = cell.label();
		formEntryLabel.text(name);
		formEntryLabel.font();// .pixel(12).color().gray();
		return new FormEntryLabel(cell, formEntryLabel);
	}

	private IContainer container() {
		IGridCell cell = grid().cell(1, gridIndex).valign().center();
		cell.height(30);
		gridIndex++;
		return cell;
	}

	void addFillColumn() {
	}

	ITextField addFormValueTextField() {
		ITextField valuePanel = container().textField();
		valuePanel.height(28);
		return valuePanel;
	}

	IPasswordField addFormValuePasswordField() {
		IPasswordField valuePanel = container().passwordField();
		return valuePanel;
	}

	ITextArea addFormValueTextArea() {
		ITextArea valuePanel = container().textArea();
		return valuePanel;
	}

	IComboBox addFormValueComboBox() {
		IComboBox valuePanel = container().comboBox();
		return valuePanel;
	}

	ICheckBox addFormValueCheckBox() {
		ICheckBox valuePanel = container().checkBox().height(30);
		return valuePanel;
	}

	ILabel addFormLabel() {
		ILabel label = container().label();
		return label;
	}

	IImage addImage() {
		return container().panel().horizontal().add().image();
	}

	@Override
	public IFormField<ICheckBox> addCheckBox(String name) {
		return new FormCheckBoxImpl(this, name);
	}

	@Override
	public IFormField<ITextArea> addTextArea(String name) {
		return new FormTextAreaImpl(this, name);
	}

	@Override
	public IFormField<ITextField> addTextField(String name) {
		return new FormTextFieldImpl(this, name);
	}

	@Override
	public IFormField<IPasswordField> addPasswordField(String name) {
		return new FormPasswordFieldImpl(this, name);
	}

	@Override
	public IImageField addImage(String name) {
		return new ImageFieldImpl(this, name);
	}

	@Override
	public IFormField<IComboBox> addComboBox(String name) {
		return new FormComboBoxImpl(this, name);
	}

	@Override
	public IFormField<ILabel> addLabel(String name) {
		return new FormLabelImpl(this, name);
	}

	@Override
	public ILabel addOKHyperlink() {
		return addHyperlink("OK");
	}

	@Override
	public ILabel addCancelHyperlink() {
		return addHyperlink("Cancel");
	}

	@Override
	public ILabel addHyperlink(String name, IClickListener clickListener) {
		return widgetTitle.addHyperlink(name).addClickListener(clickListener)
				.mouseLeft();
	}

	@Override
	public FormWidgetImpl visible(boolean visible) {
		return this;
	}

	public IGridPanel grid() {
		if (gridPanel == null) {
			gridPanel = widgetTitle.content().panel().grid();
			gridPanel.indent(4);
		}
		return gridPanel;
	}

	@Override
	public ILabel addHyperlink(String name) {
		return widgetTitle.addHyperlink(name);
	}

	@Override
	public ILabel addTitle(String title) {
		return widgetTitle.addTitle(title);
	}
}
