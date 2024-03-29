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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.data.format.api.IFormat;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IEditable;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFocusable;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.api.IRelationField;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.ComboBox;
import co.fxl.gui.impl.ComboBox.IColorAdapter;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.FieldTypeImpl;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.impl.RuntimeConstants;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.input.api.IMultiComboBoxWidget;
import co.fxl.gui.rtf.api.IHTMLArea;
import co.fxl.gui.rtf.api.ITemplateTextField;
import co.fxl.gui.style.impl.Style;

public class FormWidgetImpl implements IFormWidget, RuntimeConstants {

	private static final boolean USE_BUTTON_PANEL = NOT_SWING;
	private static final boolean ALLOW_MULTI_COLUMNS = NOT_SWING;

	class FormEntryLabel {

		IGridCell cell;
		ILabel formEntryLabel;

		FormEntryLabel(IGridCell cell, ILabel formEntryLabel) {
			this.cell = cell;
			this.formEntryLabel = formEntryLabel;
		}
	}

	private int gridIndex0 = -1;
	private WidgetTitle widgetTitle;
	protected FormGrid grid;
	private IVerticalPanel contentPanel;
	ISaveListener saveListener = null;
	List<FormFieldImpl<?, ?>> fields = new LinkedList<FormFieldImpl<?, ?>>();
	String saveTitle;
	private int fixLabelWidth = -1;
	private int fixValueWidth = -1;
	private ILabel requiredAttributeLabel;
	private boolean validate = true;
	Validation validation;
	Heights heights = new Heights(2 + (IE_LEQ_8 ? 2 : 0));
	boolean isNew;
	boolean alwaysAllowCancel = false;
	private List<IFocusable<?>> focusables = new LinkedList<IFocusable<?>>();
	SaveButtonPanel saveButton;
	private IGridPanel bottomPanel;
	List<IGridPanel> internalPanels = new LinkedList<IGridPanel>();

	protected FormWidgetImpl(IContainer panel) {
		widgetTitle = new WidgetTitle(panel.panel(), false).commandsOnTop()
				.spacing(0);
		widgetTitle.foldable(false);
	}

	FormEntryLabel addFormEntryLabel(boolean newLine, String name, int gridIndex) {
		FormGrid grid = grid();
		IGridCell cell = grid.label(newLine, gridIndex).align().end();
		if (fixLabelWidth != -1)
			cell.width(fixLabelWidth);
		ILabel l = cell.label().text(name);
		Style.instance().form().label(l);
		return new FormEntryLabel(cell, l);
	}

	private IContainer container(int gridIndex, boolean expand) {
		FormGrid grid = grid();
		IGridCell cell = grid.value(gridIndex, expand);
		if (fixValueWidth != -1)
			cell.width(fixValueWidth);
		heights.decorate(cell);
		IGridPanel subGrid = cell.panel().grid();
		internalPanels.add(subGrid);
		// gridIndex++;
		IGridCell c = subGrid.cell(0, 0);
		heights.decorate(c);
		return c;
	}

	protected void decorateCell(IGridPanel grid, IGridCell cell) {
	}

	ITextField addFormValueTextField(int gridIndex, boolean withFocus,
			boolean expand, boolean template) {
		IContainer container = container(gridIndex, expand);
		ITextField valuePanel = template ? container
				.widget(ITemplateTextField.class) : container.textField();
		heights.decorate(valuePanel);
		Style.instance().form().inputField(valuePanel);
		valuePanel.editable(saveListener != null);
		if (withFocus)
			setFocus(valuePanel);
		setCRListener(valuePanel);
		return valuePanel;
	}

	ISuggestField addFormValueSuggestField(int gridIndex) {
		ISuggestField valuePanel = container(gridIndex, false).suggestField()
				.autoSelect(true).requestOnFocus(true);
		heights.decorate(valuePanel);
		Style.instance().form().inputField(valuePanel);
		valuePanel.editable(saveListener != null);
		setCRListener(valuePanel);
		return valuePanel;
	}

	private void setFocus(IFocusable<?> f) {
		if (saveListener == null)
			return;
		focusables.add(f);
	}

	private void setCRListener(IKeyRecipient<?> kr) {
		if (saveListener == null)
			return;
		kr.addKeyListener(new IClickListener() {
			@Override
			public void onClick() {
				saveButton.onCR();
			}
		}).enter();
	}

	IPasswordField addFormValuePasswordField(int gridIndex) {
		IPasswordField valuePanel = container(gridIndex, false).passwordField();
		heights.decorate(valuePanel);
		Style.instance().form().inputField(valuePanel);
		valuePanel.editable(saveListener != null);
		setFocus(valuePanel);
		setCRListener(valuePanel);
		return valuePanel;
	}

	ITextArea addFormValueTextArea(int gridIndex, IInputElementFactory f) {
		IContainer container = container(gridIndex, true);
		ITextArea valuePanel = f == null ? container.textArea() : (ITextArea) f
				.create(container, null);
		Style.instance().form().inputField(valuePanel);
		valuePanel.editable(saveListener != null);
		setFocus(valuePanel);
		decorateCell(grid.grid(gridIndex), grid.value(gridIndex, true));
		return valuePanel;
	}

	IHTMLArea addFormValueRichTextArea(int gridIndex) {
		IHTMLArea valuePanel = container(gridIndex, true).widget(
				IHTMLArea.class);
		Style.instance().form().inputField(valuePanel);
		valuePanel.editable(saveListener != null);
		setFocus(valuePanel);
		decorateCell(grid.grid(gridIndex), grid.value(gridIndex, true));
		return valuePanel;
	}

	IComboBox addFormValueComboBox(int gridIndex) {
		return addFormValueComboBox(gridIndex, true, null, null);
	}

	IComboBox addFormValueComboBox(int gridIndex, boolean withFocus,
			IFieldType type, IColorAdapter ca) {
		IContainer container = container(gridIndex, false);
		IComboBox valuePanel = type != null && type.multiSelectionAllowed() ? container
				.widget(IMultiComboBoxWidget.class)
				: (ca != null ? ComboBox.create(container, ca, false)
						: container.comboBox());
		heights.decorate(valuePanel);
		Style.instance().form().inputField(valuePanel);
		valuePanel.editable(saveListener != null);
		if (withFocus)
			setFocus(valuePanel);
		setCRListener(valuePanel);
		return valuePanel;
	}

	ICheckBox addFormValueCheckBox(int gridIndex) {
		ICheckBox valuePanel = container(gridIndex, false).checkBox();
		heights.valuePanel(valuePanel);
		valuePanel.editable(saveListener != null);
		// setFocus(valuePanel);
		setCRListener(valuePanel);
		return valuePanel;
	}

	ILabel addFormLabel(int gridIndex) {
		IContainer cell = container(gridIndex, false);
		ILabel label = cell.label();
		heights.decorate(label);
		return label;
	}

	IImage addImage(int gridIndex) {
		return container(gridIndex, false).panel().horizontal().add().image();
	}

	@Override
	public IFormField<ICheckBox, Boolean> addCheckBox(boolean newLine,
			String name) {
		return new FormCheckBoxImpl(newLine, this, nextGridIndex(), name);
	}

	@Override
	public IFormField<ITextArea, String> addTextArea(String name) {
		return new FormTextAreaImpl(this, nextGridIndex(), name, null);
	}

	@Override
	public IFormField<ITextArea, String> addTextArea(String name,
			IInputElementFactory f) {
		return new FormTextAreaImpl(this, nextGridIndex(), name, f);
	}

	@Override
	public IFormField<ITextField, String> addTextField(boolean newLine,
			String name, IFieldType type) {
		return new FormTextFieldImpl<String>(newLine, this, nextGridIndex(),
				name, type);
	}

	@Override
	public IRelationField addRelationField(boolean newLine, String name) {
		return new FormRelationFieldImpl(newLine, this, nextGridIndex(), name);
	}

	@Override
	public IFormField<ITextField, Date> addDateField(boolean newLine,
			String name, boolean addCalendar, IFormat<Date> f) {
		return new FormDateFieldImpl(newLine, this, nextGridIndex(), name,
				addCalendar, f);
	}

	@Override
	public IFormField<ITextField, String> addColorField(boolean newLine,
			String name) {
		return new FormColorFieldImpl(newLine, this, nextGridIndex(), name);
	}

	@Override
	public IFormField<IPasswordField, String> addPasswordField(boolean newLine,
			String name) {
		return new FormPasswordFieldImpl(newLine, this, nextGridIndex(), name);
	}

	// @Override
	// public IImageField addImage(String name) {
	// return new ImageFieldImpl(this, nextGridIndex(), name);
	// }

	private int nextGridIndex() {
		return ++gridIndex0;
	}

	@Override
	public IFormField<IComboBox, String> addComboBox(boolean newLine,
			String name, IFieldType type, IColorAdapter ca) {
		return new FormComboBoxImpl(newLine, this, nextGridIndex(), name, type,
				ca);
	}

	// @Override
	// public IFormField<ILabel, String> addLabel(String name) {
	// return new FormLabelImpl(this, nextGridIndex(), name);
	// }

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
		focus();
		if (validation != null)
			validation.activate();
		return this;
	}

	private boolean bottomPanelIsSetUp;
	private IVerticalPanel buttonPanel;
	private int buttonPanelIndent;
	private IHorizontalPanel buttonPanelWithIndent;
	List<IResizeListener> resizeListeners = new LinkedList<IResizeListener>();
	private int setWidth4Layout;

	void setUpBottomPanel() {
		if (bottomPanelIsSetUp)
			return;
		bottomPanelIsSetUp = true;
		if (saveListener != null || hasRequiredAttributes()) {
			if (fields.isEmpty())
				return;
			if ((saveListener != null || hasRequiredAttributes())
					&& bottomPanel == null) {
				bottomPanel = contentPanel.add().panel().grid();
				bottomPanel.margin().top(8);
			}
			if (saveListener != null) {
				addSaveCancelButtons(bottomPanel.clear());
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
		if (!validate || validation == null)
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
		IHorizontalPanel panel = getButtonPanel(grid);
		final IHorizontalPanel subPanel = panel.add().panel().horizontal()
				.align().begin();
		subPanel.margin().top(1);
		final CancelButtonPanel cb = new CancelButtonPanel(this, panel);
		addSaveButton(subPanel, cb);
		if (validate) {
			validation = new Validation();
			validation.showDiscardChanges();
			validation.linkClickable(saveButton);
			if (!alwaysAllowCancel) {
				validation.linkReset(cb);
			}
			for (final FormFieldImpl<?, ?> formField : fields) {
				if (formField.validate) {
					linkInput(formField);
				}
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

	private IHorizontalPanel getButtonPanel(IGridPanel grid) {
		if (buttonPanel != null && USE_BUTTON_PANEL) {
			IHorizontalPanel begin = buttonPanel.clear().add().panel()
					.horizontal().width(1.0).spacing(4).align().begin();
			buttonPanelWithIndent = begin.add().panel().horizontal().align()
					.begin();// .addSpace(10);
			buttonPanelWithIndent.margin().left(buttonPanelIndent - 5);
			return buttonPanelWithIndent;
		}
		int column = 0;
		int gridIndex = 0;
		IGridCell cell = grid.cell(column, gridIndex);
		cell.clear();
		IHorizontalPanel panel = cell.panel().horizontal().spacing(2);
		return panel;
	}

	SaveButtonPanel addSaveButton(final IHorizontalPanel subPanel,
			CancelButtonPanel cancelButtonElement) {
		saveButton = new SaveButtonPanel(this, subPanel, cancelButtonElement);
		// new HyperlinkMouseOverListener(saveButton);
		if (validation != null)
			validation.setClickable(saveButton);
		if (!saveListener.allowsCancel())
			cancelButtonElement.visible(false);
		return saveButton;
	}

	void removeInput(final FormFieldImpl<?, ?> formField) {
		if (validation != null)
			validation.removeInput(formField.valueElement());
	}

	void linkInput(final FormFieldImpl<?, ?> formField) {
		if (validation == null)
			return;
		Object valueElement = formField.valueElement();
		boolean required = formField.required;
		FieldTypeImpl type = formField.type;
		validation.validate(valueElement, required, type, formField.maxLength);
	}

	FormGrid grid() {
		if (grid == null) {
			contentPanel = widgetTitle.content().panel().vertical();
			grid = setWidth4Layout <= 0 || !ALLOW_MULTI_COLUMNS ? new FormGridImpl(
					this, contentPanel) : new FlowFormGrid(this, contentPanel);
			grid.setWidth4Layout(setWidth4Layout);
		}
		return grid;
	}

	// void expand(int column) {
	// IGridColumn c = grid.column(column);
	// c.expand();
	// }

	@Override
	public IClickable<?> addHyperlink(String name) {
		return widgetTitle.addHyperlink(name);
	}

	@Override
	public ILabel addTitle(String title) {
		prepareAddTitle();
		return widgetTitle.addTitle(title);
	}

	void prepareAddTitle() {
		widgetTitle.spacing(0);
		// spacing = 8;
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
		if (validation != null)
			validation.notifyChange(new DummyCallback<Boolean>());
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

	// void looseFocus(Object ff) {
	// int index = focusables.indexOf(focus);
	// focusables.remove(focus);
	// if (ff != focus)
	// return;
	// focus.focus(false);
	// focus = null;
	// if (index < focusables.size() - 1) {
	// focus = focusables.get(index + 1);
	// } else if (focusables.size() > 0) {
	// focus = focusables.get(0);
	// }
	// if (focus != null)
	// focus.focus(true);
	// }

	@Override
	public IFormField<IHTMLArea, String> addRichTextArea(String name) {
		return new FormRichTextAreaImpl(this, nextGridIndex(), name);
	}

	protected void prepareButtonColumn(IGridPanel grid, int column) {
	}

	@Override
	public IFormWidget focus() {
		for (IFocusable<?> f : focusables)
			if (f instanceof IEditable) {
				if (((IEditable<?>) f).editable()
						&& ((IElement<?>) f).visible()) {
					f.focus(true);
					break;
				}
			}
		return this;
	}

	@Override
	public WidgetTitle widgetTitle() {
		return widgetTitle;
	}

	@Override
	public IFormWidget buttonPanel(IVerticalPanel bottom) {
		if (bottom != null)
			buttonPanel = bottom.align().center();
		return this;
	}

	@Override
	public IFormWidget buttonPanelIndent(int buttonPanelIndent) {
		this.buttonPanelIndent = buttonPanelIndent;
		if (buttonPanelWithIndent != null)
			buttonPanelWithIndent.margin().left(buttonPanelIndent - 5);
		return this;
	}

	@Override
	public IFormWidget addResizeListener(
			co.fxl.gui.api.IResizable.IResizeListener listener) {
		resizeListeners.add(listener);
		return this;
	}

	@Override
	public IFormWidget addLargeTitle(String string) {
		prepareAddTitle();
		widgetTitle.addLargeTitle(string);
		return this;
	}

	@Override
	public IFormWidget width(int width) {
		widgetTitle.baseFocusPanel.width(width);
		return this;
	}

	@Override
	public void notifyWidthChange() {
		for (FormFieldImpl<?, ?> f : fields) {
			f.clearTextAreaWidth();
		}
	}

	protected void decorate(IGridPanel grid) {
	}

	@Override
	public void setWidth4Layout(int setWidth4Layout) {
		this.setWidth4Layout = setWidth4Layout;
		if (grid != null)
			grid.setWidth4Layout(setWidth4Layout);
	}
}
