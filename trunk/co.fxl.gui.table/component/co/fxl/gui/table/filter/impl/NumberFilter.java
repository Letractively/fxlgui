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

import co.fxl.gui.api.IGridPanel;

class NumberFilter extends RangeFilter<Number> {

	private Double lowerBound = null;
	private Double upperBound = null;

	NumberFilter(int columnIndex, IGridPanel parent, String name,
			int filterIndex) {
		super(columnIndex, parent, name, filterIndex);
	}

	@Override
	public boolean update() {
		super.update();
		lowerBound = getDouble(lowerBoundText);
		setLowerBound(valueOf(lowerBound));
		upperBound = getDouble(upperBoundText);
		setUpperBound(valueOf(upperBound));
		return lowerBound != null || upperBound != null;
	}

	private String valueOf(Double bound) {
		if (bound == null)
			return "";
		return String.valueOf(bound);
	}

	private Double getDouble(String split) {
		split = split.trim();
		try {
			return Double.valueOf(split);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public boolean applies(Number number) {
		Double value = toDouble(number);
		if (lowerBound != null && lowerBound.compareTo(value) > 0) {
			return false;
		}
		if (upperBound != null && upperBound.compareTo(value) < 0) {
			return false;
		}
		return true;
	}

	private Double toDouble(Number number) {
		if (number instanceof Integer)
			return Double.valueOf((Integer) number);
		return (Double) number;
	}

	@Override
	public Object[] values() {
		update();
		return new Object[] { lowerBound, upperBound };
	}
}
