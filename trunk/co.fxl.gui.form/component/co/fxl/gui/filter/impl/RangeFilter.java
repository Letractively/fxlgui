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
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.filter.impl.FilterPanel.ICell;

abstract class RangeFilter<T> extends FilterTemplate<T> {

	ITextField lowerBoundTextField;
	ITextField upperBoundTextField;
	String lowerBoundText = "";
	String upperBoundText = "";
	private IHorizontalPanel panel;

	RangeFilter(FilterGrid parent, String name, int filterIndex) {
		super(parent, name, filterIndex);
		ICell cell = parent.cell(filterIndex);
		// cell.width(WIDTH_RANGE_CELL);// , HEIGHT);
		panel = cell.horizontal();
		lowerBoundTextField = addTextField(0);
		// lowerBoundTextField.height(HEIGHT);
		panel.addSpace(4);
		panel.add().label().text("-");
		panel.addSpace(4);
		upperBoundTextField = addTextField(2);
		// upperBoundTextField.height(HEIGHT);
	}

	private ITextField addTextField(int column) {
		ITextField textField = panel.add().textField().width(WIDTH_RANGE_CELL)
				.height(HEIGHT);
		return textField;
	}

	public boolean update() {
		lowerBoundText = lowerBoundTextField.text();
		upperBoundText = upperBoundTextField.text();
		return true;
	}

	void setUpperBound(String text) {
		upperBoundTextField.text(text);
		upperBoundText = text;
	}

	void setLowerBound(String text) {
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
}
