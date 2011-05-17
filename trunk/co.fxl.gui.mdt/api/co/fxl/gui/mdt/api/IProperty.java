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

import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.IFieldType;

public interface IProperty<T, S> {

	public interface IConditionRule<T, S, R> {

		public interface ICondition<S> {

			boolean satisfied(S value);
		}

		IConditionRule<T, S, R> condition(ICondition<S> condition);

		IConditionRule<T, S, R> target(IProperty<T, R> target);

		IConditionRule<T, S, R> targetValues(R... targetValues);
	}

	public interface IConstraintAdapter<T, S> {

		List<S> constraints(T entity);
	}

	public interface IAdapter<T, S> {

		boolean hasProperty(T entity);

		S valueOf(T entity);

		boolean valueOf(T entity, S value);

		// TODO not supported yet
		boolean editable(T entity);
	}

	public interface IUpdateListener<T> {

		void update(T entity, ICallback<Boolean> cb);
	}

	IFieldType type();

	IProperty<T, S> adapter(IAdapter<T, S> adapter);

	IProperty<T, S> constraintAdapter(IConstraintAdapter<T, S> adapter);

	IProperty<T, S> updateListener(IUpdateListener<T> listener);

	IProperty<T, S> required();

	IProperty<T, S> inTable(boolean inTable);

	IProperty<T, S> asDetail(boolean asDetail);

	IProperty<T, S> sortable(boolean sortable);

	IProperty<T, S> editable(boolean b);

	IProperty<T, S> filterable();

	IConditionRule<T, S, ?> addConditionRule();

	String name();
}
