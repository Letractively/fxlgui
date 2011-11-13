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

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.table.api.IColumn;

public interface IScrollTableColumn<T> extends IColumn<T> {

	public interface IColorAdapter<T> {

		String color(T identifier);
	}

	public interface IScrollTableListener<T> {

		void onClick(T identifier);
	}

	IAlignment<IScrollTableColumn<T>> align();

	IScrollTableColumn<T> filterable();

	IScrollTableColumn<T> editable();

	IScrollTableColumn<T> colorAdapter(IColorAdapter<T> colorAdapter);

	IScrollTableColumn<T> addClickListener(IScrollTableListener<T> l);

	IScrollTableColumn<T> visible(boolean visible);
	
	boolean visible();

	String name();

	IScrollTableColumn<T> forceSort();
}
