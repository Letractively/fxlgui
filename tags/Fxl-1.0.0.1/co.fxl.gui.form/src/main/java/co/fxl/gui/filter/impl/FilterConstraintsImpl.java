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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.filter.impl.Constraint.IBooleanConstraint;
import co.fxl.gui.filter.impl.Constraint.IDoubleRangeConstraint;
import co.fxl.gui.filter.impl.Constraint.INamedConstraint;
import co.fxl.gui.filter.impl.Constraint.IRelationConstraint;
import co.fxl.gui.filter.impl.Constraint.ISizeConstraint;
import co.fxl.gui.filter.impl.Constraint.IStringPrefixConstraint;

public class FilterConstraintsImpl implements IFilterConstraints {

	private class IntegerRangeConstraintImpl implements IRange<Integer> {

		private Integer upperBound = null;
		private Integer lowerBound = null;

		IntegerRangeConstraintImpl(IDoubleRangeConstraint iNamedConstraint) {
			if (iNamedConstraint.upperBound() != null)
				upperBound = iNamedConstraint.upperBound().intValue();
			if (iNamedConstraint.lowerBound() != null)
				lowerBound = iNamedConstraint.lowerBound().intValue();
		}

		@Override
		public Integer lowerBound() {
			return lowerBound;
		}

		@Override
		public Integer upperBound() {
			return upperBound;
		}
	}

	private class LongRangeConstraintImpl implements IRange<Long> {

		private Long upperBound = null;
		private Long lowerBound = null;

		LongRangeConstraintImpl(IDoubleRangeConstraint iNamedConstraint) {
			if (iNamedConstraint.upperBound() != null)
				upperBound = iNamedConstraint.upperBound().longValue();
			if (iNamedConstraint.lowerBound() != null)
				lowerBound = iNamedConstraint.lowerBound().longValue();
		}

		@Override
		public Long lowerBound() {
			return lowerBound;
		}

		@Override
		public Long upperBound() {
			return upperBound;
		}
	}

	public class RowIterator implements IRowIterator {

		private int firstRow = 0;
		private boolean hasNext = false;
		private boolean hasPrevious;
		private int nextPreviousRow;
		private int nextFirstRow;

		@Override
		public int firstRow() {
			return firstRow;
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public void hasNext(boolean hasNext) {
			this.hasNext = hasNext;
		}

		@Override
		public int nextFirstRow() {
			return nextFirstRow;
		}

		@Override
		public void nextFirstRow(int nextFirstRow) {
			this.nextFirstRow = nextFirstRow;
		}

		@Override
		public boolean hasPrevious() {
			return hasPrevious;
		}

		@Override
		public void hasPrevious(boolean hasPrevious) {
			this.hasPrevious = hasPrevious;
		}

		@Override
		public int nextPreviousRow() {
			return nextPreviousRow;
		}

		@Override
		public void nextPreviousRow(int nextPreviousRow) {
			this.nextPreviousRow = nextPreviousRow;
		}

		@Override
		public void firstRow(int firstRow) {
			this.firstRow = firstRow;
		}

	}

	private int size = Integer.MAX_VALUE;
	private Map<String, INamedConstraint> constraints = new HashMap<String, INamedConstraint>();
	private String cfg;
	private String sortOrder;
	private boolean sortDirection = true;
	private IRowIterator it = new RowIterator();

	public FilterConstraintsImpl(FilterConstraintsImpl f) {
		size = f.size;
		constraints = new HashMap<String, INamedConstraint>(f.constraints);
		cfg = f.cfg;
		sortOrder = f.sortOrder;
		sortDirection = f.sortDirection;
		it = f.it;
	}

	public FilterConstraintsImpl() {
		size = (Integer) FilterWidgetImpl.DEFAULT_SIZES.get(0);
	}

	public FilterConstraintsImpl(String configuration) {
		cfg = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IRange<Date> dateRange(String column) {
		return (IRange<Date>) constraints.get(column);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IRange<Integer> intRange(String column) {
		INamedConstraint iNamedConstraint = constraints.get(column);
		if (iNamedConstraint instanceof IDoubleRangeConstraint) {
			return new IntegerRangeConstraintImpl(
					(IDoubleRangeConstraint) iNamedConstraint);
		}
		return (IRange<Integer>) iNamedConstraint;
	}

	@Override
	public boolean isAttributeConstrained(String column) {
		return constraints.get(column) != null
				&& !(constraints.get(column) instanceof IRelationConstraint);
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

	@Override
	public List<Object> relationList(String column) {
		return ((IRelationConstraint) constraints.get(column)).values();
	}

	@Override
	public boolean isRelationConstrained(String column) {
		return constraints.get(column) != null
				&& constraints.get(column) instanceof IRelationConstraint;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IRange<Double> doubleRange(String column) {
		return (IRange<Double>) constraints.get(column);
	}

	@Override
	public String configuration() {
		return cfg;
	}

	@Override
	public Boolean booleanValue(String uiLabel) {
		final INamedConstraint c = constraints.get(uiLabel);
		if (c instanceof IStringPrefixConstraint) {
			IStringPrefixConstraint sc = (IStringPrefixConstraint) c;
			return sc.equals(BooleanFilter.TRUE);
		}
		return ((IBooleanConstraint) c).value();
	}

	@Override
	public IFilterConstraints sortOrder(String columnName) {
		sortOrder = columnName;
		return this;
	}

	@Override
	public String sortOrder() {
		return sortOrder;
	}

	@Override
	public boolean sortDirection() {
		return sortDirection;
	}

	@Override
	public IFilterConstraints sortDirection(boolean sortDirection) {
		this.sortDirection = sortDirection;
		return this;
	}

	@Override
	public IRowIterator rowIterator() {
		return it;
	}

	@Override
	public String toString() {
		return constraints.toString();
	}

	@Override
	public void clear() {
		constraints.clear();
		size = (Integer) FilterWidgetImpl.DEFAULT_SIZES.get(0);
	}

	@Override
	public boolean isSpecified() {
		return isConstraintSpecified()
				|| size != (Integer) FilterWidgetImpl.DEFAULT_SIZES.get(0)
				|| sortOrder != null;
	}

	@Override
	public boolean isContentRestricted() {
		return isConstraintSpecified() || size != Integer.MAX_VALUE;
	}

	@Override
	public boolean isConstraintSpecified() {
		return !constraints.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IRange<Long> longRange(String column) {
		INamedConstraint iNamedConstraint = constraints.get(column);
		if (iNamedConstraint instanceof IDoubleRangeConstraint) {
			return new LongRangeConstraintImpl(
					(IDoubleRangeConstraint) iNamedConstraint);
		}
		return (IRange<Long>) iNamedConstraint;
	}

	@Override
	public void configuration(String configuration) {
		this.cfg = configuration;
	}

	@Override
	public List<String[]> description() {
		List<String[]> desc = new LinkedList<String[]>();
		for (INamedConstraint c : constraints.values())
			desc.add(new String[] { c.column(), c.toString() });
		return desc;
	}

	@Override
	public IFilterConstraints rowIterator(IRowIterator rowIterator) {
		it = rowIterator;
		return this;
	}

	@Override
	public IFilterConstraints copy() {
		return new FilterConstraintsImpl(this);
	}

	@Override
	public IRelationFilter<Object, Object> addRelationFilter() {
		throw new MethodNotImplementedException();
	}

}