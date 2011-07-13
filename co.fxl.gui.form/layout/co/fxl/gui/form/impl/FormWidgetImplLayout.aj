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

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IContainer;

privileged aspect FormWidgetImplLayout {

	IGridPanel.IGridCell around(int column, int row) : 
	call(IGridPanel.IGridCell IGridPanel.cell(int, int)) 
	&& withincode(* FormWidgetImpl.*(..))
	&& args(column, row) {
		IGridPanel.IGridCell cell = proceed(0, row * 2 + column);
		return cell;
	}

	after(IGridPanel.IGridCell cell) : 
	call(public ILabel IContainer.label()) 
	&& withincode(* FormWidgetImpl.*(..)) 
	&& target(cell) {
		cell.align().begin();
	}

	IGridPanel.IGridCell around(int column, int row) : 
	call(IGridPanel.IGridCell IGridPanel.cell(int, int)) 
	&& withincode(* FormFieldImpl.*(..))
	&& args(column, row) {
		return proceed(1, row * 2 + 1);
	}

	IGridPanel.IGridColumn around(int column) : 
	call(IGridPanel.IGridColumn IGridPanel.column(int)) 
	&& args(column) {
		return proceed(0);
	}
}
