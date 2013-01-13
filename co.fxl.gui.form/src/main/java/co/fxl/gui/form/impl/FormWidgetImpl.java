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
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.FieldTypeImpl;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.StylishButton;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.rtf.api.IHTMLArea;

public class FormWidgetImpl implements IFormWidget {

	private static final boolean USE_BUTTON_PANEL = !Env.is(Env.SWING);

	class FormEntryLabel {

		IGridCell cell;
		ILabel formEntryLabel;

		FormEntryLabel(IGridCell cell, ILabel formEntryLabel) {
			this.cell = cell;
			this.formEntryLabel = formEntryLabel;
		}
	}

	private static final boolean FIXED_WIDTH = Constants.get(
			"FormWidgetImpl.FIXED_WIDTH", false);
	private int gridIndex0 = -1;
	private WidgetTitle widgetTitle;
	protected IGridPanel grid;
	private IVerticalPanel contentPanel;
	ISaveListener saveListener = null;
	List<FormFieldImpl<?, ?>> fields = new LinkedList<FormFieldImpl<?, ?>>();
	private String saveTitle;
	private int fixLabelWidth = -1;
	private int fixValueWidth = -1;
	private ILabel requiredAttributeLabel;
	private boolean validate = true;
	Validation validation;
	Heights heights = new Heights(2);
	private boolean isNew;
	private boolean alwaysAllowCancel = false;
	private StylishButton saveButton;
	private List<IFocusable<?>> focusables = new LinkedList<IFocusable<?>>();
	private int spacing = 0;
	private IClickListener saveClickListener;
	private IGridPanel bottomPanel;
	List<IGridPanel> internalPanels = new LinkedList<IGridPanel>();

	protected FormWidgetImpl(IContainer panel) {
		widgetTitle = new WidgetTitle(panel.panel(), false).commandsOnTop()
				.spacing(0);
		widgetTitle.foldable(false);
	}

	FormEntryLabel addFormEntryLabel(String name, int gridIndex) {
		IGridPanel grid = grid();
		int column = 0;
		IGridCell cell = grid.cell(column, gridIndex).align().end();
		if (fixLabelWidth != -1)
			cell.width(fixLabelWidth);
		ILabel l = cell.label().autoWrap(FIXED_WIDTH).text(name);
		l.font().pixel(11);
		l.margin().right(4);
		return new FormEntryLabel(cell, l);
	}

	private IContainer container(int gridIndex) {
		IGridPanel grid = grid();
		int column = 1;
		IGridCell cell = grid.cell(column, gridIndex);
		if (fixValueWidth != -column)
			cell.width(fixValueWidth);
		heights.decorate(cell);
		IGridPanel subGrid = cell.panel().grid();
		internalPanels.add(subGrid);
		// gridIndex++;
		IGridCell c = subGrid.cell(0, 0);
		heights.decorate(c);
		return c;
	}

	protected void decorateCell(IGridCell cell) {
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
		decorateCell(grid.cell(1, gridIndex));
		return valuePanel;
	}

	IHTMLArea addFormValueRichTextArea(int gridIndex) {
		IHTMLArea valuePanel = container(gridIndex).widget(IHTMLArea.class);
		valuePanel.editable(saveListener != null);
		setFocus(valuePanel);
		decorateCell(grid.cell(1, gridIndex));
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
		return ++gridIndex0;
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
		focus();
		return this;
	}

	private boolean bottomPanelIsSetUp;
	private IVerticalPanel buttonPanel;
	private int buttonPanelIndent;
	private IHorizontalPanel buttonPanelWithIndent;
	List<IResizeListener> resizeListeners = new LinkedList<IResizeListener>();

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
		// panel.addSpace(4).add().label().text("-").font().color().gray();
		final ILabel cb = panel.addSpace(8).add().label()
				.text("Cancel and reset").hyperlink();
		// cb.margin().top(1);
		cb.font().pixel(11);
		// HyperlinkDecorator.styleHyperlinkActive(cb);
		// @SuppressWarnings("rawtypes")
		final IClickable<?> cancelButton = cb;// new IClickable() {
		//
		// @Override
		// public Object clickable(boolean clickable) {
		// cb.clickable(clickable);
		// if (clickable)
		// HyperlinkDecorator.styleHyperlinkActive(cb);
		// else
		// HyperlinkDecorator.styleHyperlinkInactive(cb);
		// cb.font().underline(clickable);
		// return this;
		// }
		//
		// @Override
		// public boolean clickable() {
		// return cb.clickable();
		// }
		//
		// @Override
		// public IKey addClickListener(IClickListener clickListener) {
		// return cb.addClickListener(clickListener);
		// }
		//
		// };
		// new HyperlinkMouseOverListener(cancelButton);
		addSaveButton(subPanel, cancelButton, cb);
		cancelButton.addClickListener(new LazyClickListener() {
			@Override
			public void onAllowedClick() {
				saveListener.cancel(new CallbackTemplate<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						validation.reset();
						saveButton.clickable(false);
						if (!alwaysAllowCancel)
							cancelButton.clickable(false);
					}
				});
			}
		});
		if (validate) {
			validation = new Validation();
			validation.showDiscardChanges();
			validation.linkClickable(saveButton);
			if (!alwaysAllowCancel)
				validation.linkReset(cancelButton);
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
					.begin().addSpace(10);
			buttonPanelWithIndent.margin().left(buttonPanelIndent);
			return buttonPanelWithIndent;
		}
		int column = 0;
		int gridIndex = 0;
		IGridCell cell = grid.cell(column, gridIndex);
		cell.clear();
		IHorizontalPanel panel = cell.panel().horizontal().spacing(2);
		return panel;
	}

	private void addSaveButton(final IHorizontalPanel subPanel,
			final IClickable<?> cancelButton,
			final IElement<?> cancelButtonElement) {
		saveButton = new StylishButton(subPanel.add().panel().horizontal(),
				saveTitle, true, 3, false);
		// new HyperlinkMouseOverListener(saveButton);
		if (validation != null)
			validation.setClickable(saveButton);
		if (!saveListener.allowsCancel())
			cancelButtonElement.visible(false);
		saveClickListener = new IClickListener() {
			@Override
			public void onClick() {
				// final boolean clickable = saveButton.clickable();
				// ILabel text = subPanel.add().label().text("Saving");
				// text.margin().right(8);
				// text.font().pixel(10);
				IElement<?> iElement = (IElement<?>) saveButton.panel();
				int h = (iElement.height() - 11) / 2;
				int v = (iElement.width() - 16) / 2;
				// subPanel.color().white();
				// subPanel.border().color().lightgray();
				subPanel.add().image().resource("saving.gif").margin().left(v)
						.right(v + (iElement.width() % 2)).top(h).bottom(h);
				iElement.remove();
				saveListener.save(new CallbackTemplate<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						resetButton();
						if (!result)
							return;
						validation.update();
						saveButton.clickable(false);
						if (!alwaysAllowCancel)
							cancelButton.clickable(false);
					}

					private void resetButton() {
						// subPanel.color().remove();
						// subPanel.border().remove();
						addSaveButton(subPanel.clear(), cancelButton,
								cancelButtonElement);
					}

					@Override
					public void onFail(Throwable throwable) {
						resetButton();
						super.onFail(throwable);
					}
				});
			}
		};
		saveButton.addClickListener(saveClickListener);
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

	IGridPanel grid() {
		if (grid == null) {
			contentPanel = widgetTitle.content().panel().vertical()
					.spacing(spacing);
			grid = contentPanel.add().panel().grid();
			grid.indent(1);
			grid.spacing(1);
			grid.resize(2, 1);
			int column = 1;
			if (FIXED_WIDTH)
				grid.column(0).width(120);
			else
				expand(column);
			decorate(grid);
		}
		return grid;
	}

	protected void decorate(IGridPanel grid) {
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
		widgetTitle.spacing(0);
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
		if (validation != null)
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
	public IFormContainer add(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFormContainer insert(int index, String name) {
		throw new UnsupportedOperationException();
	}

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
				if (((IEditable<?>) f).editable()) {
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
			buttonPanelWithIndent.margin().left(buttonPanelIndent);
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
		widgetTitle.addLargeTitle(string);
		return this;
	}
}
