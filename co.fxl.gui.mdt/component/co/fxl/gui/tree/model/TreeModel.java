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
import co.fxl.gui.tree.api.ITree.ILazyList;

class TreeModel<T> {

	private ModelTreeWidget<T> widget;
	ITree<T> root;
	private ITree<T> selection;
	private boolean isCopy;
	private ITree<T> cutCopy;
	private Map<T, ModelTreeNode<T>> nodes = new HashMap<T, ModelTreeNode<T>>();

	TreeModel(ModelTreeWidget<T> widget, ITree<T> tree) {
		this.widget = widget;
		root = tree;
	}

	ITree<T> selection() {
		return selection;
	}

	void selection(ITree<T> tree) {
		selection(tree, true);
	}

	void selection(ITree<T> tree, boolean setDetailViewTree) {
		if (selection != null) {
			if (selection.equals(tree)) {
				return;
			} else {
				node(selection).selected(false);
			}
		}
		if (root.equals(tree) && !widget.showRoot) {
			tree = null;
		}
		selection = tree;
		if (selection != null) {
			ModelTreeNode<T> node = node(tree);
			node.selected(true);
		}
		if (setDetailViewTree)
			widget.setDetailViewTree(this.selection);
	}

	ITree<T> nextSelection(ITree<T> tree) {
		ILazyList<ITree<T>> children = tree.parent().children();
		int index = children.indexOf(tree);
		if (index < children.size() - 1) {
			index++;
		} else if (index > 0) {
			index--;
		} else {
			return tree.parent();
		}
		tree = children.get(index);
		return tree;
	}

	void selection(Object selection) {
		if (root.object().equals(selection) && !widget.showRoot) {
			selection = null;
		} else {
			ModelTreeNode<T> node = nodes.get(selection);
			assert node != null : selection + " not found";
			selection(node.tree);
		}
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

	ModelTreeNode<T> refresh() {
		if (widget.showRoot)
			return refresh(root, true);
		else {
			widget.root(root);
			if (widget.showRoot)
				return node(root);
			else
				return null;
		}
	}

	ModelTreeNode<T> refresh(ITree<T> tree) {
		return refresh(tree, false);
	}

	ModelTreeNode<T> refresh(ITree<T> tree, boolean recurse) {
		ModelTreeNode<T> node;
		if (tree.equals(root) && !widget.showRoot) {
			node = refresh();
		} else
			node = node(tree).refresh(recurse);
		widget.updateButtons();
		return node;
	}

	ModelTreeNode<T> refresh(T object, boolean recurse) {
		ModelTreeNode<T> node = nodes.get(object);
		assert node != null : object + " not found in " + nodes.keySet();
		ITree<T> tree = node.tree;
		refresh(tree, recurse);
		if (tree.equals(selection)) {
			widget.setDetailViewTree(tree);
		}
		return node;
	}

	boolean isCopy() {
		return isCopy;
	}

	void move() {
		boolean moveActive = node(selection).moveActive;
		widget.reorder.label(moveActive ? "Move" : "Lock");
		node(selection).moveActive = !node(selection).moveActive;
		refresh(selection);
	}

	void register(ModelTreeNode<T> node) {
		nodes.put(node.tree.object(), node);
		if (widget.previousSelection != null
				&& widget.previousSelection.equals(node.tree.object())) {
			widget.previousSelection = null;
			selection = node.tree;
			node.selected(true);
			widget.setDetailViewTree(selection);
		} else if (selection != null
				&& selection.object().equals(node.tree.object())) {
			node.selected(true);
			widget.setDetailViewTree(selection);
		}
	}

	ModelTreeNode<T> node(ITree<T> tree) {
		return nodes.get(tree.object());
	}

	boolean isCutCopy(ModelTreeNode<T> node) {
		return cutCopy != null && cutCopy.equals(node.tree);
	}

	boolean allowDelete() {
		if (selection == null)
			return false;
		if (selection.equals(root))
			return false;
		if (selection.isNew())
			return false;
		return selection.isDeletable();
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
		if (selection == null)
			return root.getCreatableTypes();
		return selection.getCreatableTypes();
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
