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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Env;

class FlowFormGrid implements FormGrid {

	private class Row {

		private IGridPanel grid;
		private boolean expand;

		private Row(IGridPanel g) {
			grid = g;
		}

		private void expand(boolean expand) {
			this.expand = expand;
			updateWidth();
		}

		private void updateWidth() {
			if (!expand) {
				if (nonExpandedWidth < 0)
					grid.width(1.0);
				else
					grid.width(nonExpandedWidth);
			}
		}
	}

	private static final int MIN_WIDTH_COLUMN = 300;
	private static final int SPACING = 4;
	private IFlowPanel panel;
	private List<Row> panels = new LinkedList<Row>();
	private int nonExpandedWidth = MIN_WIDTH_COLUMN;

	FlowFormGrid(FormWidgetImpl widget, IVerticalPanel content) {
		panel = content.add().panel().flow().spacing(SPACING);
	}

	@Override
	public void setWidth4Layout(int width4Layout) {
		if (width4Layout < 0)
			nonExpandedWidth = -1;
		else {
			int c = 1;
			while (minWidth(c) < width4Layout)
				c++;
			if (c > 1)
				c--;
			if (c == 1)
				nonExpandedWidth = -1;
			else
				nonExpandedWidth = MIN_WIDTH_COLUMN
						+ (width4Layout - minWidth(c)) / c;
		}
		for (Row r : panels) {
			r.updateWidth();
		}
	}

	private int minWidth(int c) {
		return c * MIN_WIDTH_COLUMN + (c + 1) * SPACING;
	}

	@Override
	public IGridCell label(int row) {
		return getGridPanel(row).cell(0, 0);
	}

	private IGridPanel getGridPanel(int row) {
		while (row >= panels.size()) {
			IGridPanel g = panel.add().panel().grid();
			if (Env.is(Env.IE))
				g.indent(4);
			panels.add(new Row(g));
			g.column(0).width(120);
		}
		return panels.get(row).grid;
	}

	@Override
	public IGridCell value(int row, boolean expand) {
		Row r = panels.get(row);
		r.expand(expand);
		return r.grid.cell(1, 0);
	}

	@Override
	public IGridPanel grid(int row) {
		return getGridPanel(row);
	}

	@Override
	public void removeRow(int index) {
		panels.get(index).grid.remove();
	}

	@Override
	public void insertRow(int index) {
		List<Row> toReAdd = new LinkedList<Row>();
		for (int i = index; i < panels.size(); i++) {
			Row removed = panels.remove(index);
			removed.grid.remove();
			toReAdd.add(removed);
		}
		getGridPanel(index);
		for (Row g : toReAdd) {
			panels.add(g);
			panel.add(g.grid);
		}
	}

}
