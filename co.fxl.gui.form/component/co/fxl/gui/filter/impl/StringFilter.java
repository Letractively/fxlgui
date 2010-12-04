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

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.template.Validation;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.impl.Constraint.IStringPrefixConstraint;

class StringFilter extends FilterTemplate<String> {

	class StringPrefixConstraint implements IStringPrefixConstraint {

		@Override
		public String column() {
			return name;
		}

		@Override
		public String prefix() {
			return text;
		}
	}

	ITextField textField;
	private String text;

	StringFilter(IGridPanel panel, String name, int filterIndex) {
		super(panel, name, filterIndex);
		textField = panel.cell(1, filterIndex).textField();
		textField.size(WIDTH_SINGLE_CELL, HEIGHT);
	}

	@Override
	public boolean update() {
		text = textField.text().trim();
		if (text.equals(""))
			text = null;
		return text != null;
	}

	@Override
	public void clear() {
		textField.text("");
		text = null;
	}

	@Override
	public boolean applies(String value) {
		return value.startsWith(text);
	}

	@Override
	public void addUpdateListener(final FilterListener l) {
		textField.addUpdateListener(new IUpdateListener<String>() {

			@Override
			public void onUpdate(String value) {
				l.onActive(!textField.text().trim().equals(""));
			}
		});
	}

	@Override
	public Constraint asConstraint() {
		update();
		return new StringPrefixConstraint();
	}

	@Override
	public void validate(Validation validation) {
		validation.linkInput(textField);
	}

	@Override
	boolean fromConstraint(IFilterConstraints constraints) {
		if (constraints.isAttributeConstrained(name)) {
			String prefix = constraints.stringValue(name);
			textField.text(prefix);
			return true;
		} else
			return false;
	}
}
