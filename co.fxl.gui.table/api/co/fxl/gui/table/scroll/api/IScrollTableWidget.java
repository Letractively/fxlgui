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
package co.fxl.gui.table.scroll.api;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableListener;

public interface IScrollTableWidget<T> {

	public interface ISortListener {

		void onSort(String columnName, boolean up);
	}

	ILabel addTitle(String text);

	IClickable<?> addButton(String name);

	ISelection<T> selection();

	IScrollTableColumn<T> addColumn();

	int offsetY();

	IScrollTableWidget<T> height(int height);

	IScrollTableWidget<T> rows(IRows<T> rows);

	IKey<?> addTableListener(ITableListener l);

	IScrollTableWidget<T> sortListener(ISortListener l);

	IScrollTableWidget<T> visible(boolean visible);

	IScrollTableWidget<T> addTooltip(String tooltip);

	IScrollTableWidget<T> addFilterListener(IFilterListener l);

	IScrollTableWidget<T> refresh();
}
