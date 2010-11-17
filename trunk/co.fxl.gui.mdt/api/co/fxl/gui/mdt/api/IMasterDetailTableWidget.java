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

import co.fxl.gui.async.ICallback;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.tree.api.ITree;

public interface IMasterDetailTableWidget<T> {

	public interface IContent<T> {

		void queryTree(IFilterConstraints constraints,
				ICallback<ITree<T>> callback);

		void queryList(IFilterConstraints constraints,
				ICallback<IList<T>> callback);

		void queryRelation(T entity, String name, ICallback<List<T>> callback);
	}

	IMasterDetailTableWidget<T> title(String title);

	IFilterList<T> filterList();

	IMasterDetailTableWidget<T> content(IContent<T> source);

	IPropertyGroup<T> defaultPropertyGroup();

	IPropertyGroup<T> addPropertyGroup(String name);

	IRelation<T, ?> addRelation(String name);

	IMasterDetailTableWidget<T> visible(boolean visible);

	INavigationLink<T> addNavigationLink(String name);
}
