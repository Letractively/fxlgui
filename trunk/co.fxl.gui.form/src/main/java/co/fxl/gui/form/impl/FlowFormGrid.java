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
import co.fxl.gui.api.IFlowPanel.ILineBreak;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IPadding;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.RuntimeConstants;

class FlowFormGrid implements FormGrid, RuntimeConstants {

	private static final int _120 = 120;

	private class Row {

		private IGridPanel grid;
		private boolean expand;
		private ILineBreak br;

		private Row(ILineBreak br, IGridPanel g) {
			this.br = br;
			grid = g;
		}

		private void expand(boolean expand) {
			this.expand = expand;
			updateWidth();
		}

		private void updateWidth() {
			IPadding padding = grid.cell(1, 0).padding();
			if (!expand) {
				if (nonExpandedWidth < 0) {
					grid.width(1.0);
					expandIfFirefox();
				} else {
					grid.width(nonExpandedWidth);
					if (FIREFOX)
						grid.column(1).width(nonExpandedWidth - _120 - 4);
				}
				padding.right(0);
			} else {
				expandIfFirefox();
				padding.right(paddingRight);
			}
		}

		void expandIfFirefox() {
			if (FIREFOX) {
				if (width4Layout > 0)
					grid.column(1).width(width4Layout - _120 - 4);
				grid.width(width4Layout);
			}
		}

		public void remove() {
			if (br != null)
				br.remove();
			grid.remove();
		}
	}

	private static final int MIN_WIDTH_COLUMN = 300;
	private static final int SPACING = 4;
	private IFlowPanel panel;
	private List<Row> panels = new LinkedList<Row>();
	private int nonExpandedWidth = MIN_WIDTH_COLUMN;
	private int width4Layout;
	private int paddingRight = 0;

	FlowFormGrid(FormWidgetImpl widget, IVerticalPanel content) {
		panel = content.add().panel().flow().spacing(SPACING);
	}

	@Override
	public void setWidth4Layout(int width4Layout) {
		paddingRight = 0;
		if (width4Layout < 0) {
			this.width4Layout = -1;
			nonExpandedWidth = -1;
		} else {
			this.width4Layout = width4Layout;
			int c = 1;
			while (minWidth(c) < width4Layout)
				c++;
			if (c > 1)
				c--;
			if (c == 1)
				nonExpandedWidth = -1;
			else {
				nonExpandedWidth = MIN_WIDTH_COLUMN
						+ (width4Layout - minWidth(c)) / c;
				paddingRight = width4Layout - nonExpandedWidth * c - (c - 1)
						* SPACING;
			}
		}
		for (Row r : panels) {
			r.updateWidth();
		}
	}

	private int minWidth(int c) {
		return c * MIN_WIDTH_COLUMN + (c + 1) * SPACING;
	}

	@Override
	public IGridCell label(boolean newLine, int row) {
		return gridPanel(newLine, row).cell(0, 0);
	}

	private IGridPanel gridPanel(boolean lineBreak, int row) {
		Row r = row(lineBreak, row);
		return r.grid;
	}

	private Row row(boolean lineBreak, int row) {
		while (row >= panels.size()) {
			ILineBreak br = null;
			if (lineBreak)
				br = panel.addLineBreak();
			IGridPanel g = panel.add().panel().grid();
			if (IE_OR_FIREFOX)
				g.indent(1);
			panels.add(new Row(br, g));
			g.column(0).width(_120);
		}
		Row r = panels.get(row);
		return r;
	}

	@Override
	public IGridCell value(int row, boolean expand) {
		Row r = panels.get(row);
		r.expand(expand);
		return r.grid.cell(1, 0);
	}

	@Override
	public IGridPanel grid(int row) {
		return panels.get(row).grid;
	}

	@Override
	public void removeRow(int index) {
		panels.remove(index).grid.remove();
	}

	@Override
	public void insertRow(boolean newLine, int index) {
		List<Row> toReAdd = new LinkedList<Row>();
		for (int i = panels.size() - 1; i >= index; i--) {
			Row removed = panels.remove(index);
			removed.remove();
			toReAdd.add(removed);
		}
		gridPanel(newLine, index).width(1.0);
		for (Row g : toReAdd) {
			panels.add(g);
			if (g.br != null) {
				g.br = panel.addLineBreak();
			}
			panel.add(g.grid);
		}
	}

}
