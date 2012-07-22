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

public class ColumnWidths {

	// private static final boolean USE_MAX_TOKENS = false;
	private Map<ScrollTableColumnImpl, Integer> intWidths = new HashMap<ScrollTableColumnImpl, Integer>();
	private Map<ScrollTableColumnImpl, Double> doubleWidths = new HashMap<ScrollTableColumnImpl, Double>();

	public ColumnWidths(boolean useMaxTokens,
			List<ScrollTableColumnImpl> columns) {
		
		// TODO FilterQueryDisplayData-Computation-Unit verwenden
		
		// int widthMin = 0;
		int num = 0;
		double sumD = 0;
		for (int c = 0; c < columns.size(); c++) {
			if (!columns.get(c).visible)
				continue;
			ScrollTableColumnImpl columnImpl = columns.get(c);
			Double w = columnImpl.widthDouble;
			if (w != null && w > 0) {
				num++;
				sumD += w;
			}
		}
		double avg = num > 0 ? sumD / num : -1;
		double sum = 0;
		for (int c = 0; c < columns.size(); c++) {
			if (!columns.get(c).visible)
				continue;
			ScrollTableColumnImpl columnImpl = columns.get(c);
			intWidths.put(columnImpl, columnImpl.widthInt);
			// if (USE_MAX_TOKENS && useMaxTokens) {
			// int maxTokens = columnImpl.decorator().maxTokens();
			// if (columnImpl.widthInt == -1 && maxTokens != -1) {
			// int width = Math.min(200,
			// Math.max(100, (maxTokens + 4) * 7));
			// intWidths.put(columnImpl, width);
			// }
			// }
			if (intWidths.get(columnImpl) != -1) {
				// widthMin += intWidths.get(columnImpl);
				continue;
			}
			double d = columnImpl.decorator().defaultWeight();
			if (avg != -1) {
				d = columnImpl.widthDouble == -1 ? avg : columnImpl.widthDouble;
			}
			doubleWidths.put(columnImpl, d);
			sum += d;
			// widthMin += 100;
		}
		// int mainPanelWidth = SplitLayout.mainPanelWidth();
		// if (USE_MAX_TOKENS && widthMin > mainPanelWidth) {
		// intWidths.clear();
		// for (int c = 0; c < columns.size(); c++) {
		// if (!columns.get(c).visible)
		// continue;
		// ScrollTableColumnImpl columnImpl = columns.get(c);
		// double d = columnImpl.widthDouble != -1 ? columnImpl.widthDouble
		// : columnImpl.decorator().defaultWeight();
		// doubleWidths.put(columnImpl, d);
		// }
		// }
		for (int c = 0; c < columns.size(); c++) {
			ScrollTableColumnImpl columnImpl = columns.get(c);
			if (!columnImpl.visible)
				continue;
			Double w = doubleWidths.get(columnImpl);
			if (w != null && w != -1)
				doubleWidths.put(columnImpl, w / sum);
		}
		for (int c = 0; c < columns.size(); c++) {
			ScrollTableColumnImpl columnImpl = columns.get(c);
			if (!columnImpl.visible)
				continue;
		}
	}

	public void prepare(ScrollTableColumnImpl stc,
			co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn btc) {
		stc.decorator().prepare(btc);
		Integer i = intWidths.get(stc);
		if (i != null && i != -1)
			btc.width((int) i);
		else {
			Double d = doubleWidths.get(stc);
			if (d != null)
				btc.width(d);
		}
		if (stc.alignment.isSpecified()) {
			stc.alignment.forward(btc.align());
		}
	}
}
