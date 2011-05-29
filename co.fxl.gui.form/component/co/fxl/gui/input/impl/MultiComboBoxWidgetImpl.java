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
package co.fxl.gui.input.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.template.Heights;
import co.fxl.gui.input.api.IMultiComboBoxWidget;

class MultiComboBoxWidgetImpl implements IMultiComboBoxWidget, IClickListener,
		co.fxl.gui.api.IUpdateable.IUpdateListener<Boolean> {

	private List<IUpdateListener<String[]>> listeners = new LinkedList<IUpdateListener<String[]>>();
	private List<String> texts = new LinkedList<String>();
	private List<String> selection = new LinkedList<String>();
	private Heights heights = new Heights(0);
	private int width = -1;
	private ITextField textField;

	MultiComboBoxWidgetImpl(IContainer container) {
		textField = container.textField();
		heights.decorate(textField);
		textField.editable(false);
		textField.addFocusListener(this);
		textField.tooltip("Click to edit");
	}

	@Override
	public void onUpdate(Boolean value) {
		textField.editable(!value);
	}

	@Override
	public IUpdateable<String[]> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String[]> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public IMultiComboBoxWidget selection(String[] selection) {
		this.selection = new LinkedList<String>(Arrays.asList(selection));
		textField.text(this.selection.toString().substring(1,
				this.selection.toString().length() - 1));
		for (IUpdateListener<String[]> l : listeners)
			l.onUpdate(selection());
		return this;
	}

	@Override
	public String[] selection() {
		return selection.toArray(new String[0]);
	}

	@Override
	public IMultiComboBoxWidget addText(String... texts) {
		this.texts.addAll(Arrays.asList(texts));
		return this;
	}

	@Override
	public void onClick() {
		final IPopUp popUp = textField.display().showPopUp().autoHide(true);
		popUp.offset(textField.offsetX(),
				textField.offsetY() + textField.height());
		IScrollPane scrollPane = popUp.container().scrollPane();
		int w = width != -1 ? width : textField.width();
		scrollPane.width(Math.max(320, w));
		scrollPane
				.height(Math.min(240, texts.size() * Heights.COMBOBOX_HEIGHT));
		scrollPane.color().white();
		IGridPanel v = scrollPane.viewPort().panel().grid().spacing(0)
				.indent(0);
		int i = 0;
		for (final String text : texts) {
			ICheckBox cb = v.cell(0, i++).height(Heights.COMBOBOX_HEIGHT)
					.align().begin().valign().center().checkBox().text(text);
			cb.addUpdateListener(new IUpdateListener<Boolean>() {
				@Override
				public void onUpdate(Boolean value) {
					String t = text;
					if (value)
						MultiComboBoxWidgetImpl.this.selection.add(t);
					else
						MultiComboBoxWidgetImpl.this.selection.remove(t);
					selection(selection.toArray(new String[0]));
				}
			});
			cb.checked(selection.contains(text));
		}
		popUp.visible(true);
	}

	@Override
	public IMultiComboBoxWidget width(int width) {
		this.width = width;
		textField.width(width);
		return this;
	}

	@Override
	public IMultiComboBoxWidget clear() {
		texts.clear();
		selection.clear();
		selection(new String[0]);
		return this;
	}

	@Override
	public IMultiComboBoxWidget visible(boolean visible) {
		textField.visible(visible);
		return this;
	}
}
