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
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.Heights;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.input.api.IMultiComboBoxWidget;

class MultiComboBoxWidgetImpl implements IMultiComboBoxWidget, IClickListener {

	private List<IUpdateListener<String[]>> listeners = new LinkedList<IUpdateListener<String[]>>();
	private IVerticalPanel panel;
	private ILabel label;
	private List<String> texts = new LinkedList<String>();
	private List<String> selection = new LinkedList<String>();
	private Heights heights = new Heights(0);
	private int width = -1;

	MultiComboBoxWidgetImpl(IContainer container) {
		panel = container.panel().vertical();
		panel.spacing(4);
		heights.decorate(panel);
		WidgetTitle.decorateBorder(panel.border().color());
		label = panel.add().label();
		panel.addClickListener(this);
		panel.tooltip("Click to edit");
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
		label.text(this.selection.toString().substring(1,
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
		final IPopUp popUp = label.display().showPopUp();
		popUp.offset(panel.offsetX(), panel.offsetY() + panel.height());
		IScrollPane scrollPane = popUp.container().scrollPane();
		scrollPane.width(width != -1 ? width : panel.width());
		scrollPane
				.height(Math.min(300, texts.size() * Heights.COMBOBOX_HEIGHT));
		scrollPane.color().white();
		IVerticalPanel v = scrollPane.viewPort().panel().vertical();
		for (final String text : texts) {
			ICheckBox cb = v.add().checkBox().text(text);
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
		return this;
	}
}
