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
package co.fxl.gui.table.filter.api;

import co.fxl.gui.api.ILayout;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.api.ITableWidget;

public interface IFilterTableWidget<T> extends ITableWidget<T> {

	public interface ITableFilter {

		ITableFilter filterable(IColumn column, Object... values);

		ITableFilter filterable(IColumn column, Class<?> contentType,
				Object... values);

		ITableFilter constraints(IFilterConstraints constraints);

		ITableFilter apply();
	}

	public interface IRowModel<T> {

		IFilterTableWidget<T> notifyServerCall();

		IRow<T> addRow();

		void onSuccess();

		void onFail();
	}

	public interface IFilterListener<T> {

		void onRefresh(IRowModel<T> rows, IFilterConstraints constraints);
	}

	ITableFilter filterPanel(ILayout layout);

	IRowModel<T> resetRowModel();

	IFilterTableWidget<T> addFilterListener(IFilterListener<T> listener);
}
