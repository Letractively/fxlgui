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
package co.fxl.gui.table.impl;

import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILabel;

class ComparableCellFactory<T> extends CellFactory<ILabel, T> {

	@Override
	public Cell<ILabel> create(TableWidgetImpl table, RowImpl row,
			int columnIndex, final IGridCell cell, T value) {
		ILabel label = cell.label();
		String string = toString(value);
		label.text(string);
		font(label);
		label.autoWrap(true);
		return newLabelCell(row.rowIndex, cell, label);
	}

	Cell<ILabel> newLabelCell(int row, IGridCell cell, ILabel label) {
		return new LabelCell(row, cell, label);
	}

	String toString(T value) {
		if (value == null)
			return "";
		return String.valueOf(value);
	}
}