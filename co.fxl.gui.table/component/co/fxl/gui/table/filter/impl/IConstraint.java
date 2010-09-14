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

import co.fxl.gui.table.filter.api.IConstraints.IRange;

interface IConstraint {

	public interface INamedConstraint extends IConstraint {

		String column();
	}

	public interface ISizeConstraint extends IConstraint {

		int size();
	}

	interface IStringPrefixConstraint extends INamedConstraint {

		String prefix();
	}

	interface IRangeConstraint<T> extends INamedConstraint {

		T lowerBound();

		T upperBound();
	}

	interface IDoubleRangeConstraint extends IRangeConstraint<Double>,
			IRange<Double> {

	}

	interface IDateRangeConstraint extends IRangeConstraint<Date>, IRange<Date> {

	}

	interface IIntegerRangeConstraint extends IRangeConstraint<Integer>,
			IRange<Integer> {

	}
}
