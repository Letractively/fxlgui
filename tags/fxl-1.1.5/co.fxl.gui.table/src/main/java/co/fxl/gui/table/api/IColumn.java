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

import co.fxl.gui.impl.IFieldType;

public interface IColumn<T> {

	public interface IColumnUpdateListener<T, R> {

		boolean isEditable(T entity);

		boolean onUpdate(T entity, R value);
	}

	public interface IDecorator<T, R> {

		void decorate(T element, R value);
	}

	IColumn<T> name(String name);

	IColumn<T> sortable();

	IFieldType type();

	IColumn<T> type(IFieldType type);

	IColumn<T> fixedWidth(int width);

	IColumn<T> fixedWidth(double width);

	IColumn<T> decorator(IDecorator<?, ?> decorator);

	IColumn<T> tagSortOrder(boolean up);

	IColumn<T> updateListener(IColumnUpdateListener<T, ?> updateListener);

	IColumn<T> width(double width);

	IColumn<T> width(int width);
}