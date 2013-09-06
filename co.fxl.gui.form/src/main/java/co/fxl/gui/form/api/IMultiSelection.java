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
package co.fxl.gui.form.api;

import java.util.List;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IUpdateable;

public interface IMultiSelection<T> extends IUpdateable<List<T>> {

	public interface IMultiSelectionAdapter<T> {

		String icon(T object);

		String label(T object);

		String text(T object);

		void query(String text, ICallback<List<T>> cb);
	}

	IMultiSelection<T> adapter(IMultiSelectionAdapter<T> adapter);

	String text();

	void text(String text);

}
