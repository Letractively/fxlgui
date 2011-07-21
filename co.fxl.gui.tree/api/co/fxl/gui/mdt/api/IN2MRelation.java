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

import co.fxl.gui.impl.ICallback;
import co.fxl.gui.n2m.api.IN2MWidget.IItemImageProvider;

public interface IN2MRelation<T, R> {

	public interface IAdapter<T, R> {

		void domain(T entity, ICallback<List<R>> callback);

		void valueOf(T entity, ICallback<List<R>> callback);

		void valueOf(T entity, List<R> values, ICallback<List<R>> callback);

		boolean editable(T entity);
	}

	IN2MRelation<T, R> adapter(IAdapter<T, R> adapter);

	IN2MRelation<T, R> constrainType(Class<?> class1);

	IN2MRelation<T, R> itemImage(String itemImage);

	IN2MRelation<T, R> itemImageProvider(IItemImageProvider<R> itemImageProvider);

	IN2MRelation<T, R> allowReorder(boolean allowReorder);

}
