/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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
import java.util.List;

import co.fxl.gui.filter.api.IFilterConstraints.IRange;

public interface IFilterConstraint {

	public interface INamedConstraint extends IFilterConstraint {

		String column();
	}

	public interface ISizeConstraint extends IFilterConstraint {

		int size();
	}

	interface IBooleanConstraint extends INamedConstraint {

		Boolean value();
	}

	interface IRelationConstraint extends INamedConstraint {

		List<Object> values();
	}

	interface IRangeConstraint<T> extends INamedConstraint, IRange<T> {

		T lowerBound();

		T upperBound();
	}

	interface IDoubleRangeConstraint extends IRangeConstraint<Double> {

	}

	interface IDateRangeConstraint extends IRangeConstraint<Date> {

	}

	interface IIntegerRangeConstraint extends IRangeConstraint<Integer> {

	}
}
