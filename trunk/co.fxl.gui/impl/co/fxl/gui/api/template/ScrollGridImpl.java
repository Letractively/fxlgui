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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridClickListener;

public class ScrollGridImpl extends ScrollGridTemplate {

	public ScrollGridImpl(IContainer container) {
		super(container);
	}

	@Override
	void setUp() {
		grid = scrollPanel.viewPort().panel().grid();
		decorator.decorate(grid);
		scrollPanel.height(height);
		grid.spacing(spacing);
		grid.indent(indent);
		grid.prepare(maxColumns, maxRows);
		for (int row = 0; row < maxRows; row++) {
			for (int column = 0; column < maxColumns; column++) {
				decorator.decorate(column, row, grid.cell(column, row));
			}
		}
		for (IGridClickListener l : listeners.keySet()) {
			String key = listeners.get(l);
			IKey<IGridPanel> keyCB = grid.addGridClickListener(l);
			if (SHIFT.equals(key)) {
				keyCB.shiftPressed();
			} else if (CTRL.equals(key))
				keyCB.ctrlPressed();
		}
	}
}
