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
import co.fxl.gui.table.util.api.ILazyScrollPane;
import co.fxl.gui.table.util.api.ILazyScrollPane.IDecorator;
import co.fxl.gui.tree.api.ILazyTreeWidget;
import co.fxl.gui.tree.api.ITree;

public abstract class LazyTreeWidgetTemplate implements
		ILazyTreeWidget<Object>, IDecorator {

	protected int heightElement = 22;
	protected LazyTreeAdpList tree;
	protected int height = 600;
	protected IContainer c;
	private boolean showRoot;
	protected int spacing = 0;
	protected Object selection;
	protected ILazyScrollPane pane;
	private ITree<Object> realTree;
	protected int elementAt = -1;
	protected IDecorator decorator;

	public LazyTreeWidgetTemplate(IContainer c) {
		this(c, true);
	}

	public LazyTreeWidgetTemplate(IContainer c, boolean showRoot) {
		this.c = c;
		this.showRoot = showRoot;
	}

	@Override
	public ILazyTreeWidget<Object> selectionDecorator(IDecorator dec) {
		decorator = dec;
		return this;
	}

	@Override
	public ILazyTreeWidget<Object> selection(Object selection) {
		this.selection = selection;
		return this;
	}

	@Override
	public ITree<Object> getTreeByIndex(int index) {
		ITree<Object> row = tree.row(index);
		assert row != null : "row " + index + " not found in tree " + tree;
		return row;
	}

	@Override
	public ILazyTreeWidget<Object> showRoot(boolean showRoot) {
		this.showRoot = showRoot;
		return this;
	}

	@Override
	public ILazyTreeWidget<Object> spacing(int spacing) {
		this.spacing = spacing;
		return this;
	}

	@Override
	public ILazyTreeWidget<Object> tree(ITree<Object> tree) {
		this.realTree = tree;
		this.tree = new LazyTreeAdpList(tree, showRoot);
		return this;
	}

	@Override
	public ILazyTreeWidget<Object> height(int height) {
		this.height = height;
		return this;
	}

	@Override
	public ILazyTreeWidget<Object> visible(boolean visible) {
		if (visible) {
			pane = (ILazyScrollPane) c.widget(ILazyScrollPane.class);
			pane.size(tree.size());
			pane.horizontalScrollPane(true);
			pane.minRowHeight(heightElement);
			pane.height(height);
			pane.decorator(this);
			int index = tree.index(selection);
			if (index != -1) {
				pane.rowIndex(index);
				selection = null;
			}
			elementAt = index;
			pane.visible(true);
		} else
			throw new MethodNotImplementedException();
		return this;
	}

	@Override
	public ILazyTreeWidget<Object> refresh(boolean reset) {
		if (selection == null && elementAt != -1)
			selection = tree.row(elementAt).object();
		if (reset) {
			tree(realTree);
		}
		c.element().remove();
		visible(true);
		return this;
	}

	@Override
	public int rowHeight(int rowIndex) {
		return heightElement;
	}

	@Override
	public boolean isCollapsed(ITree<Object> tree) {
		return this.tree.isCollapsed(tree);
	}

	@Override
	public ILazyTreeWidget<Object> collapse(ITree<Object> tree, boolean collapse) {
		this.tree.collapse(tree, collapse);
		return this;
	}
}
