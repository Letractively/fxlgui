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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.layout.impl.Layout;

privileged aspect ColumnSelectionLayout {

	void around(ColumnSelection columnSelection, Object p,
			IClickListener clickListener) : 
	call(void ColumnSelection.addToPanel(..))
	&& withincode(ColumnSelection.new(ScrollTableWidgetImpl))
	&& args(p, clickListener) 
	&& this(columnSelection)
	&& if(Layout.ENABLED) {
		ColumnSelectionDialog.addButton(columnSelection, (ILinearPanel) p);
	}

	void around() : 
	call(void ColumnSelection.addTitle(ILinearPanel)) 
	&& withincode(void ColumnSelection.addToPanel(..))
	&& if(Layout.ENABLED) {
	}

	ILabel around(ILabel label, String in) : 
	call(public ILabel ITextElement.text(String)) 
	&& withincode(private void ScrollTableWidgetImpl.addDisplayingNote()) 
	&& args(in) 
	&& target(label)
	&& if(Layout.ENABLED) {
		return label.text(in.equals("DISPLAYING ROWS") ? "ROWS" : in);
	}

	after(ScrollTableWidgetImpl widget) returning(IScrollTableColumn column) : 
	execution(public IScrollTableColumn ScrollTableWidgetImpl.addColumn()) 
	&& this(widget)
	&& if(Layout.ENABLED) {
		if (widget.columns.size() > 2) {
			column.visible(false);
		}
	}

	after() returning(Link link) : 
	call(private Link Link.clickable(IContainer, String, boolean))
	&& withincode(public Link Link.clickableLink(IContainer, String))
	&& if(Layout.ENABLED) {
		link.label.visible(false);
	}

	before(Link link) : 
	execution(private Link Link.clickable(IContainer, String, boolean)) 
	&& this(link)
	&& if(Layout.ENABLED) {
		Link.SPACE = 0;
	}
}
