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
package co.fxl.gui.form.impl;

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IGridPanel.IGridRow;
import co.fxl.gui.api.IVerticalPanel;

class FormGridImpl implements FormGrid {

	private IGridPanel grid;

	FormGridImpl(FormWidgetImpl widget, IVerticalPanel content) {
		grid = content.add().panel().grid();
		grid.indent(1);
		grid.spacing(1);
		grid.resize(2, 1);
		grid.column(0).width(120);
		widget.decorate(grid);
	}

	@Override
	public IGridCell label(boolean newLine, int row) {
		return grid.cell(0, row);
	}

	@Override
	public IGridCell value(int row, boolean expand) {
		return grid.cell(1, row);
	}

	private IGridRow row(int row) {
		return grid.row(row);
	}

	@Override
	public IGridPanel grid(int row) {
		return grid;
	}

	@Override
	public void removeRow(int index) {
		row(index).remove();
	}

	@Override
	public void insertRow(boolean newLine, int index) {
		row(index).insert();
	}

	@Override
	public void setWidth4Layout(int setWidth4Layout) {
	}

}
