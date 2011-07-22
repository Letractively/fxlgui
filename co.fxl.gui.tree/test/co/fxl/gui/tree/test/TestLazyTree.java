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
package co.fxl.gui.tree.test;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.impl.ICallback;
import co.fxl.gui.tree.api.ITree;

class TestLazyTree implements ITree<Object> {

	public class LazyListImpl<T> extends LinkedList<T> implements ILazyList<T> {
		private static final long serialVersionUID = -1897500793221385532L;
	}

	private String label;
	private List<ITree<Object>> childrenList = new LinkedList<ITree<Object>>();
	private LazyListImpl<ITree<Object>> children = new LazyListImpl<ITree<Object>>();
	private ITree<Object> parent;
	private int i;

	TestLazyTree(int i) {
		this(null, null, i);
	}

	@Override
	public int hashCode() {
		return name().hashCode();
	}

	TestLazyTree(ITree<Object> parent, String prefix, int i) {
		this.parent = parent;
		if (prefix != null) {
			label = prefix + "." + i;
		} else
			label = "";
		this.i = i;
		if (i > 2)
			addChildren();
	}

	void addChildren() {
		for (int j = 0; j < i; j++) {
			TestLazyTree testLazyTree = new TestLazyTree(this, label, i - 1);
			childrenList.add(testLazyTree);
			children.add(testLazyTree);
		}
	}

	@Override
	public String name() {
		return "Node #" + label;
	}

	@Override
	public ILazyList<ITree<Object>> children() {
		return children;
	}

	@Override
	public int childCount() {
		return i;
	}

	@Override
	public Object object() {
		return this;
	}

	@Override
	public ITree<Object> parent() {
		return parent;
	}

	@Override
	public boolean isLoaded() {
		return true;
	}

	@Override
	public void load(ICallback<Boolean> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void loadChildren(final ICallback<List<Object>> callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				addChildren();
				callback.onSuccess(null);
			}
		}).start();
	}

	@Override
	public void createNew(ICallback<ITree<Object>> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void createNew(String type, ICallback<ITree<Object>> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void delete(ICallback<Object> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void save(ITree<Object> node, ICallback<Object> pCallback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public String[] getCreatableTypes() {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public boolean isDeletable() {
		return false;
	}

	@Override
	public boolean isUpdateable() {
		return false;
	}

	@Override
	public boolean isReassignable() {
		return false;
	}

	@Override
	public boolean isCopieable() {
		return false;
	}

	@Override
	public boolean isReassignableTo(ITree<Object> tree) {
		return false;
	}

	@Override
	public boolean isMovable() {
		return false;
	}

	@Override
	public boolean isCopieableTo(ITree<Object> tree) {
		return false;
	}

	@Override
	public void reassign(ITree<Object> newParent, boolean isCopy,
			ICallback<ITree<Object>> pCallback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public String icon() {
		return "save.png";
	}

	@Override
	public String iconClosed() {
		return "save.png";
	}

	@Override
	public co.fxl.gui.tree.api.ITree.IDecorator decorator() {
		return null;
	}

	@Override
	public void moveUp(ICallback<Void> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void moveDown(ICallback<Void> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void moveTop(ICallback<Void> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void moveBottom(ICallback<Void> callback) {
		throw new MethodNotImplementedException();
	}

}
