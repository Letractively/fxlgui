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
import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.impl.Constraint.INamedConstraint;
import co.fxl.gui.filter.impl.Constraint.ISizeConstraint;
import co.fxl.gui.filter.impl.Constraint.IStringPrefixConstraint;

class FilterConstraintsImpl implements IFilterConstraints {

	private int size = Integer.MAX_VALUE;
	private Map<String, INamedConstraint> constraints = new HashMap<String, INamedConstraint>();

	@SuppressWarnings("unchecked")
	@Override
	public IRange<Date> dateRange(String column) {
		return (IRange<Date>) constraints.get(column);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IRange<Integer> intRange(String column) {
		return (IRange<Integer>) constraints.get(column);
	}

	@Override
	public boolean isConstrained(String column) {
		return constraints.containsKey(column);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String stringValue(String column) {
		return ((IStringPrefixConstraint) constraints.get(column)).prefix();
	}

	void add(Constraint c) {
		if (c instanceof ISizeConstraint) {
			size = ((ISizeConstraint) c).size();
		} else {
			INamedConstraint n = (INamedConstraint) c;
			constraints.put(n.column(), n);
		}
	}

	@Override
	public Class<?> typeOf(String column) {
		INamedConstraint constraint = constraints.get(column);
		if (constraint instanceof IStringPrefixConstraint) {
			return String.class;
		} else
			throw new MethodNotImplementedException();
	}
}
