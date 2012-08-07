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
package co.fxl.gui.table.bulk.impl;

import co.fxl.gui.api.IPoint;
import co.fxl.gui.automation.api.IAutomationListener.Key;
import co.fxl.gui.automation.impl.Automation;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableClickListener;

public class AutomationTableClickListener implements ITableClickListener {

	private IBulkTableWidget e;
	private Key key;

	private AutomationTableClickListener(IBulkTableWidget e, Key key) {
		this.e = e;
		this.key = key;
	}

	@Override
	public void onClick(int column, int row, IPoint point) {
		if (Automation.ENABLED) {
			BulkTableWidgetListener.instance().notifyClick(e, column, row,
					point.offsetX(), point.offsetY(), key);
		}
	}

	public static void attach(IBulkTableWidget element) {
		element.addTableListener(new AutomationTableClickListener(element,
				Key.LEFT));
		element.addTableListener(
				new AutomationTableClickListener(element, Key.CTRL))
				.ctrlPressed();
		element.addTableListener(
				new AutomationTableClickListener(element, Key.SHIFT))
				.shiftPressed();
		element.addTableListener(
				new AutomationTableClickListener(element, Key.DOUBLE))
				.doubleClick();
	}
}
