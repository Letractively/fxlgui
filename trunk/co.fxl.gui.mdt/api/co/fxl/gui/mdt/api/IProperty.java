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

import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.IFieldType;

public interface IProperty<T, S> {

	public interface IAdapter<T, S> {

		boolean hasProperty(T entity);

		S valueOf(T entity);

		void valueOf(T entity, S value);

		// TODO not supported yet
		boolean editable(T entity);
	}

	public interface IUpdateListener<T> {

		void update(T entity, ICallback<Boolean> cb);
	}

	IFieldType type();

	IProperty<T, S> adapter(IAdapter<T, S> adapter);

	IProperty<T, S> updateListener(IUpdateListener<T> listener);

	IProperty<T, S> required();

	IProperty<T, S> inTable(boolean inTable);

	IProperty<T, S> asDetail(boolean asDetail);

	IProperty<T, S> sortable(boolean sortable);

	IProperty<T, S> editable(boolean b);

	IProperty<T, S> filterable();

	String name();
}
