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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITree.ILazyList;

class TreeModel<T> {

	private TreeWidgetImpl<T> widget;
	ITree<T> root;
	ITree<T> selection;
	private boolean isCopy;
	private ITree<T> cutCopy;
	private Map<T, NodeRef<T>> nodes = new HashMap<T, NodeRef<T>>();
	ITree<T> detailViewTree;

	TreeModel(TreeWidgetImpl<T> widget, ITree<T> tree) {
		this.widget = widget;
		root = tree;
	}

	boolean setDetailViewTree(ITree<T> tree) {
		if (detailViewTree != null && detailViewTree.equals(tree))
			return true;
		detailViewTree = tree;
		return false;
	}

	ITree<T> selection() {
		return selection;
	}

	void selection(ITree<T> tree) {
		selection(tree, true);
	}

	boolean isHiddenRoot(ITree<T> tree) {
		if (widget.showRoot)
			return false;
		return tree.equals(root);
	}

	// private boolean ignore = false;

	void selection(ITree<T> tree, boolean setDetailViewTree) {
		// if (ignore)
		// return;
		// widget.previousSelection = tree.object();
		// widget.lazyTree.refresh(false);
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
			NodeRef<T> node = node(selection);
			if (node != null) {
				node.selected(true);
				selection = node.tree();
			} else {
				throw new MethodNotImplementedException();
				// widget.previousSelection = tree.object();
				// ignore = true;
				// widget.lazyTree.refresh(false);
				// ignore = false;
			}
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

	void selection(T selection) {
		// if (root.object().equals(selection) && !widget.showRoot) {
		// this.selection = null;
		// } else {
		// NodeRef<T> node = nodes.get(selection);
		// if (node != null)
		// selection(node.tree());
		// else {
		widget.previousSelection = selection;
		widget.refreshLazyTree(false);
		// node(this.selection).selected(false);
		// }
		// }
	}

	boolean isSelected(ITree<T> t) {
		return t.equals(selection);
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
		widget.lazyTree.marked(cutCopy.object());
	}

	NodeRef<T> refresh() {
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

	NodeRef<T> refresh(ITree<T> tree) {
		return refresh(tree, false);
	}

	NodeRef<T> refresh(ITree<T> tree, boolean recurse) {
		NodeRef<T> node;
		if (tree.equals(root) && !widget.showRoot) {
			node = refresh();
		} else {
			node = node(tree).refresh(recurse);
			// if (tree.equals(selection))
			// widget.scrollIntoView(node);
		}
		widget.updateButtons();
		return node;
	}

	NodeRef<T> refresh(T object, boolean recurse) {
		NodeRef<T> node = nodes.get(object);
		if (node == null) {
			for (T t : nodes.keySet()) {
				if (t.equals(object)) {
					node = nodes.get(t);
					break;
				}
			}
		}
		widget.refreshLazyTree(true);
		if (node == null) {
			// widget.refreshLazyTree(true);
			// TODO ...
		} else {
			assert node != null : trace(object) + " not found in "
					+ trace(nodes.keySet());
			ITree<T> tree = node.tree();
			// refresh(tree, recurse);
			if (tree.equals(selection)) {
				// widget.scrollIntoView(node);
				widget.setDetailViewTree(tree);
			}
		}
		return node;
	}

	private String trace(Set<T> s) {
		if (s.isEmpty())
			return "";
		Iterator<T> it = s.iterator();
		StringBuilder b = new StringBuilder(trace(it.next()));
		while (it.hasNext())
			b.append(", " + trace(it.next()));
		return b.toString();
	}

	private String trace(T object) {
		if (object == null)
			return "null";
		return object + ":" + object.hashCode();
	}

	boolean isCopy() {
		return isCopy;
	}

	void move() {
		widget.moveActive = true;
		widget.reorder.clickable(false);
		refresh(selection);
	}

	public void moveStop() {
		widget.moveActive = false;
		widget.reorder.clickable(true);
		refresh(selection);
	}

	void register(NodeRef<T> node) {
		nodes.clear();
		nodes.put(node.tree().object(), node);
		if (widget.previousSelection != null
				&& widget.previousSelection.equals(node.tree().object())) {
			if (selection != null && node(selection) != null) {
				node(selection).selected(false);
			}
			widget.previousSelection = null;
			selection = node.tree();
			node.selected(true);
			widget.setDetailViewTree(selection, false);
		} else if (selection != null
				&& selection.object().equals(node.tree().object())) {
			selection = node.tree();
			node.selected(true);
			widget.setDetailViewTree(selection, false);
		}
	}

	NodeRef<T> node(final ITree<T> tree) {
		NodeRef<T> nodeRef = nodes.get(tree.object());
		if (nodeRef == null) {
			nodeRef = new NodeRef<T>() {

				@Override
				public ITree<T> tree() {
					return tree;
				}

				@Override
				public void selected(boolean selected) {
					if (!selected)
						return;
					widget.previousSelection = tree.object();
					widget.refreshLazyTree(false);
				}

				@Override
				public NodeRef<T> refresh(boolean recurse) {
					widget.refreshLazyTree(false);
					return this;
				}
			};
		}
		return nodeRef;
	}

	boolean isCutCopy(TreeNode<T> node) {
		return isCutCopy(node.tree);
	}

	boolean isCutCopy(ITree<T> tree) {
		return cutCopy != null && cutCopy.equals(tree);
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
		return allowMove(selection);
	}

	boolean allowMove(ITree<T> selection) {
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
		if (selection.isNew())
			return false;
		if (cutCopy.equals(selection))
			return true;
		if (!isCopy) {
			return cutCopy.isReassignableTo(selection);
		} else {
			return cutCopy.isCopieableTo(selection);
		}
	}

	boolean allowCut() {
		if (selection == null)
			return false;
		return selection.isReassignable() && !selection.isNew();
	}

	boolean allowCopy() {
		if (selection == null)
			return false;
		return selection.isCopieable() && !selection.isNew();
	}

	public void clearCutCopy() {
		cutCopy = null;
		widget.lazyTree.marked(null);
	}
}
