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
package co.fxl.gui.filter.impl;

import java.util.List;

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.filter.impl.Constraint.ISizeConstraint;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.form.impl.Validation;

class ComboBoxIntegerFilter extends FilterTemplate<Integer> {

	class SizeConstraint implements ISizeConstraint {

		@Override
		public int size() {
			return text;
		}

		@Override
		public String toString() {
			return String.valueOf(text);
		}
	}

	IComboBox comboBox;
	private Integer text;
	private Integer defaultValue;

	ComboBoxIntegerFilter(FilterGrid panel, String name, List<Object> values,
			int filterIndex) {
		super(panel, name, filterIndex);
		this.defaultValue = (Integer) values.get(0);
		comboBox = panel.cell(filterIndex)// .width(WIDTH_SINGLE_CELL)
				.comboBox().width(WIDTH_COMBOBOX_CELL);
		panel.heights().decorate(comboBox);
		for (Object object : values) {
			comboBox.addText(string(object));
		}
	}

	private String string(Object object) {
		if (object == null)
			return "";
		return String.valueOf(object);
	}

	@Override
	public boolean update() {
		String trim = comboBox.text().trim();
		if (trim.equals(""))
			text = defaultValue;
		else
			text = Integer.valueOf(trim);
		return text != null;
	}

	@Override
	public void clear() {
		comboBox.text(String.valueOf(defaultValue));
		text = defaultValue;
	}

	@Override
	public boolean applies(Integer value) {
		return true;
	}

	@Override
	public void addUpdateListener(final FilterListener l) {
		comboBox.addUpdateListener(new IUpdateListener<String>() {

			@Override
			public void onUpdate(String value) {
				l.onActive(!comboBox.text().trim().equals(""));
			}
		});
	}

	@Override
	public Constraint asConstraint() {
		update();
		return new SizeConstraint();
	}

	@Override
	public void validate(Validation validation) {
		validation.linkInput(comboBox);
	}

	void set(int size) {
		comboBox.text(String.valueOf(size));
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		throw new UnsupportedOperationException();
	}
}
