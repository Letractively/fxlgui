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

import java.util.List;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.ICallback;

public interface ITree<T> {

	public interface IDecorator {

		void decorate(ILabel label);
	}

	String name();

	T object();

	int childCount();

	List<ITree<T>> children();

	ITree<T> parent();

	boolean isLoaded();

	void load(ICallback<Boolean> callback);

	void loadChildren(ICallback<List<T>> callback);

	void createNew(ICallback<ITree<T>> callback);

	void createNew(String type, ICallback<ITree<T>> callback);

	void delete(ICallback<T> callback);

	void save(ITree<T> node, final ICallback<T> pCallback);

	String[] getCreatableTypes();

	boolean isLeaf();

	boolean isDeletable();

	boolean isUpdateable();

	boolean isReassignable();

	boolean isCopieable();

	boolean isReassignableTo(ITree<T> tree);

	boolean isMovable();

	boolean isCopieableTo(ITree<T> tree);

	void reassign(ITree<T> newParent, boolean isCopy,
			final ICallback<ITree<T>> pCallback);

	boolean isNew();

	String icon();

	String iconClosed();

	IDecorator decorator();

	void moveUp(CallbackTemplate<Void> callback);

	void moveDown(CallbackTemplate<Void> callback);

	void moveTop(CallbackTemplate<Void> callback);

	void moveBottom(CallbackTemplate<Void> callback);
}
