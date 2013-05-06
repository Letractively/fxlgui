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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.filter.impl.FilterPanel.ICell;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.Heights;

class FilterGridImpl implements FilterGrid {

	private static final boolean ACTIVE_SET_WIDTH_COLUMN_1 = Env.is(Env.SWING);
	private final FilterPanelImpl filterPanelImpl;
	private IGridPanel grid;

	FilterGridImpl(FilterPanelImpl filterPanelImpl, IContainer gridContainer) {
		this.filterPanelImpl = filterPanelImpl;
		grid = gridContainer.panel().vertical().align().end().spacing(5).add()
				.panel().grid().spacing(3);
		if (ACTIVE_SET_WIDTH_COLUMN_1)
			grid.column(1).width(FilterTemplate.WIDTH_SINGLE_CELL);
	}

	@Override
	public ICell cell(int row) {
		return new CellImpl(this.filterPanelImpl, grid.cell(1, row));
	}

	@Override
	public void updateFilters() {
		filterPanelImpl.updateFilters();
	};

	@Override
	public void title(int filterIndex, String name) {
		IGridCell cell = grid.cell(0, filterIndex);// .width(50);
		this.filterPanelImpl.widget.heights.decorate(cell);

		// TODO SWING-FXL: Look: Align filter labels to the right, grid cell
		// alignment ist of no effect in Swing - in contrast to the GWT
		// implementation, there it works

		ILabel text = cell.align().end().label().autoWrap(true);
		text.text(name).font().pixel(10);
	}

	@Override
	public void register(ITextField tf) {
		this.filterPanelImpl.widget.register(tf);
	}

	@Override
	public Heights heights() {
		return this.filterPanelImpl.widget.heights;
	}

	@Override
	public void resize(int size) {
		grid.resize(2, size);
	}

	@Override
	public void show(FilterPart<?> firstConstraint) {
	}

	@Override
	public FilterGrid rowInc(int rowInc) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addTooltips() {
		return true;
	}

	@Override
	public void notifyComboBoxChange(boolean clickableClear) {
		filterPanelImpl.widget.notifyComboBoxChange(clickableClear);
	}

	@Override
	public void clearRowIterator() {
		filterPanelImpl.clearRowIterator();
	}
}