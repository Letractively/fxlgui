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
package co.fxl.gui.table.filter.impl;

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.table.filter.api.IRowModel.Constraint;

abstract class FilterTemplate<T> implements IFilterPart<T>, Constraint {

	interface FilterListener {
		void onActive(boolean isActive);
	}

	static final int HEIGHT = 20;
	int columnIndex;
	IGridPanel panel;

	FilterTemplate(int columnIndex, IGridPanel panel, String name,
			int filterIndex) {
		this.columnIndex = columnIndex;
		addTitle(panel, name, filterIndex);
		this.panel = panel.cell(1, filterIndex).panel().grid();
	}

	@Override
	public int columnIndex() {
		return columnIndex;
	}

	static void addTitle(IGridPanel panel, String name, int filterIndex) {
		IGridCell cell = panel.cell(0, filterIndex);
		cell.align().end();
		cell.label().text(name).font().color().gray();
	}
}