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
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.Validation;
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

	// static final int HEIGHT_CELL = 30;
	// static final int HEIGHT_CELL_INNER = 28;
	private int gridIndex = 0;
	private WidgetTitle widgetTitle;
	private IGridPanel gridPanel;
	boolean hasRequiredAttributes = false;
	private IVerticalPanel contentPanel;
	private ISaveListener saveListener = null;
	List<FormFieldImpl<?>> fields = new LinkedList<FormFieldImpl<?>>();
	private String saveTitle;
	private int fixLabelWidth = -1;
	private int fixValueWidth = -1;
	private ILabel requiredAttributeLabel;
	private boolean validate = true;
	Validation validation;
	private Heights heights = new Heights(2);
	private boolean isNew;

	FormWidgetImpl(IContainer panel) {
		widgetTitle = new WidgetTitle(panel.panel());
		widgetTitle.foldable(false);
	}

	FormEntryLabel addFormEntryLabel(String name) {
		IGridCell cell = grid().cell(0, gridIndex).align().end().valign()
				.center();// .height(HEIGHT_CELL);
		if (fixLabelWidth != -1)
			cell.width(fixLabelWidth);
		ILabel formEntryLabel = cell.label().autoWrap(false);
		formEntryLabel.text(name);
		formEntryLabel.font().pixel(12);
		return new FormEntryLabel(cell, formEntryLabel);
	}

	private IContainer container() {
		IGridCell cell = grid().cell(1, gridIndex).valign().center();
		if (fixValueWidth != -1)
			cell.width(fixValueWidth);
		heights.decorate(cell);
		gridIndex++;
		return cell;
	}

	void addFillColumn() {
	}

	ITextField addFormValueTextField() {
		ITextField valuePanel = container().textField();
		heights.decorate(valuePanel);
		valuePanel.editable(saveListener != null);
		return valuePanel;
	}

	IPasswordField addFormValuePasswordField() {
		IPasswordField valuePanel = container().passwordField();
		heights.decorate(valuePanel);
		valuePanel.editable(saveListener != null);
		return valuePanel;
	}

	ITextArea addFormValueTextArea() {
		ITextArea valuePanel = container().textArea();
		valuePanel.editable(saveListener != null);
		return valuePanel;
	}

	IComboBox addFormValueComboBox() {
		IComboBox valuePanel = container().comboBox();
		heights.decorate(valuePanel);
		valuePanel.editable(saveListener != null);
		return valuePanel;
	}

	ICheckBox addFormValueCheckBox() {
		ICheckBox valuePanel = container().checkBox();
		heights.valuePanel(valuePanel);
		valuePanel.editable(saveListener != null);
		return valuePanel;
	}

	ILabel addFormLabel() {
		ILabel label = container().label();
		heights.decorate(label);
		return label;
	}

	IImage addImage() {
		return container().panel().horizontal().add().image();
	}

	@Override
	public IFormField<ICheckBox> addCheckBox(String name) {
		return new FormCheckBoxImpl(this, gridIndex, name);
	}

	@Override
	public IFormField<ITextArea> addTextArea(String name) {
		return new FormTextAreaImpl(this, gridIndex, name);
	}

	@Override
	public IFormField<ITextField> addTextField(String name) {
		return new FormTextFieldImpl(this, gridIndex, name);
	}

	@Override
	public IFormField<IPasswordField> addPasswordField(String name) {
		return new FormPasswordFieldImpl(this, gridIndex, name);
	}

	@Override
	public IImageField addImage(String name) {
		return new ImageFieldImpl(this, name);
	}

	@Override
	public IFormField<IComboBox> addComboBox(String name) {
		return new FormComboBoxImpl(this, gridIndex, name);
	}

	@Override
	public IFormField<ILabel> addLabel(String name) {
		return new FormLabelImpl(this, gridIndex, name);
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
		if (saveListener != null || hasRequiredAttributes) {
			if (fields.isEmpty())
				return this;
			// assert !fields.isEmpty() : "no fields added to form";
			contentPanel.addSpace(10);
			IGridPanel grid = contentPanel.add().panel().grid();
			if (saveListener != null) {
				addSaveButton(grid);
			}
			if (hasRequiredAttributes) {
				requiredAttributeLabel = grid.cell(1, 0).align().end().label()
						.text("* Required Attribute");
				requiredAttributeLabel.font().pixel(10);
			}
		}
		return this;
	}

	@Override
	public FormWidgetImpl validate(boolean validate) {
		this.validate = validate;
		return this;
	}

	private void addSaveButton(IGridPanel grid) {
		IHorizontalPanel panel = grid.cell(0, 0).panel().horizontal()
				.spacing(2);
		final IButton clickable = panel.add().button();
		final IButton clickable1 = panel.add().button();
		// TODO un-hack
		if (!saveListener.allowsCancel())
			clickable1.visible(false);
		clickable.text(saveTitle).addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				saveListener.save(new CallbackTemplate<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						validation.update();
						clickable.clickable(false);
						clickable1.clickable(false);
					}
				});
			}
		});
		clickable1.text("Cancel").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				saveListener.cancel(new CallbackTemplate<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						validation.reset();
						clickable.clickable(false);
						clickable1.clickable(false);
					}
				});
			}
		});
		if (validate) {
			validation = new Validation();
			validation.showDiscardChanges();
			validation.linkClickable(clickable);
			validation.linkReset(clickable1);
			for (final FormFieldImpl<?> formField : fields) {
				Object valueElement = formField.valueElement();
				if (valueElement instanceof ITextArea) {
					validation.linkInput((ITextArea) valueElement,
							formField.required);
				} else if (valueElement instanceof ITextField) {
					if (formField.type.clazz.equals(Date.class)) {
						validation.validateDate(
								(ITextField) formField.valueElement(),
								formField.required);
					} else if (formField.type.clazz.equals(Integer.class)) {
						validation.validateLong(
								(ITextField) formField.valueElement(),
								formField.required);
					} else {
						validation.linkInput(
								(ITextField) formField.valueElement(),
								formField.required);
					}
				} else if (valueElement instanceof IPasswordField) {
					validation.linkInput(
							(IPasswordField) formField.valueElement(),
							formField.required);
				} else if (valueElement instanceof ICheckBox) {
					validation.linkInput((ICheckBox) valueElement);
				} else if (valueElement instanceof IComboBox) {
					validation.linkInput((IComboBox) valueElement);
				} else if (valueElement instanceof ILabel) {
				} else
					throw new MethodNotImplementedException(
							valueElement.getClass());
				// for (final IExternalStatusAdapter a :
				// formField.externalStatusAdapters) {
				// validation.addField(new Validation.IField() {
				//
				// @Override
				// public boolean isError() {
				// return false;
				// }
				//
				// @Override
				// public boolean isNull() {
				// return a.isNull();
				// }
				//
				// @Override
				// public boolean required() {
				// return formField.required;
				// }
				//
				// @Override
				// public boolean isSpecified() {
				// return a.hasChanged();
				// }
				//
				// @Override
				// public void update() {
				// }
				// });
				// }
			}
			validation.notifyChange(new CallbackTemplate<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
				}
			});
			if (isNew) {
				validation.isNew();
			}
		}
	}

	public IGridPanel grid() {
		if (gridPanel == null) {
			contentPanel = widgetTitle.content().panel().vertical();
			gridPanel = contentPanel.add().panel().grid();
			gridPanel.indent(2);
			gridPanel.prepare(2, 1).column(1).expand();
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

	@Override
	public IFormWidget saveListener(String title, ISaveListener listener) {
		this.saveTitle = title;
		saveListener = listener;
		return this;
	}

	@Override
	public IFormWidget fixLabelColumn(int width) {
		fixLabelWidth = width;
		return this;
	}

	@Override
	public IFormWidget fixValueColumn(int width) {
		fixValueWidth = width;
		return this;
	}

	@Override
	public ILabel labelRequiredAttribute() {
		return requiredAttributeLabel;
	}

	@Override
	public IFormWidget notifyUpdate() {
		validation.notifyChange(new CallbackTemplate<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
			}
		});
		return this;
	}

	@Override
	public IFormWidget isNew(boolean validate) {
		isNew = validate;
		return this;
	}
}
