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
package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;

class ScrollTableWidgetImpl implements IScrollTableWidget<Object> {

	// TODO show status line: showing rows 27-40 of 3000

	ScrollTableWidgetImpl(ILayout layout) {
		throw new MethodNotImplementedException();
	}

	@Override
	public int offsetY() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollTableWidget<Object> height(int height) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollTableWidget<Object> rows(IRows<Object> rows) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollTableWidget<Object> visible(boolean visible) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ILabel addTitle(String text) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IClickable<?> addButton(String name) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ISelection<Object> selection() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColumn addColumn() {
		throw new MethodNotImplementedException();
	}
}
