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

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;

public class SplitLayout {

	private ILayout layout;
	public IGridPanel panel;
	public IVerticalPanel mainPanel;
	public IVerticalPanel sidePanel;

	public SplitLayout(ILayout layout) {
		this.layout = layout;
		init();
	}

	private void init() {
		panel = layout.grid();
		IVerticalPanel vpanel = panel.cell(0, 0).valign().begin().panel()
				.vertical().spacing(10);
		mainPanel = vpanel.add().panel().vertical().spacing(10);
		mainPanel.border().color().lightgray();
		mainPanel.color().white();
		IGridCell cell = panel.cell(1, 0).width(300).valign().begin();
		sidePanel = cell.panel().vertical().add().panel().vertical()
				.spacing(10);
	}

	public void reset() {
		panel.remove();
		init();
	}
}
