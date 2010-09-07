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
package co.fxl.gui.table.filter.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;

class MiniWidget {

	FilterTableWidgetImpl widget;
	IVerticalPanel panel;

	MiniWidget(FilterTableWidgetImpl widget, IContainer cell, String title) {
		this.widget = widget;
		panel = cell.panel().vertical();
		panel.stretch(true);
		panel.spacing(7).border().color().lightgray();
		panel.color().mix().lightgray().white();
		panel.add().label().text(title).font().pixel(14).weight().bold();
	}
}
