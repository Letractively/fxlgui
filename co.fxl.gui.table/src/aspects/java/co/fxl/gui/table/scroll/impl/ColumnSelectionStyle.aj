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

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.style.impl.Style;

privileged aspect ColumnSelectionStyle {

	after(ScrollTableColumnImpl c, IHorizontalPanel b) :
	execution(ILabel ColumnSelection.decoratePanel(..)) 
	&& args(.., c, b) 
	&& if(Style.ENABLED) {
		Style.instance().table().columnSelection().panel(b, c.visible);
	}

	after(ScrollTableColumnImpl c, ILabel l) :
	execution(void ColumnSelection.decorateLabel(..)) 
	&& args(.., c, l) 
	&& if(Style.ENABLED) {
		Style.instance().table().columnSelection().label(l, c.visible);
	}
}
