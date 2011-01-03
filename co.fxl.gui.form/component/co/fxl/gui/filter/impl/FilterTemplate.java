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
import co.fxl.gui.api.ILabel;
import co.fxl.gui.filter.api.IFilterConstraints;

abstract class FilterTemplate<T> implements FilterPart<T> {

	interface FilterListener {
		void onActive(boolean isActive);
	}

	static final int WIDTH_SINGLE_CELL = 180;
	static final int WIDTH_RANGE_CELL = 84;
	static final int HEIGHT = 24;
	String name;

	FilterTemplate(IGridPanel panel, String name, int filterIndex) {
		this.name = name;
		addTitle(panel, name, filterIndex);
	}

	abstract Constraint asConstraint();

	static void addTitle(IGridPanel panel, String name, int filterIndex) {
		ILabel text = panel.cell(0, filterIndex).align().end().valign()
				.center().label();
		text.text(name).font().color().gray();
		int size = 12;
		if (name.length() > 12)
			size = 11;
		if (name.length() > 16)
			size = 10;
		text.font().pixel(size);
	}

	boolean fromConstraint(IFilterConstraints constraints) {
		if (constraints.isAttributeConstrained(name)) {
			throw new MethodNotImplementedException(name);
		} else
			return false;
	}
}