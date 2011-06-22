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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.table.util.api.ILazyScrollPane;
import co.fxl.gui.table.util.api.ILazyScrollPane.IDecorator;
import co.fxl.gui.tree.api.ILazyTreeWidget;
import co.fxl.gui.tree.api.ITree;

public abstract class LazyTreeWidgetTemplate implements ILazyTreeWidget,
		IDecorator {

	protected static final int HEIGHT_ELEMENT = 26;
	protected LazyTreeAdp tree;
	private int height = 600;
	protected IContainer c;
	protected List<ILazyTreeListener> listeners = new LinkedList<ILazyTreeListener>();

	public LazyTreeWidgetTemplate(IContainer c) {
		this.c = c;
	}

	@Override
	public ILazyTreeWidget tree(ITree<Object> tree) {
		this.tree = new LazyTreeAdp(tree);
		return this;
	}

	@Override
	public ILazyTreeWidget height(int height) {
		this.height = height;
		return this;
	}

	@Override
	public ILazyTreeWidget addListener(ILazyTreeListener l) {
		listeners.add(l);
		return this;
	}

	@Override
	public abstract IContainer elementAt(final int index);

	@Override
	public ILazyTreeWidget visible(boolean visible) {
		if (visible) {
			ILazyScrollPane pane = (ILazyScrollPane) c
					.widget(ILazyScrollPane.class);
			pane.size(tree.width);
			pane.minRowHeight(HEIGHT_ELEMENT);
			pane.height(height);
			pane.decorator(this);
			pane.visible(true);
		} else
			throw new MethodNotImplementedException();
		return this;
	}

	@Override
	public abstract void decorate(IContainer container, int firstRow,
			int lastRow);

	@Override
	public int rowHeight(int rowIndex) {
		return HEIGHT_ELEMENT;
	}
}
