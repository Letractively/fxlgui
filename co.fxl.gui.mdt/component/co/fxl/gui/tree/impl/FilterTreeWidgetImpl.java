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

import co.fxl.gui.api.ILayout;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.mdt.api.IFilterList;
import co.fxl.gui.async.ICallback;
import co.fxl.gui.tree.api.IFilterTreeWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.impl.TreeWidgetImpl.RefreshListener;

class FilterTreeWidgetImpl<T> extends TreeWidgetImpl<T> implements
		IFilterTreeWidget<T>, IFilterListener, RefreshListener {

	ISource<T> source;
	private FilterListImpl filterList;
	private IFilterConstraints constraints;

	FilterTreeWidgetImpl(ILayout layout) {
		super(layout);
		addRefreshListener(this);
	}

	@Override
	public IFilterList<T> filterList(ILayout layout) {
		return filterList = new FilterListImpl(this, layout);
	}

	@Override
	public IFilterTreeWidget<T> source(ISource<T> source) {
		this.source = source;
		return this;
	}

	@Override
	public IFilterTreeWidget<T> visible(boolean visible) {
		filterList.apply();
		return this;
	}

	@Override
	public void onApply(IFilterConstraints constraints) {
		this.constraints = constraints;
		onRefresh();
	}

	@Override
	public void onRefresh() {
		source.query(constraints, new ICallback<ITree<T>>() {

			@Override
			public void onFail(Throwable throwable) {
				throw new MethodNotImplementedException();
			}

			@Override
			public void onSuccess(ITree<T> tree) {
				root(tree);
			}
		});
	}
}
