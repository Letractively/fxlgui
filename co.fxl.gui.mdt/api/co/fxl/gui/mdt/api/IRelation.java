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

import java.util.List;

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.template.ICallback;

public interface IRelation<T, R> extends IPropertyGroup<R> {

	public interface IAddRemoveListener<T, R> {

		// TODO callback

		void onAdd(T base, R entity);

		void onRemove(T base, R entity);

		boolean isDetailedAdd();

		IButton decorateAdd(T base, IContainer c);
	}

	public interface IShowListener<R> {

		void onShow(R entity);
	}

	public interface IAdapter<T, R> {

		void valueOf(T entity, ICallback<List<R>> callback);
	}

	IRelation<T, R> adapter(IAdapter<T, R> adapter);

	IRelation<T, R> showListener(IShowListener<R> l);

	IRelation<T, R> addRemoveListener(IAddRemoveListener<T, R> l);
}
