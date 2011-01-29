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
package co.fxl.gui.tree.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.tree.api.IFilterList;
import co.fxl.gui.tree.api.IFilterTreeWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.impl.TreeWidgetImpl.RefreshListener;

class FilterTreeWidgetImpl<T> extends TreeWidgetImpl<T> implements
		IFilterTreeWidget<T>, IFilterListener, RefreshListener {

	ISource<T> source;
	private FilterListImpl<T> filterList;
	private IFilterConstraints constraints;

	FilterTreeWidgetImpl(IContainer layout) {
		super(layout);
	}

	@Override
	public IFilterList<T> filterList(ILayout layout) {
		return filterList = new FilterListImpl<T>(this, layout);
	}

	@Override
	public IFilterTreeWidget<T> source(ISource<T> source) {
		this.source = source;
		return this;
	}

	@Override
	public IFilterTreeWidget<T> visible(boolean visible) {
		addButtons();
		if (filterList != null)
			filterList.apply();
		else {
			onRefresh();
		}
		return this;
	}

	@Override
	public void onApply(IFilterConstraints constraints) {
		this.constraints = constraints;
		onRefresh();
	}

	@Override
	public void onRefresh() {
		source.query(constraints, new CallbackTemplate<ITree<T>>() {

			@Override
			public void onSuccess(ITree<T> tree) {
				Node<T> n = object2node.get(selection);
				if (n != null) {
					assert n.tree != null : "Tree cannot be null";
					ITree<T> p = n.tree;
					showToParent(tree, p);
				} else
					root(tree);
			}
		});
	}

	@Override
	public IFilterTreeWidget<T> refresh(
			final co.fxl.gui.api.template.ICallback<Boolean> cb) {
		source.query(constraints, new CallbackTemplate<ITree<T>>() {

			@Override
			public void onSuccess(ITree<T> tree) {
				Node<T> n = object2node.get(selection);
				if (n != null) {
					ITree<T> p = n.tree;
					assert p != null : "Tree cannot be null";
					showToParent(tree, p);
				} else
					root(tree);
				if (cb != null)
					cb.onSuccess(true);
			}
		});
		return this;
	}
}
