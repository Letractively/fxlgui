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

import co.fxl.data.format.impl.Format;
import co.fxl.gui.api.IButton;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFocusable;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IGridPanel.IGridColumn;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.api.IImageField;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.WidgetTitle;

class FormWidgetImpl implements IFormWidget {

	// TODO FEATURE: Option: Usability: Integrate an RTF-Editor, e.g.
	// http://gwt.google.com/samples/Showcase/Showcase.html#!CwRichText, or
	// implement own RTF-Editor using toggle-buttons that writes html with a
	// preview panel

	class FormEntryLabel {

		IGridCell cell;
		ILabel formEntryLabel;

		FormEntryLabel(IGridCell cell, ILabel formEntryLabel) {
			this.cell = cell;
			this.formEntryLabel = formEntryLabel;
		}
	}

	private int gridIndex = -1;
	private WidgetTitle widgetTitle;
	IGridPanel grid;
	private IVerticalPanel contentPanel;
	ISaveListener saveListener = null;
	List<FormFieldImpl<?, ?>> fields = new LinkedList<FormFieldImpl<?, ?>>();
	private String saveTitle;
	private int fixLabelWidth = -1;
	private int fixValueWidth = -1;
	private ILabel requiredAttributeLabel;
	private boolean validate = true;
	Validation validation;
	private Heights heights = new Heights(2);
	private boolean isNew;
	private boolean alwaysAllowCancel = false;
	private IButton saveButton;
	private IFocusable<?> focus = null;
	private List<IFocusable<?>> focusables = new LinkedList<IFocusable<?>>();
	private int spacing = 0;
	private IClickListener saveClickListener;
	private IGridPanel bottomPanel;

	FormWidgetImpl(IContainer panel) {
		widgetTitle = new WidgetTitle(panel.panel(), false)// .grayBackground()
				.commandsOnTop().space(0);
		widgetTitle.foldable(false);
	}

	FormEntryLabel addFormEntryLabel(String name, int gridIndex) {
		IGridPanel grid = grid();
		int column = 0;
		IGridCell cell = grid.cell(column, gridIndex);
		cell.align().end().valign().center();// .height(HEIGHT_CELL);
		if (fixLabelWidth != -1)
			cell.width(fixLabelWidth);
		ILabel formEntryLabel = cell.label();
		formEntryLabel.autoWrap(false);
		formEntryLabel.text(name);
		formEntryLabel.font().pixel(12);
		return new FormEntryLabel(cell, formEntryLabel);
	}

	private IContainer container(int gridIndex) {
		IGridPanel grid = grid();
		int column = 1;
		IGridCell cell = grid.cell(column, gridIndex);
		cell.valign().center();
		if (fixValueWidth != -column)
			cell.width(fixValueWidth);
		heights.decorate(cell);
		gridIndex++;
		return cell;
	}

	ITextField addFormValueTextField(int gridIndex, boolean withFocus) {
		ITextField valuePanel = container(gridIndex).textField();
		heights.decorate(valuePanel);
		valuePanel.editable(saveListener != null);
		if (withFocus)
			setFocus(valuePanel);
		setCRListener(valuePanel);
		return valuePanel;
	}

	private void setFocus(IFocusable<?> f) {
		if (saveListener == null)
			return;
		focusables.add(f);
		if (focus != null)
			return;
		focus = f;
		f.focus(true);
	}

	private void setCRListener(IKeyRecipient<?> kr) {
		if (saveListener == null)
			return;
		kr.addKeyListener(new IClickListener() {
			@Override
			public void onClick() {
				if (saveClickListener != null && saveButton.clickable())
					saveClickListener.onClick();
			}
		}).enter();
	}

	IPasswordField addFormValuePasswordField(int gridIndex) {
		IPasswordField valuePanel = container(gridIndex).passwordField();
		heights.decorate(valuePanel);
		valuePanel.editable(saveListener != null);
		setFocus(valuePanel);
		setCRListener(valuePanel);
		return valuePanel;
	}

	ITextArea addFormValueTextArea(int gridIndex) {
		ITextArea valuePanel = container(gridIndex).textArea();
		valuePanel.editable(saveListener != null);
		setFocus(valuePanel);
		return valuePanel;
	}

	IComboBox addFormValueComboBox(int gridIndex) {
		return addFormValueComboBox(gridIndex, true);
	}

	IComboBox addFormValueComboBox(int gridIndex, boolean withFocus) {
		IComboBox valuePanel = container(gridIndex).comboBox();
		heights.decorate(valuePanel);
		valuePanel.editable(saveListener != null);
		if (withFocus)
			setFocus(valuePanel);
		setCRListener(valuePanel);
		return valuePanel;
	}

	ICheckBox addFormValueCheckBox(int gridIndex) {
		ICheckBox valuePanel = container(gridIndex).checkBox();
		heights.valuePanel(valuePanel);
		valuePanel.editable(saveListener != null);
		setFocus(valuePanel);
		setCRListener(valuePanel);
		return valuePanel;
	}

	ILabel addFormLabel(int gridIndex) {
		IContainer cell = container(gridIndex);
		ILabel label = cell.label();
		heights.decorate(label);
		return label;
	}

	IImage addImage(int gridIndex) {
		return container(gridIndex).panel().horizontal().add().image();
	}

	@Override
	public IFormField<ICheckBox, Boolean> addCheckBox(String name) {
		return new FormCheckBoxImpl(this, nextGridIndex(), name);
	}

	@Override
	public IFormField<ITextArea, String> addTextArea(String name) {
		return new FormTextAreaImpl(this, nextGridIndex(), name);
	}

	@Override
	public IFormField<ITextField, String> addTextField(String name) {
		return new FormTextFieldImpl<String>(this, nextGridIndex(), name);
	}

	@Override
	public IFormField<ITextField, Date> addDateField(String name) {
		return new FormDateFieldImpl(this, nextGridIndex(), name);
	}

	@Override
	public IFormField<ITextField, String> addColorField(String name) {
		return new FormColorFieldImpl(this, nextGridIndex(), name);
	}

	@Override
	public IFormField<IPasswordField, String> addPasswordField(String name) {
		return new FormPasswordFieldImpl(this, nextGridIndex(), name);
	}

	@Override
	public IImageField addImage(String name) {
		return new ImageFieldImpl(this, nextGridIndex(), name);
	}

	private int nextGridIndex() {
		return ++gridIndex;
	}

	@Override
	public IFormField<IComboBox, String> addComboBox(String name) {
		return new FormComboBoxImpl(this, nextGridIndex(), name);
	}

	@Override
	public IFormField<ILabel, String> addLabel(String name) {
		return new FormLabelImpl(this, nextGridIndex(), name);
	}

	@Override
	public IClickable<?> addOKHyperlink() {
		return addHyperlink("OK");
	}

	@Override
	public IClickable<?> addCancelHyperlink() {
		return addHyperlink("Cancel");
	}

	@Override
	public IClickable<?> addHyperlink(String name, IClickListener clickListener) {
		IClickable<?> addHyperlink = widgetTitle.addHyperlink(name);
		addHyperlink.addClickListener(clickListener).mouseLeft();
		return addHyperlink;
	}

	@Override
	public IClickable<?> addHyperlink(String image, String name) {
		IClickable<?> addHyperlink = widgetTitle.addHyperlink(image, name);
		return addHyperlink;
	}

	@Override
	public FormWidgetImpl visible(boolean visible) {
		setUpBottomPanel();
		return this;
	}

	void setUpBottomPanel() {
		if (saveListener != null || hasRequiredAttributes()) {
			if (fields.isEmpty())
				return;
			if ((saveListener != null || hasRequiredAttributes())
					&& bottomPanel == null) {
				contentPanel.addSpace(8);
				bottomPanel = contentPanel.add().panel().grid();
			}
			if (saveListener != null) {
				addSaveCancelButtons(bottomPanel);
			}
			if (hasRequiredAttributes()) {
				if (requiredAttributeLabel == null) {
					int column = 1;
					int gridIndex = 0;
					IGridCell cell = bottomPanel.cell(column, gridIndex);
					cell.align().end();
					ILabel label = cell.label();
					requiredAttributeLabel = label;
					requiredAttributeLabel.text("* Required Attribute");
					requiredAttributeLabel.font().pixel(10);
				}
			} else {
				if (requiredAttributeLabel != null)
					requiredAttributeLabel.remove();
				requiredAttributeLabel = null;
			}
		}
	}

	void updateRequiredStatus(FormFieldImpl<?, ?> field, boolean status) {
		if (!validate)
			return;
		validation.updateInput(field.valueElement(), status);
	}

	private boolean hasRequiredAttributes() {
		for (FormFieldImpl<?, ?> f : fields)
			if (f.required)
				return true;
		return false;
	}

	@Override
	public FormWidgetImpl validate(boolean validate) {
		this.validate = validate;
		return this;
	}

	private void addSaveCancelButtons(IGridPanel grid) {
		int column = 0;
		int gridIndex = 0;
		IGridCell cell = grid.cell(column, gridIndex);
		IHorizontalPanel panel = cell.panel().horizontal().add().panel()
				.horizontal().spacing(2);
		saveButton = panel.add().button();
		final IButton clickable1 = panel.add().button();
		// TODO un-hack
		if (!saveListener.allowsCancel())
			clickable1.visible(false);
		saveClickListener = new IClickListener() {
			@Override
			public void onClick() {
				saveListener.save(new CallbackTemplate<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if (!result)
							return;
						validation.update();
						saveButton.clickable(false);
						if (!alwaysAllowCancel)
							clickable1.clickable(false);
					}
				});
			}
		};
		saveButton.text(saveTitle).addClickListener(saveClickListener);
		clickable1.text("Cancel").addClickListener(new LazyClickListener() {
			@Override
			public void onAllowedClick() {
				saveListener.cancel(new CallbackTemplate<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						validation.reset();
						saveButton.clickable(false);
						if (!alwaysAllowCancel)
							clickable1.clickable(false);
					}
				});
			}
		});
		if (validate) {
			validation = new Validation();
			validation.showDiscardChanges();
			validation.linkClickable(saveButton);
			if (!alwaysAllowCancel)
				validation.linkReset(clickable1);
			for (final FormFieldImpl<?, ?> formField : fields) {
				linkInput(formField);
			}
			if (isNew) {
				validation.isNew();
			}
			validation.notifyChange(new CallbackTemplate<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
				}
			});
		}
	}

	void removeInput(final FormFieldImpl<?, ?> formField) {
		if (validation != null)
			validation.removeInput(formField.valueElement());
	}

	void linkInput(final FormFieldImpl<?, ?> formField) {
		Object valueElement = formField.valueElement();
		if (valueElement instanceof ITextArea) {
			validation.linkInput((ITextArea) valueElement, formField.required);
		} else if (valueElement instanceof ITextField) {
			if (formField.type.clazz.equals(Date.class)) {
				if (formField.type.isLong) {
					validation.validateDate(
							(ITextField) formField.valueElement(),
							Format.dateTime(), formField.required);
				} else if (formField.type.isShort) {
					validation.validateDate(
							(ITextField) formField.valueElement(),
							Format.time(), formField.required);
				} else
					validation.validateDate(
							(ITextField) formField.valueElement(),
							formField.required);
			} else if (formField.type.clazz.equals(Integer.class)) {
				validation.validateLong((ITextField) formField.valueElement(),
						formField.required);
			} else {
				validation.linkInput((ITextField) formField.valueElement(),
						formField.required);
			}
		} else if (valueElement instanceof IPasswordField) {
			validation.linkInput((IPasswordField) formField.valueElement(),
					formField.required);
		} else if (valueElement instanceof ICheckBox) {
			validation.linkInput((ICheckBox) valueElement);
		} else if (valueElement instanceof IComboBox) {
			validation.linkInput((IComboBox) valueElement, formField.required);
		} else if (valueElement instanceof ILabel) {
		} else
			throw new MethodNotImplementedException(valueElement.getClass());
	}

	public IGridPanel grid() {
		if (grid == null) {
			contentPanel = widgetTitle.content().panel().vertical()
					.spacing(spacing).add().panel().vertical();
			grid = contentPanel.add().panel().grid();
			grid.indent(1);
			grid.spacing(1);
			grid.resize(2, 1);
			int column = 1;
			expand(column);
		}
		return grid;
	}

	void expand(int column) {
		IGridColumn c = grid.column(column);
		c.expand();
	}

	@Override
	public IClickable<?> addHyperlink(String name) {
		return widgetTitle.addHyperlink(name);
	}

	@Override
	public ILabel addTitle(String title) {
		widgetTitle.space(0);
		spacing = 8;
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

	@Override
	public IFormWidget alwaysAllowCancel() {
		alwaysAllowCancel = true;
		return this;
	}

	@Override
	public IFormWidget clickable(boolean clickable) {
		saveButton.clickable(clickable);
		return this;
	}

	void looseFocus(Object ff) {
		int index = focusables.indexOf(focus);
		focusables.remove(focus);
		if (ff != focus)
			return;
		focus.focus(false);
		focus = null;
		if (index < focusables.size() - 1) {
			focus = focusables.get(index + 1);
		} else if (focusables.size() > 0) {
			focus = focusables.get(0);
		}
		if (focus != null)
			focus.focus(true);
	}

	@Override
	public IFormContainer add(String name) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IFormContainer insert(int index, String name) {
		throw new MethodNotImplementedException();
	}
}
