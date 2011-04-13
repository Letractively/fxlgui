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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.filter.api.IFilterConstraints;

public interface IRelation<T, R> extends IPropertyGroup<R> {

	public interface IEditableAdapter<T> {

		boolean isEditable(T base);
	}

	public interface IAddListener<T, R> {

		void onAdd(T base, int index, R entity, ICallback<Boolean> refresh);

		boolean isDetailedAdd();

		IClickable<?> decorateAdd(T base, IContainer c);
	}

	public interface IUpDownListener<T, R> {

		void onUp(T base, int index, R entity, boolean max,
				ICallback<Boolean> refresh);

		void onDown(T base, int index, R entity, boolean max,
				ICallback<Boolean> refresh);
	}

	public interface IEditListener<T, R> {

		void onEdit(T base, int index, R entity, ICallback<Boolean> refresh);
	}

	public interface IShowListener<R> {

		void onShow(R entity);
	}

	public interface IAdapter<T, R> {

		void valueOf(T entity, IFilterConstraints constraints,
				ICallback<IDeletableList<R>> callback);
	}

	IRelation<T, R> adapter(IAdapter<T, R> adapter);

	IRelation<T, R> showListener(IShowListener<R> l);

	IRelation<T, R> addListener(IAddListener<T, R> l);

	IRelation<T, R> upDownListener(IUpDownListener<T, R> l);

	IRelation<T, R> allowColumnSelection(boolean allowColumnSelection);

	IRelation<T, R> sortable(boolean sortable);

	IRelation<T, R> editable(boolean editable);

	IRelation<T, R> editableAdapter(IEditableAdapter<T> editableAdapter);

	IRelation<T, R> editListener(IEditListener<T, R> editListener);

	IRelation<T, R> hide(IProperty<T, ?> property);
}
