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
package co.fxl.gui.tree.model;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.tree.api.ITree;

class TreeModel<T> {

	private ModelTreeWidget<T> widget;
	boolean showRoot = true;
	private ITree<T> root;
	private ITree<T> selection;
	private boolean isCopy;
	private ITree<T> cutCopy;
	private ITree<T> previousSelection;
	private Map<T, ModelTreeNode<T>> nodes = new HashMap<T, ModelTreeNode<T>>();

	TreeModel(ModelTreeWidget<T> widget, ITree<T> tree) {
		this.widget = widget;
		root = tree;
	}

	TreeModel(ModelTreeWidget<T> widget, ITree<T> tree, TreeModel<T> model) {
		this(widget, tree);
		previousSelection = model != null ? model.selection : null;
	}

	void showRoot(boolean showRoot) {
		this.showRoot = showRoot;
	}

	ITree<T> selection() {
		return selection;
	}

	void selection(ITree<T> selection) {
		if (this.selection != null) {
			if (this.selection.equals(selection)) {
				return;
			} else {
				node(this.selection).selected(false);
			}
		}
		if (this.selection != null && !this.selection.equals(selection)) {
			node(this.selection).selected(false);
		}
		this.selection = selection;
		node(selection).selected(true);
		widget.setDetailViewNode(node(selection));
	}

	void selection(Object selection, boolean recurse) {
		throw new MethodNotImplementedException();
	}

	ITree<T> cutCopy() {
		return cutCopy;
	}

	void cutCopy(boolean isCopy) {
		this.isCopy = isCopy;
		if (cutCopy != null) {
			ITree<T> lastCutCopy = cutCopy;
			cutCopy = null;
			refresh(lastCutCopy);
		}
		cutCopy = selection;
		refresh(cutCopy);
	}

	void refresh() {
		refresh(root);
	}

	void refresh(ITree<T> tree) {
		refresh(tree, false);
	}

	void refresh(ITree<T> tree, boolean recurse) {
		throw new MethodNotImplementedException();
	}

	void refresh(Object object, boolean recurse) {
		// TODO refresh tree
		// TODO refresh detail-views
		throw new MethodNotImplementedException();
	}

	boolean isCopy() {
		return isCopy;
	}

	void move() {
		node(selection).moveActive = !node(selection).moveActive;
		refresh(selection);
	}

	void register(ModelTreeNode<T> node) {
		if (previousSelection != null
				&& previousSelection.object().equals(node.tree.object())) {
			previousSelection = null;
			selection = node.tree;
			node.selected(true);
			widget.setDetailViewNode(node(selection));
		} else if (selection != null
				&& selection.object().equals(node.tree.object())) {
			node.selected(true);
			widget.setDetailViewNode(node(selection));
		}
		nodes.put(node.tree.object(), node);
	}

	ModelTreeNode<T> node(ITree<T> tree) {
		return nodes.get(tree.object());
	}

	boolean isCutCopy(ModelTreeNode<T> node) {
		return cutCopy != null && cutCopy.equals(node.tree);
	}

	boolean allowDelete() {
		throw new MethodNotImplementedException();
	}

	boolean allowMove() {
		if (selection == null)
			return false;
		if (selection.equals(root))
			return false;
		if (selection.isNew())
			return false;
		return selection.isMovable();
	}

	String[] getCreatableTypes() {
		throw new MethodNotImplementedException();
	}

	public boolean allowPaste() {
		if (cutCopy == null)
			return false;
		if (selection == null)
			return false;
		if (!isCopy) {
			return cutCopy.isReassignableTo(selection);
		} else {
			return cutCopy.isCopieableTo(selection);
		}
	}

	boolean allowCut() {
		if (selection == null)
			return false;
		return selection.isReassignable();
	}

	boolean allowCopy() {
		if (selection == null)
			return false;
		return selection.isCopieable();
	}
}
