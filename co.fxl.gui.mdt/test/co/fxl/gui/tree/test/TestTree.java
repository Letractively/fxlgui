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

import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.tree.api.ITree;

class TestTree implements ITree<String> {

	private String path;
	private int id;
	List<ITree<String>> children = new LinkedList<ITree<String>>();

	TestTree(TestTree parent, String path, int id) {
		this.path = path;
		this.id = id;
		if (path.length() > 12)
			return;
		for (int i = 0; i < 4; i++)
			children.add(new TestTree(this, name(), i));
	}

	@Override
	public List<ITree<String>> children() {
		return children;
	}

	@Override
	public String name() {
		return path + "." + id;
	}

//	@Override
//	public boolean canLoadChildren() {
//		return canLoadChildren;
//	}
//
//	@Override
//	public void loadChildren() {
//		canLoadChildren = false;
//		for (int i = 4; i < 8; i++)
//			children.add(new TestTree(this, name(), i));
//	}

	@Override
	public String object() {
		return name();
	}

//	@Override
//	public ITree<String> createNew() {
//		TestTree tree = new TestTree(this, name(), children.size());
//		children.add(tree);
//		return tree;
//	}
//
//	@Override
//	public void delete() {
//		parent.delete(this);
//	}

	@Override
	public ITree<String> parent() {
		throw new MethodNotImplementedException();
	}

	@Override
	public int childCount() {
		return children.size();
	}

	@Override
	public void createNew(ICallback<ITree<String>> callback) {
		throw new MethodNotImplementedException();
		
	}

	@Override
	public void delete(ICallback<String> callback) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void loadChildren(ICallback<List<String>> callback) {
		throw new MethodNotImplementedException();		
	}

	@Override
	public void save(String node) {
		throw new MethodNotImplementedException();
	}
}