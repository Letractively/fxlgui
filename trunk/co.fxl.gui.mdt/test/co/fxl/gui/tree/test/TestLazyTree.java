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
	private LazyListImpl<ITree<Object>> children = new LazyListImpl<ITree<Object>>();

	TestLazyTree(int i) {
		this(null, i);
	}

	TestLazyTree(String prefix, int i) {
		if (prefix != null) {
			label = prefix + "." + i;
		} else
			label = "";
		for (int j = 0; j < i; j++) {
			children.add(new TestLazyTree(label, i - 1));
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
		return children.size();
	}

	@Override
	public Object object() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ITree<Object> parent() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isLoaded() {
		throw new MethodNotImplementedException();
	}

	@Override
	public void load(ICallback<Boolean> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void loadChildren(ICallback<List<Object>> callback) {
		throw new MethodNotImplementedException();
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
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isLeaf() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isDeletable() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isUpdateable() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isReassignable() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isCopieable() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isReassignableTo(ITree<Object> tree) {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isMovable() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isCopieableTo(ITree<Object> tree) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void reassign(ITree<Object> newParent, boolean isCopy,
			ICallback<ITree<Object>> pCallback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isNew() {
		throw new MethodNotImplementedException();
	}

	@Override
	public String icon() {
		throw new MethodNotImplementedException();
	}

	@Override
	public String iconClosed() {
		throw new MethodNotImplementedException();
	}

	@Override
	public co.fxl.gui.tree.api.ITree.IDecorator decorator() {
		throw new MethodNotImplementedException();
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
