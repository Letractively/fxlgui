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

import co.fxl.gui.api.IElement;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.impl.SplitLayout;

abstract class FilterTemplate<R extends IElement<R>, T> implements
		FilterPart<T> {

	interface FilterListener {
		void onActive(boolean isActive);
	}

	R input;
	static final int WIDTH_SINGLE_CELL = 200 - SplitLayout.DECREMENT;
	static final int WIDTH_COMBOBOX_CELL = 192 - SplitLayout.DECREMENT;
	static final int WIDTH_RANGE_CELL = 98 - (SplitLayout.DECREMENT / 2);
	String name;

	FilterTemplate(FilterGrid panel, String name, int filterIndex) {
		this.name = name;
		addTitle(panel, name, filterIndex);
	}

	abstract IFilterConstraint asConstraint();

	static void addTitle(FilterGrid panel, String name, int filterIndex) {
		panel.title(filterIndex, name);
	}

	boolean fromConstraint(IFilterConstraints constraints) {
		if (constraints.isAttributeConstrained(name)) {
			throw new UnsupportedOperationException(name);
		} else
			return false;
	}

	@Override
	public String name() {
		return name;
	}
}