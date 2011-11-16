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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.ICallback;

public interface ITreeWidget<T> {

	public interface IDefaultViewResolver<T> {

		String resolve(T entity);

		void notifyActive(T entity, String view);

	}

	public interface IViewID {

	}

	public interface IView<T> {

		IView<T> constrainType(String clazz);

		IView<T> constrainType(String[] clazz);

		IView<T> isDefaultView();

		IView<T> toggleLoading(boolean toggleLoading);

		IViewID iD();

		IView<T> constraintAdapter(IConstraintAdapter<T> constraintAdapter);
	}

	public interface ITreeClickListener<T> {

		void onClick(ITree<T> tree);
	}

	public interface ISelectionListener<T> {

		void onChange(T selection);
	}

	public interface IDecorator<T> {

		void decorate(IVerticalPanel panel, IVerticalPanel bottom,
				ITree<T> tree, ICallback<Void> cb, int width);

		void decorate(IVerticalPanel panel, IVerticalPanel bottom, Object tree,
				ICallback<Void> cb, int width);

		boolean clear(IVerticalPanel contentPanel);

		void resize();
	}

	ITreeWidget<T> title(String title);

	ITreeWidget<T> addToContextMenu(boolean addToContextMenu);

	IClickable<?> addHyperlink(String title);

	ITreeWidget<T> setDetailView(IDecorator<T> decorator);

	IView<T> addDetailView(String title, IDecorator<T> decorator,
			String imageResource);

	IViewID activeDetailView();

	ITreeWidget<T> activeDetailView(IViewID view);

	ITreeWidget<T> root(ITree<T> tree);

	ITreeWidget<T> expand(boolean expand);

	ITreeWidget<T> expand(T current, boolean expand);

	ITreeWidget<T> selection(T selection);

	T selection();

	ITreeWidget<T> addSelectionListener(ISelectionListener<T> listener);

	IKey<?> addTreeClickListener(ITreeClickListener<T> listener);

	ITreeWidget<T> clickNew();

	ITreeWidget<T> clickNew(String type);

	ITreeWidget<T> notifyUpdate(T originalObject);

	ITreeWidget<T> notifyUpdate(T originalObject, boolean recurse);

	ITreeWidget<T> addCreatableType(String type);

	ITreeWidget<T> addCreatableType(String s, String string);

	ITreeWidget<T> hideRoot();

	ITreeWidget<T> showRefresh(boolean showRefresh);

	ITreeWidget<T> allowCreate(boolean allowCreate);

	ITreeWidget<T> allowCutPaste(boolean allowCutPaste);

	ITreeWidget<T> allowReorder(boolean allowReorder);

	ITreeWidget<T> showCommands(boolean showCommands);

	ITreeWidget<T> refreshSelection(boolean refreshChildren);

	ITreeWidget<T> refreshTreeNode(boolean refreshChildren);

	ITreeWidget<T> defaultViewResolver(IDefaultViewResolver<T> viewResolver);

	ITreeWidget<T> repaint();

	int heightRegisterPanel();

	ITreeWidget<T> typeResolver(ITypeResolver<T> typeResolver);

	ITreeWidget<T> navigateToParent();

	ITreeWidget<T> navigateUp();

	ITreeWidget<T> navigateDown();

	ITreeWidget<T> expandSelection(boolean recurse);
}
