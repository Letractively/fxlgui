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

import co.fxl.gui.filter.api.IFilterConstraints;

public interface INavigationLink<R, T> {

	public interface IEntityConstraint<R> {

		boolean applies(R entity);
	}

	public interface IBaseEntityConstraint<R> {

		boolean applies(IFilterConstraints constraints, List<R> selection);
	}

	public interface INavigationLinkSelectionListener<R, T> {

		void onUpdate(INavigationLink<R, T> link, List<T> selection);
	}

	public interface INavigationLinkListener<T> {

		void onClick(List<T> selection);
	}

	INavigationLink<R, T> inTable(boolean inTable);

	INavigationLink<R, T> asDetail(boolean asDetail);

	INavigationLink<R, T> addClickListener(INavigationLinkListener<T> listener);

	INavigationLink<R, T> requiresSelection(boolean requiresSelection);

	INavigationLink<R, T> requiresRows(boolean requiresRows);

	INavigationLink<R, T> typeConstraint(Class<?> typeConstraint);

	INavigationLink<R, T> exclusionConstraint(Class<?>[] typeConstraint);

	INavigationLink<R, T> imageResource(String imageResource);

	INavigationLink<R, T> text(String text);

	INavigationLink<R, T> addSelectionListener(
			INavigationLinkSelectionListener<R, T> l);

	INavigationLink<R, T> clickable(boolean b);

	INavigationLink<R, T> entityConstraint(IEntityConstraint<T> constraint);

	INavigationLink<R, T> baseEntityConstraint(
			IBaseEntityConstraint<R> constraint);
}
