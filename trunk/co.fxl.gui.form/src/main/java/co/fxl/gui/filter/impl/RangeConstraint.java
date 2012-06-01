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
import co.fxl.gui.filter.impl.IFilterConstraint.IRangeConstraint;

public abstract class RangeConstraint<T> implements IRangeConstraint<T> {

	public String name;
	public T lowerBound;
	public T upperBound;

	RangeConstraint(String name, T lowerBound, T upperBound) {
		this.name = name;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	public String column() {
		return name;
	}

	@Override
	public T lowerBound() {
		return lowerBound;
	}

	@Override
	public T upperBound() {
		return upperBound;
	}

	@Override
	public String toString() {
		return wNull(lowerBound) + "-" + wNull(upperBound);
	}

	String wNull(T lowerBound) {
		if (lowerBound == null)
			return "";
		return Format.format(Date.class, lowerBound);
	}

}
