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

import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.impl.Constraint.IStringPrefixConstraint;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.filter.impl.FilterPanel.ICell;
import co.fxl.gui.form.impl.Validation;

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

		@Override
		public String toString() {
			return text;
		}
	}

	ITextField textField;
	private String text;

	StringFilter(FilterGrid panel, String name, int filterIndex) {
		super(panel, name, filterIndex);
		ICell width = panel.cell(filterIndex);// .width(WIDTH_SINGLE_CELL);
		textField = textField(width, filterIndex).width(width());
		panel.heights().decorate(textField);
		panel.register(textField);
		// textField.height(HEIGHT);
	}

	ITextField textField(ICell c, int filterIndex) {
		return c.textField();
	}

	int width() {
		return WIDTH_SINGLE_CELL;
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

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		throw new MethodNotImplementedException();
	}
}
