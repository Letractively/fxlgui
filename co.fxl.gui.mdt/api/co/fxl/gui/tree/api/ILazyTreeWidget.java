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
package co.fxl.gui.tree.api;

import co.fxl.gui.api.IContainer;

public interface ILazyTreeWidget<T> {

	public interface IDecorator {

		void decorate(IContainer c, int index);
	}

	ILazyTreeWidget<T> spacing(int spacing);

	ILazyTreeWidget<T> tree(ITree<T> tree);

	ITree<T> getTreeByIndex(int index);

	ILazyTreeWidget<T> showRoot(boolean showRoot);

	ILazyTreeWidget<T> height(int height);

	ILazyTreeWidget<T> visible(boolean visible);

	ILazyTreeWidget<T> selection(T selection);

	ILazyTreeWidget<T> selectionDecorator(IDecorator decorator);

	ILazyTreeWidget<T> refresh(boolean reset);

	boolean isCollapsed(ITree<T> tree);

	ILazyTreeWidget<T> collapse(ITree<T> tree, boolean collapse);
}