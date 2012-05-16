/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.filter.impl;

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.filter.impl.FilterPanel.ICell;

class CellImpl implements ICell {

	private final FilterPanelImpl filterPanelImpl;
	private IGridCell cell;

	CellImpl(FilterPanelImpl filterPanelImpl, IGridCell cell) {
		this.filterPanelImpl = filterPanelImpl;
		this.filterPanelImpl.widget.heights.decorate(cell);
		this.cell = cell;
	}

	@Override
	public IComboBox comboBox() {
		return cell.comboBox();
	}

	@Override
	public IHorizontalPanel horizontal() {
		return cell.panel().horizontal();
	}

	@Override
	public ITextField textField() {
		return cell.textField();
	}

	@Override
	public IDockPanel dock() {
		return cell.panel().dock();
	}
}