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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.tree.api.ITree;

public class LazyTreeAdpList {

	private List<ITree<Object>> rows = new ArrayList<ITree<Object>>();
	private boolean showRoot;

	public LazyTreeAdpList(ITree<Object> tree, boolean showRoot) {
		this.showRoot = showRoot;
		if (showRoot) {
			generateRows(tree);
		} else {
			for (ITree<Object> c : tree.children()) {
				generateRows(c);
			}
		}
	}

	private void generateRows(ITree<Object> tree) {
		rows.add(tree);
		for (ITree<Object> c : tree.children())
			generateRows(c);
	}

	public List<ITree<Object>> rows(int start, int end) {
		if (rows.isEmpty())
			return new LinkedList<ITree<Object>>();
		return rows.subList(start, end + 1);
	}

	public ITree<Object> row(int index) {
		return rows.get(index);
	}

	public int index(Object selection) {
		for (int i = 0; i < size(); i++)
			if (row(i).object().equals(selection))
				return i;
		return -1;
	}

	boolean isCollapsed(ITree<Object> tree) {
		if (tree.childCount() > 0) {
			int index = index(tree.object());
			if (index < size() - 1) {
				return !row(index + 1).parent().equals(tree);
			} else {
				return true;
			}
		}
		return false;
	}

	void clearChildren(ITree<Object> tree) {
		int i = index(tree.object()) + 1;
		while (i < size() && row(i).parent().equals(tree)) {
			rows.remove(i);
		}
	}

	void collapse(ITree<Object> tree, boolean collapse) {
		if (collapse) {
			for (ITree<Object> c : tree.children()) {
				if (!c.children().isEmpty()) {
					collapse(c, true);
				}
				rows.remove(c);
			}
		} else {
			List<ITree<Object>> cs = new LinkedList<ITree<Object>>();
			for (ITree<Object> c : tree.children())
				cs.add(c);
			int i = index(tree.object());
			assert i >= 0 : tree + " not found in " + rows;
			int index = i + 1;
			if (index < size())
				rows.addAll(index, cs);
			else
				rows.addAll(cs);
		}
	}

	public int size() {
		return rows.size();
	}

	public int indent(ITree<Object> row) {
		int indent = showRoot ? 0 : -1;
		while (row.parent() != null) {
			row = row.parent();
			indent++;
		}
		return indent;
	}

	void copyCollapseState(LazyTreeAdpList newTree) {
		int index = 0;
		for (int i = 0; i < size(); i++) {
			ITree<Object> t = row(i);
			if (t.childCount() > 0) {
				if (index < size() - 1) {
					if (!row(index + 1).parent().equals(t)) {
						newTree.collapse(t, true);
					}
				} else {
					newTree.collapse(t, true);
				}
			}
			index++;
		}
	}
}
