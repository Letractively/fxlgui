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
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterConstraints.IRange;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.filter.impl.FilterPanel.ICell;
import co.fxl.gui.form.impl.Validation;

abstract class RangeFilter<T> extends FilterTemplate<ITextField, T> {

	String lowerBoundText = "";
	String upperBoundText = "";
	protected RangeField panel;

	// private FilterGrid parent;

	RangeFilter(FilterGrid parent, String name, int filterIndex,
			boolean isDateField) {
		super(parent, name, filterIndex);
		// this.parent = parent;
		ICell cell = parent.cell(filterIndex);
		// cell.width(WIDTH_RANGE_CELL);// , HEIGHT);
		panel = cell.horizontal(isDateField);
		// input = addTextField(0);
	}

	// private ITextField addTextField(int column) {
	// ITextField textField = panel.add().textField().width(WIDTH_RANGE_CELL);
	// parent.heights().decorate(textField);
	// parent.register(textField);
	// return textField;
	// }

	public boolean update() {
		lowerBoundText = panel.text();
		upperBoundText = panel.upperBoundText();
		return true;
	}

	void setUpperBound(String text) {
		if (text.endsWith(".0"))
			text = text.substring(0, text.length() - 2);
		panel.upperBoundText(text);
		upperBoundText = text;
	}

	void setLowerBound(String text) {
		if (text.endsWith(".0"))
			text = text.substring(0, text.length() - 2);
		panel.text(text);
		lowerBoundText = text;
	}

	@Override
	public void clear() {
		setLowerBound("");
		setUpperBound("");
	}

	@Override
	public void addUpdateListener(final FilterListener l) {
		IUpdateListener<String> listener = new IUpdateListener<String>() {

			@Override
			public void onUpdate(String value) {
				l.onActive(!panel.text().trim().equals("")
						|| !panel.upperBoundText().trim().equals(""));
			}
		};
		panel.addUpdateListener(listener);
		panel.upperBoundAddUpdateListener(listener);
	}

	@Override
	public void validate(Validation validation) {
		panel.validation(validation, type());
	}

	abstract Class<?> type();

	boolean fromConstraint(IFilterConstraints constraints) {
		if (constraints.isAttributeConstrained(name)) {
			IRange<Double> range = constraints.doubleRange(name);
			if (range.upperBound() != null)
				setUpperBound(String.valueOf(range.upperBound()));
			if (range.lowerBound() != null)
				setLowerBound(String.valueOf(range.lowerBound()));
			return true;
		} else
			return false;
	}
}
