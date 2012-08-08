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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IColumnWidths;

public class ColumnWidths implements IColumnWidths {

	// TODO FilterQueryDisplayData-Computation-Unit verwenden

	private Map<IScrollTableColumn<?>, Integer> intWidths = new HashMap<IScrollTableColumn<?>, Integer>();
	private Map<IScrollTableColumn<?>, Double> doubleWidths = new HashMap<IScrollTableColumn<?>, Double>();

	@Override
	public IColumnWidths columns(List<IScrollTableColumn<?>> columns) {
		intWidths.clear();
		doubleWidths.clear();
		int num = 0;
		double sumD = 0;
		for (int c = 0; c < columns.size(); c++) {
			if (!columns.get(c).visible())
				continue;
			IScrollTableColumn<?> columnImpl = columns.get(c);
			Double w = columnImpl.widthDouble();
			if (w != null && w > 0) {
				num++;
				sumD += w;
			}
		}
		double avg = num > 0 ? sumD / num : -1;
		double sum = 0;
		for (int c = 0; c < columns.size(); c++) {
			if (!columns.get(c).visible())
				continue;
			IScrollTableColumn<?> columnImpl = columns.get(c);
			intWidths.put(columnImpl, columnImpl.widthInt());
			if (intWidths.get(columnImpl) != -1) {
				continue;
			}
			double d = ((ScrollTableColumnImpl) columnImpl).decorator()
					.defaultWeight();
			if (avg != -1) {
				d = columnImpl.widthDouble() == -1 ? avg : columnImpl
						.widthDouble();
			}
			doubleWidths.put(columnImpl, d);
			sum += d;
		}
		for (int c = 0; c < columns.size(); c++) {
			IScrollTableColumn<?> columnImpl = columns.get(c);
			if (!columnImpl.visible())
				continue;
			Double w = doubleWidths.get(columnImpl);
			if (w != null && w != -1)
				doubleWidths.put(columnImpl, w / sum);
		}
		for (int c = 0; c < columns.size(); c++) {
			IScrollTableColumn<?> columnImpl = columns.get(c);
			if (!columnImpl.visible())
				continue;
		}
		return this;
	}

	@Override
	public void prepare(IScrollTableColumn<?> stc,
			co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn btc) {
		((ScrollTableColumnImpl) stc).decorator().prepare(btc);
		Integer i = intWidths.get(stc);
		if (i != null && i != -1)
			btc.width((int) i);
		else {
			Double d = doubleWidths.get(stc);
			if (d != null)
				btc.width(d);
		}
		if (stc.isAlignmentSpecified()) {
			stc.forwardAlignment(btc.align());
		}
	}

	@Override
	public void notifyColumnSelectionChange() {
	}

	@Override
	public void startPrepare(int width) {
	}
}
