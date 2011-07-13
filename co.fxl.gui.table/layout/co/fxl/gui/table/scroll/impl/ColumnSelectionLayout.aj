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
package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;

privileged aspect ColumnSelectionLayout {

	void around(ColumnSelection columnSelection, ILinearPanel<?> p,
			IClickListener clickListener) : call(* ColumnSelection.addToPanel(..)) 
	&& args(p, clickListener) 
	&& this(columnSelection) {
		ColumnSelectionDialog.addButton(columnSelection, p);
	}

	void around() : call(* ColumnSelection.addTitle(..)) {
	}

	ILabel around(String in) : call(public ILabel ILabel.text(String)) 
	&& withincode(private void ScrollTableWidgetImpl.addDisplayingNote()) 
	&& args(in) 
	&& if(in.equals("DISPLAYING ROWS")) {
		return proceed("ROWS");
	}

	after(ScrollTableWidgetImpl widget) returning(IScrollTableColumn column) : 
	execution(public IScrollTableColumn ScrollTableWidgetImpl.addColumn()) 
	&& this(widget) {
		if (widget.columns.size() > 2) {
			column.visible(false);
		}
	}
}
