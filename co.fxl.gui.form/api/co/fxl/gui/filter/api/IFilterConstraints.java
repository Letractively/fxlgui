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

public interface IFilterConstraints {

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

	String stringValue(String column);

	List<?> relationList(String column);

	Class<?> typeOf(String column);

	IRange<Double> doubleRange(String string);
}
