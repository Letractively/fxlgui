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
package co.fxl.gui.mdt.test;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.mdt.api.IList;
import co.fxl.gui.mdt.api.IMasterDetailTableWidget.IContent;
import co.fxl.gui.async.ICallback;
import co.fxl.gui.tree.api.ITree;

class TestContent implements IContent<String> {

	class Leaf implements ITree<String> {

		private String object;

		Leaf(Extract parent, String s) {
			object = s;
		}

		@Override
		public int childCount() {
			return 0;
		}

		@Override
		public List<ITree<String>> children() {
			return new LinkedList<ITree<String>>();
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
		public String name() {
			return object();
		}

		@Override
		public String object() {
			return object;
		}

		@Override
		public ITree<String> parent() {
			throw new MethodNotImplementedException();
		}
	}

	class Extract implements ITree<String> {

		private int i;
		private List<ITree<String>> children = new LinkedList<ITree<String>>();

		Extract(Root root, int i, List<String> extract) {
			this.i = i;
			for (String s : extract) {
				children.add(new Leaf(this, s));
			}
		}

		@Override
		public int childCount() {
			return children.size();
		}

		@Override
		public List<ITree<String>> children() {
			return children;
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
		public String name() {
			return object();
		}

		@Override
		public String object() {
			return "Partition " + i;
		}

		@Override
		public ITree<String> parent() {
			throw new MethodNotImplementedException();
		}

	}

	class Root implements ITree<String> {

		private List<ITree<String>> children = new LinkedList<ITree<String>>();

		Root(List<String> list) {
			for (int i = 0; i < 10; i++) {
				List<String> extract = extract(list, i);
				if (!extract.isEmpty()) {
					children.add(new Extract(this, i + 1, extract));
				}
			}
		}

		private List<String> extract(List<String> list, int i) {
			List<String> extract = new LinkedList<String>();
			for (int k = i * 10; k < (i + 1) * 10; k++) {
				if (list.contains(getString(k))) {
					extract.add(getString(k));
				}
			}
			return extract;
		}

		
		@Override
		public int childCount() {
			return children.size();
		}

		@Override
		public List<ITree<String>> children() {
			return children;
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
		public String name() {
			return object();
		}

		@Override
		public String object() {
			return "Root";
		}

		@Override
		public ITree<String> parent() {
			throw new MethodNotImplementedException();
		}

	}

	private List<String> entities = new LinkedList<String>();

	TestContent() {
		for (int i = 0; i < 100; i++)
			entities.add(getString(i));
	}

	@Override
	public void queryList(final IFilterConstraints constraints,
			ICallback<IList<String>> callback) {
		callback.onSuccess(new IList<String>() {

			@Override
			public IList<String> delete(String entity) {
				entities.remove(entity);
				return this;
			}

			@Override
			public List<String> jdkList() {
				List<String> result = new LinkedList<String>();
				l: for (int i = 0; i < constraints.size()
						&& i < entities.size(); i++) {
					String e = entities.get(i);
					for (int k = 1; k < 2; k++) {
						if (constraints
								.isAttributeConstrained(MasterDetailTableWidgetTest
										.propertyNameOf(k))) {
							String name = MasterDetailTableWidgetTest
									.valueNameOf(e);
							if (!name.startsWith(constraints
									.stringValue(MasterDetailTableWidgetTest
											.propertyNameOf(k))))
								continue l;
						}
						if (constraints
								.isAttributeConstrained(MasterDetailTableWidgetTest
										.propertyDescriptionOf(k))) {
							String description = MasterDetailTableWidgetTest
									.valueDescriptionOf(e);
							if (!description.startsWith(constraints
									.stringValue(MasterDetailTableWidgetTest
											.propertyDescriptionOf(k))))
								continue l;
						}
					}
					result.add(e);
				}
				return result;
			}
		});
	}

	@Override
	public void queryTree(IFilterConstraints constraints,
			final ICallback<ITree<String>> callback) {
		queryList(constraints, new ICallback<IList<String>>() {

			@Override
			public void onFail(Throwable throwable) {
				callback.onFail(throwable);
			}

			@Override
			public void onSuccess(IList<String> result) {
				List<String> list = result.jdkList();
				callback.onSuccess(new Root(list));
			}
		});
	}

	private String getString(int k) {
		return "Entity " + k;
	}

	@Override
	public void queryRelation(String entity, String name,
			ICallback<List<String>> callback) {
		throw new MethodNotImplementedException();
	}
}
