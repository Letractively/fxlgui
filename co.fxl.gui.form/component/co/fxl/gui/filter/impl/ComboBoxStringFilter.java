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
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.template.Validation;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.impl.Constraint.IStringPrefixConstraint;

class ComboBoxStringFilter extends FilterTemplate<String> {

	class StringConstraint implements IStringPrefixConstraint {

		@Override
		public String column() {
			return name;
		}

		@Override
		public String prefix() {
			return text;
		}
	}

	IComboBox comboBox;
	private String text;

	ComboBoxStringFilter(IGridPanel panel, String name, List<Object> values,
			int filterIndex) {
		super(panel, name, filterIndex);
		comboBox = panel.cell(1, filterIndex)// .width(WIDTH_SINGLE_CELL)
				.comboBox();
		// comboBox.height(HEIGHT);
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
		text = comboBox.text().trim();
		if (text.equals(""))
			text = null;
		return text != null;
	}

	@Override
	public void clear() {
		comboBox.text("");
		text = null;
	}

	@Override
	public boolean applies(String value) {
		return value.startsWith(text);
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
		return new StringConstraint();
	}

	@Override
	public void validate(Validation validation) {
		validation.linkInput(comboBox);
	}

	@Override
	boolean fromConstraint(IFilterConstraints constraints) {
		if (constraints.isAttributeConstrained(name)) {
			String prefix = constraints.stringValue(name);
			comboBox.text(prefix);
			return true;
		} else
			return false;
	}
}
