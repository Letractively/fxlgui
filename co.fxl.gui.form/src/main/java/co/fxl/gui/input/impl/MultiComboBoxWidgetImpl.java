/**
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
 *
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.input.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.form.impl.Validation;
import co.fxl.gui.form.impl.Validation.IValidation;
import co.fxl.gui.impl.ComboBoxAdp;
import co.fxl.gui.impl.DummyKeyRecipientKeyTemplate;
import co.fxl.gui.impl.ElementPopUp;
import co.fxl.gui.input.api.IMultiComboBoxWidget;

class MultiComboBoxWidgetImpl extends ComboBoxAdp implements
		IMultiComboBoxWidget,
		co.fxl.gui.api.IUpdateable.IUpdateListener<Boolean> {

	private List<IUpdateListener<String>> listeners = new LinkedList<IUpdateListener<String>>();
	private List<String> texts = new LinkedList<String>();
	private List<String> selection = new LinkedList<String>();
	private ITextField textField;
	private Map<String, ICheckBox> checkBoxes = new HashMap<String, ICheckBox>();
	private ElementPopUp popUp;
	private boolean editable;

	MultiComboBoxWidgetImpl(IContainer container) {
		super(null);
		textField = container.textField();
		textField.addFocusListener(this);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				for (IUpdateListener<String> l : listeners)
					l.onUpdate(value);
				if (!texts.isEmpty()) {
					List<String> result = new LinkedList<String>(Arrays
							.asList(extract(value)));
					boolean changed = false;
					for (String t : texts) {
						if (!result.contains(t)) {
							selection.remove(t);
							changed = true;
						}
					}
					for (String t : result) {
						if (!selection.contains(t) && texts.contains(t)) {
							selection.add(t);
							changed = true;
						}
					}
					if (changed) {
						updateCheckBoxes();
						notifyListeners();
					}
				}
			}
		});
		ElementPopUp.HEIGHTS.decorate(textField);
		popUp = new ElementPopUp(textField).rowHeight(23);
	}

	@Override
	public IBorder border() {
		return textField.border();
	}

	@Override
	public boolean visible() {
		return textField.visible();
	}

	@Override
	public IColor color() {
		return textField.color();
	}

	@Override
	public IComboBox text(String text) {
		textField.text(text);
		return null;
	}

	@Override
	public co.fxl.gui.api.IKeyRecipient.IKey<IComboBox> addKeyListener(
			IClickListener listener) {
		final co.fxl.gui.api.IKeyRecipient.IKey<ITextField> l = textField
				.addKeyListener(listener);
		return new DummyKeyRecipientKeyTemplate<IComboBox>() {
			@Override
			public IComboBox enter() {
				l.enter();
				return null;
			}
		};
	}

	@Override
	public IComboBox height(int h) {
		textField.height(h);
		return this;
	}

	@Override
	public void onUpdate(Boolean value) {
		if (!texts.isEmpty() && value) {
			clearPopUp();
			createPopUp();
			popUp.visible(true);
		}
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
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
		notifyListeners();
		return this;
	}

	private void notifyListeners() {
		for (IUpdateListener<String> l : listeners)
			l.onUpdate(textField.text());
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
		popUp.lines(this.texts.size());
		return this;
	}

	private void createPopUp() {
		if (texts.isEmpty())
			return;
		IVerticalPanel v = popUp.create();
		for (final String text : texts) {
			ICheckBox cb = v.add().panel().horizontal().add().checkBox()
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
		popUp.clear();
	}

	@Override
	public IMultiComboBoxWidget width(int width) {
		textField.width(width);
		return this;
	}

	@Override
	public IMultiComboBoxWidget editable(boolean editable) {
		this.editable = editable;
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
		textField.editable(!texts.isEmpty() || editable);
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

	@Override
	public String text() {
		return textField.text();
	}

	@Override
	public IMultiComboBoxWidget validate(Validation validation) {
		validation.linkInput(textField, false);
		return this;
	}
}
