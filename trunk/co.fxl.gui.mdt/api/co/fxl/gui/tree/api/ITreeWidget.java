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
import co.fxl.gui.api.template.IPageListener;

public interface ITreeWidget<T> {

	public interface ITreeClickListener<T> {

		void onClick(ITree<T> tree);
	}

	public interface ISelectionListener<T> {

		void onChange(T selection);
	}

	public interface IDecorator<T> {

		void decorate(IVerticalPanel panel, ITree<T> tree);

		void decorate(IVerticalPanel panel, Object tree);

		void clear(IVerticalPanel contentPanel);
	}

	ITreeWidget<T> title(String title);

	IClickable<?> addHyperlink(String title);

	ITreeWidget<T> setDetailView(IDecorator<T> decorator);

	ITreeWidget<T> addDetailView(String title, IDecorator<T> decorator);

	ITreeWidget<T> root(ITree<T> tree);

	ITreeWidget<T> expand();

	ITreeWidget<T> selection(T selection);

	T selection();

	ITreeWidget<T> addSelectionListener(ISelectionListener<T> listener);

	IKey<?> addTreeClickListener(ITreeClickListener<T> listener);

	ITreeWidget<T> clickNew();

	ITreeWidget<T> clickNew(String type);

	ITreeWidget<T> notifyUpdate(T original, T object);

	ITreeWidget<T> addCreatableType(String type);

	ITreeWidget<T> hideRoot();

	ITreeWidget<T> showRefresh(boolean showRefresh);

	ITreeWidget<T> allowCreate(boolean allowCreate);

	ITreeWidget<T> pageListener(IPageListener l);
}
