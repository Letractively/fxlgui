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

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.impl.FormWidgetImpl.FormEntryLabel;
import co.fxl.gui.impl.ClickableMultiplexer;
import co.fxl.gui.impl.FieldTypeImpl;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.impl.ITooltipResolver;
import co.fxl.gui.impl.RuntimeConstants;

public abstract class FormFieldImpl<T, R> implements IFormField<T, R>,
		RuntimeConstants {

	private static final int SPACING = IE ? (IE_GEQ_10 ? 3 : 2) : 4;
	FormWidgetImpl widget;
	private ILabel label;
	private IGridCell cell;
	FieldTypeImpl type = new FieldTypeImpl();
	boolean required = false;
	int row;
	private int column = 2;
	private boolean visible = true;
	private String name;
	boolean validate = true;
	int maxLength = -1;
	T valueElement;
	boolean useAssignButton;

	public FormFieldImpl(FormWidgetImpl widget, int index, String name) {
		this(widget, index, name, null);
	}

	public FormFieldImpl(FormWidgetImpl widget, int index, String name,
			IFieldType type) {
		this.widget = widget;
		this.name = name;
		if (type != null)
			this.type = (FieldTypeImpl) type;
		widget.fields.add(this);
		createLabelColumn(index);
		this.row = index;
		createContentColumn(index);
	}

	@Override
	public void useAssignButton() {
		useAssignButton = true;
	}

	@Override
	public final T valueElement() {
		return valueElement;
	}

	void createLabelColumn(int index) {
		FormEntryLabel formEntryLabel = widget.addFormEntryLabel(name, index);
		cell = formEntryLabel.cell;
		label = formEntryLabel.formEntryLabel;
	}

	abstract void createContentColumn(int index);

	@Override
	public IFormField<T, R> validate(boolean validate) {
		this.validate = validate;
		return this;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public IFormField<T, R> editable(boolean editable) {
		if (editable) {
			label.font().color().black();
		} else {
			label.font().color().gray();
		}
		if (valueElement instanceof IColored)
			((IColored) valueElement).color().gray(editable ? 253 : 244);
		// checkFocus(editable);
		return this;
	}

	// void checkFocus(boolean editable) {
	// if (!editable)
	// widget.looseFocus(valueElement());
	// }

	// @Override
	// public ILabel addButton(String title) {
	// ILabel l = addContainer().label().hyperlink().text(title);
	// l.font().pixel(11);
	// return l;
	// }

	@Override
	public IClickable<?> addImage(String resource) {
		Object[] cp = addContainerInPanel(true);
		IImage l = ((IContainer) cp[1]).image().resource(resource);
		return new ClickableMultiplexer((IClickable<?>) cp[0], l);
	}

	@Override
	public IContainer addContainer(boolean decorate) {
		return (IContainer) addContainerInPanel(decorate)[1];
	}

	@Override
	public IContainer addContainer() {
		return addContainer(true);
	}

	private int containerInPanelIndex = 1;

	private Object[] addContainerInPanel(boolean decorate) {
		IGridPanel g = widget.internalPanels.get(row).width(1d);
		g.column(0).expand();
		IGridCell cell2 = g.cell(containerInPanelIndex++, 0).align().center()
				.valign().center();
		if (SWING)
			cell2.align().begin();
		if (decorate) {
			g.column(1).width(24);// - (IE ? 4 : 0));
			widget.heights.decorate(cell2);
		}
		IHorizontalPanel spacing = cell2.panel().horizontal();
		if (decorate) {
			spacing.spacing(SPACING);
			widget.heights.styleColor(spacing);
			IBorder border = spacing.border();
			border.color().rgb(211, 211, 211);
			border.style().bottom().style().top().style().right();
		}
		IContainer c = spacing.add();
		widget.prepareButtonColumn(g, column);
		return new Object[] { spacing, c };
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
		if (showRequiredAsBold()) {
			label.font().weight().bold();
			label.text(name + " *");
		}
		return this;
	}

	boolean showRequiredAsBold() {
		return true;
	}

	@Override
	public IFormField<T, R> setRequired(boolean required) {
		if (this.required == required)
			return this;
		if (required) {
			required();
			widget.setUpBottomPanel();
			widget.updateRequiredStatus(this, required);
		} else {
			required = false;
			label.font().weight().plain();
			label.text(name);
			widget.setUpBottomPanel();
			widget.updateRequiredStatus(this, required);
		}
		this.required = required;
		return this;
	}

	@Override
	public IFieldType type() {
		return type;
	}

	@Override
	public void type(IFieldType type) {
		this.type = (FieldTypeImpl) type;
	}

	@Override
	public void remove() {
		widget.grid.removeRow(getVisibleIndex());
	}

	@SuppressWarnings("unchecked")
	@Override
	public IUpdateable<R> addUpdateListener(IUpdateListener<R> listener) {
		return ((IUpdateable<R>) valueElement()).addUpdateListener(listener);
	}

	@Override
	public boolean visible() {
		return visible;
	}

	@Override
	public boolean visible(boolean visible) {
		if (visible == this.visible)
			return false;
		if (visible)
			widget.linkInput(this);
		else
			widget.removeInput(this);
		int index = getVisibleIndex();
		if (visible) {
			widget.grid.insertRow(index);
			createLabelColumn(index);
			createContentColumn(index);
		} else {
			remove();
		}
		this.visible = visible;
		return true;
	}

	private int getVisibleIndex() {
		int index = 0;
		for (FormFieldImpl<?, ?> ff : widget.fields) {
			if (ff == this)
				return index;
			else {
				if (ff.visible)
					index++;
			}
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public IFormField<T, R> tooltip(ITooltipResolver tooltip) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void maxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	void clearTextAreaWidth() {
	}
}
