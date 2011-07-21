/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.mdt.api;

import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;

public interface IMDTFilterList<T> {

	public interface IMDTFilter extends IFilter {

		IMDTFilter inTable(boolean inTable);

		IMDTFilter asDetail(boolean asDetail);
	}

	public interface IMDTRelationFilter<R, S> extends IRelationFilter<R, S> {

		IMDTFilter inTable(boolean inTable);

		IMDTFilter asDetail(boolean asDetail);
	}

	// TODO move to IMasterDetailTableWidget
	IMDTFilterList<T> addConfiguration(String configuration);

	IMDTFilter addFilter();

	IMDTRelationFilter<?, ?> addRelationFilter();

	// TODO @IProperty
	IMDTFilterList<T> addPropertyFilter(IProperty<T, ?> property);

	IMDTFilterList<T> constraints(IFilterConstraints constraints);

	IFilterConstraints constraints();
}
