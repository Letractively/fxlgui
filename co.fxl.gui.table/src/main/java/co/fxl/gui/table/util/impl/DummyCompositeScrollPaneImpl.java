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
package co.fxl.gui.table.util.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.impl.ScrollPaneAdp;
import co.fxl.gui.table.util.api.ICompositeScrollPane;

public class DummyCompositeScrollPaneImpl extends ScrollPaneAdp implements
		ICompositeScrollPane {

	private IGridPanel grid;

	public DummyCompositeScrollPaneImpl(IContainer c) {
		super(c.scrollPane().height(1.0));
		grid = element.viewPort().panel().grid().resize(2, 1);
	}

	@Override
	public ICompositeScrollPane height(int height) {
		grid.height(height);
		super.height(height);
		return this;
	}

	@Override
	public IContainer viewPort() {
		return grid.cell(0, 0).valign().begin();
	}

	@Override
	public IContainer right(int width) {
		grid.column(1).width(width);
		return grid.cell(1, 0).width(width).valign().begin();
	}

	@Override
	public ICompositeScrollPane update() {
		return this;
	}

}
