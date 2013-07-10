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
package co.fxl.gui.filter.impl;

import java.util.Date;

import co.fxl.data.format.impl.Format;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterConstraints.IRange;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;

class DateFilter extends RangeFilter<Date> {

	Date lowerBound = null;
	Date upperBound = null;

	DateFilter(FilterGrid parent, String name, int filterIndex) {
		super(parent, name, filterIndex, true);
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
		return Format.date().format(bound);
	}

	private Date getDate(String split, String prefix) {
		split = split.trim();
		if (split.length() == 4)
			split = prefix + split;
		return Format.date().parse(split);
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
	public IFilterConstraint asConstraint() {
		update();
		return new DateRangeConstraint(name, lowerBound, upperBound);
	}

	@Override
	boolean fromConstraint(IFilterConstraints constraints) {
		if (constraints.isAttributeConstrained(name)) {
			IRange<Date> range = constraints.dateRange(name);
			if (range.lowerBound() != null)
				setLowerBound(format(range.lowerBound()));
			if (range.upperBound() != null)
				setUpperBound(format(range.upperBound()));
			return true;
		} else
			return false;
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	Class<?> type() {
		return Date.class;
	}
}
