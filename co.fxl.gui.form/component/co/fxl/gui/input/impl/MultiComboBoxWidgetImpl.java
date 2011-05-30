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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.Heights;
import co.fxl.gui.form.impl.Validation;
import co.fxl.gui.form.impl.Validation.IValidation;
import co.fxl.gui.input.api.IMultiComboBoxWidget;

class MultiComboBoxWidgetImpl implements IMultiComboBoxWidget,
		co.fxl.gui.api.IUpdateable.IUpdateListener<Boolean> {

	private List<IUpdateListener<String[]>> listeners = new LinkedList<IUpdateListener<String[]>>();
	private List<String> texts = new LinkedList<String>();
	private List<String> selection = new LinkedList<String>();
	private Heights heights = new Heights(0);
	private ITextField textField;
	private IPopUp popUp;
	private Map<String, ICheckBox> checkBoxes = new HashMap<String, ICheckBox>();

	MultiComboBoxWidgetImpl(IContainer container) {
		textField = container.textField().visible(false);
		heights.decorate(textField);
	}

	@Override
	public void onUpdate(Boolean value) {
		if (value) {
			clearPopUp();
			createPopUp();
			popUp.visible(true);
		}
	}

	@Override
	public IUpdateable<String[]> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String[]> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public IMultiComboBoxWidget selection(String[] selection) {
		setSelection(selection);
		StringBuilder b = new StringBuilder();
		for (String s : selection) {
			if (b.length() > 0)
				b.append(";");
			b.append(s);
		}
		textField.text(b.toString());
		for (IUpdateListener<String[]> l : listeners)
			l.onUpdate(selection());
		return this;
	}

	private void setSelection(String[] selection) {
		this.selection = new LinkedList<String>(Arrays.asList(selection));
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

	private void createPopUp() {
		popUp = textField.display().showPopUp().autoHide(true);
		heights.decorateBorder(popUp);
		int w = Math.min(320, textField.width());
		int h = Math.min(240, 2 + 19 * texts.size());
		popUp.size(w, h);
		popUp.offset(textField.offsetX(),
				textField.offsetY() + textField.height());
		IScrollPane scrollPane = popUp.container().scrollPane();
		scrollPane.border().remove();
		scrollPane.color().white();
		// TODO refine, 16 = hack for GWT
		IVerticalPanel v = scrollPane.viewPort().panel().vertical()
				.width(w - 16);
		for (final String text : texts) {
			ICheckBox cb = v.add().panel().horizontal().align().begin().add()
					.panel().horizontal().align().begin().add().checkBox()
					.text(text);
			checkBoxes.put(text, cb);
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
	}

	private void clearPopUp() {
		if (popUp != null) {
			popUp.visible(false);
			popUp = null;
		}
	}

	@Override
	public IMultiComboBoxWidget width(int width) {
		textField.width(width);
		return this;
	}

	@Override
	public IMultiComboBoxWidget clear() {
		clearPopUp();
		texts.clear();
		selection.clear();
		selection(new String[0]);
		return this;
	}

	@Override
	public IMultiComboBoxWidget visible(boolean visible) {
		textField.visible(visible);
		textField.addFocusListener(this);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				List<String> result = new LinkedList<String>(Arrays
						.asList(extract(value)));
				for (String t : texts) {
					if (!result.contains(t))
						selection.remove(t);
				}
				for (String t : result) {
					if (!selection.contains(t))
						selection.add(t);
				}
				updateCheckBoxes();
			}
		});
		return this;
	}

	private void updateCheckBoxes() {
		for (String t : texts) {
			ICheckBox cb = checkBoxes.get(t);
			if (cb != null)
				cb.checked(selection.contains(t));
		}
	}

	@Override
	public IMultiComboBoxWidget validation(Validation validation) {
		validation.validate(textField, new IValidation<String>() {
			@Override
			public boolean validate(String trim) {
				String[] s = extract(trim);
				List<String> tokens = new LinkedList<String>(texts);
				for (String s0 : s) {
					boolean r = tokens.remove(s0);
					if (!r)
						return false;
				}
				return true;
			}
		});
		return this;
	}

	private String[] extract(String value) {
		return value.split(";");
	}
}
