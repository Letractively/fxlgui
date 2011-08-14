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
	private int height = 600;
	protected IContainer c;
	private boolean showRoot;
	protected int spacing = 0;
	protected Object selection;
	protected ILazyScrollPane pane;
	private ITree<Object> realTree;
	protected int selectionIndex = -1;
	protected IDecorator decorator;
	private int lastFirstRow = -1;
	private int width = -1;
	protected Object marked;
	int markedIndex = -1;

	public LazyTreeWidgetTemplate(IContainer c) {
		this(c, true);
	}

	public LazyTreeWidgetTemplate(IContainer c, boolean showRoot) {
		this.c = c;
		this.showRoot = showRoot;
	}

	protected int height() {
		return height;
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
	public ILazyTreeWidget<Object> marked(Object marked) {
		this.marked = marked;
		markedIndex = -1;
		return this;
	}

	protected boolean isMarked(int i) {
		if (marked != null) {
			markedIndex = tree.index(marked);
		}
		return i == markedIndex;
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
		final LazyTreeAdpList newTree = new LazyTreeAdpList(tree, showRoot);
		if (this.tree != null) {
			this.tree.copyCollapseState(newTree);
		}
		this.tree = newTree;
		return this;
	}

	@Override
	public ILazyTreeWidget<Object> height(int height) {
		this.height = height;
		if (pane != null) {
			pane.remove();
			visible(true);
		}
		return this;
	}

	@Override
	public ILazyTreeWidget<Object> visible(boolean visible) {
		if (visible) {
			show(true);
		} else
			throw new MethodNotImplementedException();
		return this;
	}

	void show(boolean checkSelection) {
		pane = (ILazyScrollPane) c.widget(ILazyScrollPane.class);
		pane.size(tree.size());
		pane.horizontalScrollPane(true);
		pane.minRowHeight(heightElement);
		pane.height(height);
		pane.decorator(this);
		pane.adjustHeights(false);
		if (width != -1)
			pane.width(width);
		if (lastFirstRow != -1) {
			pane.rowIndex(lastFirstRow);
		}
		int index = tree.index(selection);
		if (index != -1) {
			if (checkSelection) {
				pane.rowIndex(index);
			}
			selection = null;
		}
		selectionIndex = index;
		markedIndex = -1;
		pane.visible(true);
	}

	@Override
	public ILazyTreeWidget<Object> width(int width) {
		this.width = width;
		if (pane != null) {
			pane.width(width);
		}
		return this;
	}

	@Override
	public void decorate(IContainer container, int firstRow, int lastRow) {
		lastFirstRow = firstRow;
	}

	@Override
	public ILazyTreeWidget<Object> refresh(boolean reset) {
		return refresh(reset, false);
	}

	@Override
	public ILazyTreeWidget<Object> refresh(boolean reset, boolean checkSelection) {
		if (selection == null && selectionIndex != -1)
			selection = tree.row(selectionIndex).object();
		if (reset) {
			tree(realTree);
		}
		c.element().remove();
		show(checkSelection);
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
		selection = tree.object();
		selectionIndex = -1;
		this.tree.collapse(tree, collapse);
		refresh(false);
		return this;
	}

	@Override
	public int indent(ITree<Object> t) {
		return this.tree.indent(t);
	}
}
