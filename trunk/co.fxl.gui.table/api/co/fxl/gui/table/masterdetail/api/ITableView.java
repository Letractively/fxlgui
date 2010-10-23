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
package co.fxl.gui.table.masterdetail.api;

import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.filter.api.IFilterTableWidget.IFilterListener;

public interface ITableView<T> {

	ITableView<T> contrainedOn(String constraint, IClickListener removeListener);

	ITableView<T> nameColumn(int columnIndex);

	IRow<T> addRow();

	ILabel addTitle(String title);

	IClickable<?> addNavigationLink(String name);

	List<T> selection();

	IColumn addColumn(String name, Class<?> type);

	IColumn addColumn(String name, String[] texts);

	IColumn addImageColumn(String name, String[] images);

	ITableView<T> filterListener(IFilterListener<T> listener);

	ITableView<T> visible(boolean visible);
}
