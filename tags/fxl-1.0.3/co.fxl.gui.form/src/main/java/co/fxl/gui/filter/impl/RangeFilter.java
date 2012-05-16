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

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterConstraints.IRange;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.filter.impl.FilterPanel.ICell;

abstract class RangeFilter<T> extends FilterTemplate<T> {

	ITextField lowerBoundTextField;
	ITextField upperBoundTextField;
	String lowerBoundText = "";
	String upperBoundText = "";
	private IHorizontalPanel panel;
	private FilterGrid parent;

	RangeFilter(FilterGrid parent, String name, int filterIndex) {
		super(parent, name, filterIndex);
		this.parent = parent;
		ICell cell = parent.cell(filterIndex);
		// cell.width(WIDTH_RANGE_CELL);// , HEIGHT);
		panel = cell.horizontal().add().panel().horizontal();
		lowerBoundTextField = addTextField(0);
		// lowerBoundTextField.height(HEIGHT);
		panel.addSpace(4);
		panel.add().label().text("-");
		panel.addSpace(4);
		upperBoundTextField = addTextField(2);
		// upperBoundTextField.height(HEIGHT);
	}

	private ITextField addTextField(int column) {
		ITextField textField = panel.add().textField().width(WIDTH_RANGE_CELL);
		parent.heights().decorate(textField);
		parent.register(textField);
		return textField;
	}

	public boolean update() {
		lowerBoundText = lowerBoundTextField.text();
		upperBoundText = upperBoundTextField.text();
		return true;
	}

	void setUpperBound(String text) {
		if (text.endsWith(".0"))
			text = text.substring(0, text.length() - 2);
		upperBoundTextField.text(text);
		upperBoundText = text;
	}

	void setLowerBound(String text) {
		if (text.endsWith(".0"))
			text = text.substring(0, text.length() - 2);
		lowerBoundTextField.text(text);
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
				l.onActive(!lowerBoundTextField.text().trim().equals("")
						|| !upperBoundTextField.text().trim().equals(""));
			}
		};
		lowerBoundTextField.addUpdateListener(listener);
		upperBoundTextField.addUpdateListener(listener);
	}

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
