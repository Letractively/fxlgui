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
package co.fxl.gui.filter.api;

import java.util.Date;
import java.util.List;

import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;

public interface IFilterConstraints {

	public static final String COMMON = "C";

	public interface IRowIterator {

		int firstRow();

		void firstRow(int firstRow);

		boolean hasNext();

		void hasNext(boolean hasNext);

		int nextFirstRow();

		void nextFirstRow(int nextFirstRow);

		boolean hasPrevious();

		void hasPrevious(boolean hasPrevious);

		int nextPreviousRow();

		void nextPreviousRow(int nextPreviousRow);
	}

	public interface IRange<R> {

		R lowerBound();

		R upperBound();
	}

	int size();

	String configuration();

	boolean isAttributeConstrained(String column);

	boolean isRelationConstrained(String column);

	IRange<Integer> intRange(String column);

	IRange<Date> dateRange(String column);

	IRange<Double> doubleRange(String string);

	IRange<Long> longRange(String uiLabel);

	String stringValue(String column);

	List<?> relationList(String column);

	Class<?> typeOf(String column);

	Boolean booleanValue(String uiLabel);

	IFilterConstraints sortOrder(String columnName);

	String sortOrder();

	boolean sortDirection();

	IFilterConstraints sortDirection(boolean ascending);

	List<String[]> description();

	IRowIterator rowIterator();

	void clear();

	boolean isSpecified();

	boolean isConstraintSpecified();

	void configuration(String configuration);

	boolean isContentRestricted();

	IFilterConstraints rowIterator(IRowIterator rowIterator);

	IFilterConstraints copy();

	IRelationFilter<Object, Object> addRelationFilter();
}
