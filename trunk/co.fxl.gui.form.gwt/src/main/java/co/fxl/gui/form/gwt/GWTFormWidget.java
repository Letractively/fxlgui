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
package co.fxl.gui.form.gwt;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.impl.FormWidgetImpl;
import co.fxl.gui.gwt.GWTDisplay;
import co.fxl.gui.gwt.GWTGridPanel.GridCell;

import com.google.gwt.dom.client.Style.TableLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Grid;

class GWTFormWidget extends FormWidgetImpl implements IFormWidget {

	GWTFormWidget(IContainer container) {
		super(container);
	}

	@Override
	protected void decorate(IGridPanel grid) {
		if (GWTDisplay.isInternetExplorer8()) {
			grid.column(0).width(100);
			Grid g = (Grid) grid.nativeElement();
			g.getElement().getStyle().setTableLayout(TableLayout.FIXED);
			Element element = g.getColumnFormatter().getElement(1);
			element.addClassName("wordWrapBreakAll");
		}
	}

	@Override
	protected void decorateCell(IGridCell cell) {
		if (GWTDisplay.isInternetExplorer8()) {
			Grid g = (Grid) grid.nativeElement();
			GridCell gc = (GridCell) cell;
			Element element = g.getCellFormatter()
					.getElement(gc.row, gc.column);
			element.addClassName("wordWrapBreakAll");
		}
	}

	@Override
	protected void prepareButtonColumn(IGridPanel grid, int column) {
		if (GWTDisplay.isInternetExplorer8()) {
			grid.column(column).width(60);
		}
	}
}
