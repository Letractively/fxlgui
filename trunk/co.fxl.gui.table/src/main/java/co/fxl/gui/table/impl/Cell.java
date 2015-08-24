/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IGridPanel.IGridCell;

abstract class Cell<T> {

	private IGridCell cell;
	T element;

	Cell(int row, IGridCell cell, T element) {
		this.cell = cell;
		IBorder border = cell.border();
		border.color().lightgray();
		border.style().bottom();
		this.element = element;
	}

	abstract void update(Object value);

	void highlight(int row, boolean selected) {

		// TODO doesn't work for IE

		if (!selected)
			cell.color().remove();
		else
			cell.color().rgb(230, 230, 255);
	}

	void visible(boolean visible) {
		cell.visible(visible);
	}
}
