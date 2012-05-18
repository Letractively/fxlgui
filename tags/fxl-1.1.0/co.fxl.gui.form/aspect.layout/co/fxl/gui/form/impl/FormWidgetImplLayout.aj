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
package co.fxl.gui.form.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.layout.impl.Layout;

privileged aspect FormWidgetImplLayout {

	IGridCell around(IGridPanel grid, int column, int gridIndex) : 
	call(IGridCell IGridPanel.cell(int, int)) 
	&& withincode(* FormWidgetImpl.*(..))
	&& args(column, gridIndex) 
	&& target(grid) 
	&& if(Layout.ENABLED) {
		return Layout.instance().form().middleCell(grid, gridIndex, column);
	}

	after(IGridCell cell) : 
	call(public ILabel IContainer.label()) 
	&& withincode(* FormWidgetImpl.*(..)) 
	&& target(cell) 
	&& if(Layout.ENABLED) {
		Layout.instance().form().cell(cell);
	}

	IGridCell around(IGridPanel grid, int column, int gridIndex) : 
	call(IGridCell IGridPanel.cell(int, int)) 
	&& withincode(* FormFieldImpl.*(..))
	&& args(column, gridIndex) 
	&& target(grid) 
	&& if(Layout.ENABLED) {
		return Layout.instance().form().outerCell(grid, gridIndex);
	}

	void around(FormWidgetImpl widget, int column) : 
	call(void FormWidgetImpl.expand(int)) 
	&& withincode(* FormWidgetImpl.*(..))
	&& args(column) 
	&& this(widget) 
	&& if(Layout.ENABLED) {
		Layout.instance().form().grid(widget.grid);
	}
}
