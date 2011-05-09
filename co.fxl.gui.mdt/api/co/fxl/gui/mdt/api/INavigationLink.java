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

public interface INavigationLink<T> {

	public interface IEntityConstraint<T> {

		boolean applies(T entity);
	}

	public interface INavigationLinkSelectionListener<T> {

		void onUpdate(INavigationLink<T> link, List<T> selection);
	}

	public interface INavigationLinkListener<T> {

		void onClick(List<T> selection);
	}

	INavigationLink<T> inTable(boolean inTable);

	INavigationLink<T> asDetail(boolean asDetail);

	INavigationLink<T> addClickListener(INavigationLinkListener<T> listener);

	INavigationLink<T> requiresSelection(boolean requiresSelection);

	INavigationLink<T> requiresRows(boolean requiresRows);

	INavigationLink<T> typeConstraint(Class<?> typeConstraint);

	INavigationLink<T> exclusionConstraint(Class<?>[] typeConstraint);

	INavigationLink<T> imageResource(String imageResource);

	INavigationLink<T> text(String text);

	INavigationLink<T> addSelectionListener(
			INavigationLinkSelectionListener<T> l);

	INavigationLink<T> clickable(boolean b);

	INavigationLink<T> entityConstraint(IEntityConstraint<T> constraint);
}
