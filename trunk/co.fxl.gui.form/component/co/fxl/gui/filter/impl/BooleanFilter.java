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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.filter.api.IFilterConstraints;

class BooleanFilter extends ComboBoxStringFilter {

	private static final String FALSE = "no";
	private static final String TRUE = "yes";

	BooleanFilter(IGridPanel panel, String name, int filterIndex) {
		super(panel, name, values(), filterIndex);
	}

	private static List<Object> values() {
		List<Object> values = new LinkedList<Object>();
		values.add("");
		values.add(TRUE);
		values.add(FALSE);
		return values;
	}

	@Override
	boolean fromConstraint(IFilterConstraints constraints) {
		if (constraints.isAttributeConstrained(name)) {
			Boolean b = constraints.booleanValue(name);
			comboBox.text(b ? TRUE : FALSE);
			return true;
		} else
			return false;
	}

}
