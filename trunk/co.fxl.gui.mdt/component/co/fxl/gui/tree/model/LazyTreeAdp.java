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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.tree.api.ITree;

public class LazyTreeAdp {

	public ITree<Object> tree;
	private List<LazyTreeAdp> children = new LinkedList<LazyTreeAdp>();
	int width = 1;
	private int row = 0;
	private int rootIndex = 0;
	public int indent = 0;

	public LazyTreeAdp(ITree<Object> tree) {
		this(null, null, tree);
	}

	public LazyTreeAdp(LazyTreeAdp root, LazyTreeAdp parent, ITree<Object> tree) {
		this.tree = tree;
		if (root != null) {
			root.rootIndex++;
			row = root.rootIndex;
		}
		if (parent != null)
			indent = parent.indent + 1;
		for (ITree<Object> c : tree.children()) {
			LazyTreeAdp adp = new LazyTreeAdp(root == null ? this : root, this,
					c);
			width += adp.width;
			children.add(adp);
		}
	}

	public List<LazyTreeAdp> rows(int start, int end) {
		List<LazyTreeAdp> rows = new LinkedList<LazyTreeAdp>();
		if (row >= start && row <= end) {
			rows.add(this);
		}
		int lastRow = row + width - 1;
		if (lastRow < start || row > end)
			return rows;
		for (LazyTreeAdp child : children) {
			rows.addAll(child.rows(start, end));
		}
		return rows;
	}

	public LazyTreeAdp row(int index) {
		if (row == index) {
			return this;
		}
		int lastRow = row + width - 1;
		if (lastRow < index || index > lastRow)
			return null;
		for (LazyTreeAdp child : children) {
			LazyTreeAdp c = child.row(index);
			if (c != null)
				return c;
		}
		return null;
	}
}
