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
package co.fxl.gui.table.api;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.ILabel;

public interface ITableWidget<T> {

	public interface IRows<T> {

		T identifier(int i);

		Object[] row(int i);

		int size();
	}

	ILabel addTitle(String text);

	IClickable<?> addButton(String name);

	ISelection<T> selection();

	IColumn addColumn();

	IRow<T> addRow();

	ITableWidget<?> visible(boolean visible);

	int offsetY();

	ITableWidget<T> height(int height);

	ITableWidget<T> source(IRows<T> rows);
}
