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

import java.util.Date;


import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.table.impl.SimpleDateFormat;

class DateFilter extends RangeFilter<Date> {

	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
	private Date lowerBound = null;
	private Date upperBound = null;

	DateFilter(int columnIndex, IGridPanel parent, String name, int filterIndex) {
		super(columnIndex, parent, name, filterIndex);
	}

	@Override
	public boolean update() {
		super.update();
		lowerBound = getDate(lowerBoundText, "01.01.");
		setLowerBound(format(lowerBound));
		upperBound = getDate(upperBoundText, "31.12.");
		setUpperBound(format(upperBound));
		return lowerBound != null || upperBound != null;
	}

	private String format(Date bound) {
		if (bound == null)
			return "";
		return DATE_FORMAT.format(bound);
	}

	private Date getDate(String split, String prefix) {
		split = split.trim();
		if (split.length() == 4)
			split = prefix + split;
		return DATE_FORMAT.parse(split);
	}

	@Override
	public boolean applies(Date value) {
		if (lowerBound != null && lowerBound.after(value)) {
			return false;
		}
		if (upperBound != null && upperBound.before(value)) {
			return false;
		}
		return true;
	}

	@Override
	public Object[] values() {
		update();
		return new Object[] { lowerBound, upperBound };
	}
}
