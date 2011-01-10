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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog.IQuestionDialog;
import co.fxl.gui.api.IDialog.IQuestionDialog.IQuestionDialogListener;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ISplitPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.ResizeListener;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IMenuItem.INavigationListener;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.tree.api.ICallback;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;

class TreeWidgetImpl<T> implements ITreeWidget<T>, IResizeListener {

	// TODO nice-2-have: double click on tree node: expand

	private static final int SPLIT_POSITION = 250;
	private static final int BACKGROUND_GRAY = 247;
	private boolean showRefresh = true;

	interface RefreshListener {

		void onRefresh();
	}

	private class DetailView {

		private IDecorator<T> decorator;
		private Node<T> node;
		private boolean onTop = false;
		private IVerticalPanel contentPanel;

		DetailView(String title, IDecorator<T> decorator) {
			this.decorator = decorator;
			IMenuItem register = registers.addNavigationItem();
			register.text(title);
			register.addListener(new INavigationListener() {

				@Override
				public void onActive(boolean visible) {
					onTop = visible;
					if (visible) {
						update();
					}
				}
			});
			contentPanel = register.contentPanel().spacing(16);
			if (detailViews.isEmpty())
				register.active();
		}

		// DetailView(IDecorator<Object> decorator) {
		// this.decorator = decorator;
		// contentPanel = detailPanel;
		// onTop = true;
		// }

		void setNode(Node<T> node) {
			if (node == null) {
				decorator.clear(contentPanel);
				return;
			}
			this.node = node;
			if (onTop) {
				update();
			}
		}

		protected void update() {
			if (node == null)
				return;
			if (node.tree.object() != null)
				decorator.decorate(contentPanel, node.tree);
		}
	}

	private IVerticalPanel panel;
	private Node<T> last;
	private IMenuWidget registers;
	private List<DetailView> detailViews = new LinkedList<DetailView>();
	IVerticalPanel detailPanel;
	ITree<T> root;
	private WidgetTitle widgetTitle;
	private boolean expand = false;
	Object selection;
	Map<Object, Node<T>> object2node = new HashMap<Object, Node<T>>();
	private ILabel refresh;
	private Map<String, IClickListener> newClick = new HashMap<String, IClickListener>();
	private Map<String, ILabel> newClickHyperlink = new HashMap<String, ILabel>();
	private List<ISelectionListener<T>> selectionListeners = new LinkedList<ISelectionListener<T>>();
	private ILabel delete;
	private IVerticalPanel leftContentPanel;
	private IVerticalPanel rightContentPanel;
	private ISplitPane splitPane;
	IScrollPane scrollPane;
	Node<T> node;
	private boolean hasButtons = false;
	private String defaultCreatableType = null;
	private List<String> creatableTypes = new LinkedList<String>();
	private boolean showRoot = true;

	TreeWidgetImpl(IContainer layout) {
		widgetTitle = new WidgetTitle(layout.panel()).space(0);
		widgetTitle.foldable(false);
	}

	void addButtons() {
		if (hasButtons) {
			return;
		}
		hasButtons = true;
		if (creatableTypes.isEmpty())
			creatableTypes.add(null);
		for (final String type : creatableTypes) {
			IClickListener cl = new IClickListener() {
				@Override
				public void onClick() {
					final ITree<T> lParentNode = last != null ? last.tree
							: root;
					ChainedCallback<List<T>, ITree<T>> lCallback1 = new ChainedCallback<List<T>, ITree<T>>() {
						@Override
						public void onSuccess(List<T> result) {
							if (type == null)
								lParentNode.createNew(getNextCallback());
							else
								lParentNode.createNew(type, getNextCallback());
						}
					};
					CallbackTemplate<ITree<T>> lCallback2 = new CallbackTemplate<ITree<T>>() {
						@Override
						public void onSuccess(ITree<T> result) {
							selection(result.object());
							boolean rememberExpand = expand;
							expand = false;
							List<ITree<T>> path = new LinkedList<ITree<T>>();
							path.add(result);
							while (result.parent() != null) {
								result = result.parent();
								path.add(0, result);
							}
							root(root, path);
							assert node != null;
							node.path = null;
							expand = rememberExpand;
						}
					};
					lCallback1.setNextCallback(lCallback2);
					lParentNode.loadChildren(lCallback1);
				}
			};
			newClick.put(type, cl);
			ILabel hl = widgetTitle.addHyperlink("New"
					+ (type == null ? "" : " " + type));
			newClickHyperlink.put(type, hl);
			hl.addClickListener(cl);
		}
		delete = widgetTitle.addHyperlink("Delete")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						IQuestionDialog question = panel.display().showDialog()
								.question();
						question.question("Delete Entity?").title("Warning");
						question.addQuestionListener(new IQuestionDialogListener() {

							@Override
							public void onYes() {
								delete();
							}

							@Override
							public void onNo() {
							}
						});
					}
				}).mouseLeft();
		if (showRefresh && this instanceof RefreshListener)
			refresh().addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					((RefreshListener) TreeWidgetImpl.this).onRefresh();
				}
			});
	}

	void delete() {
		final ITree<T> tree = last.tree;
		final ITree<T> parent = tree.parent();
		ICallback<T> callback = new ICallback<T>() {
			@Override
			public void onFail(Throwable throwable) {
				throw new MethodNotImplementedException();
			}

			@Override
			public void onSuccess(T result) {
				showToParent(root, parent);
			}
		};
		tree.delete(callback);
	}

	@Override
	public ITreeWidget<T> title(String title) {
		widgetTitle.addTitle(title).font().pixel(18);
		return this;
	}

	IVerticalPanel panel() {
		if (panel == null) {
			panel = widgetTitle.content().panel().vertical();
		}
		return panel;
	}

	@Override
	public ITreeWidget<T> setDetailView(IDecorator<T> decorator) {
		return addDetailView("Details", decorator);
		// setUpDetailPanel();
		// DetailView detailView = new DetailView(decorator);
		// detailViews.add(detailView);
		// return this;
	}

	@Override
	public ITreeWidget<T> addDetailView(String title, IDecorator<T> decorator) {
		setUpRegisters();
		DetailView detailView = new DetailView(title, decorator);
		detailViews.add(detailView);
		return this;
	}

	private void setUpRegisters() {
		if (registers != null)
			return;
		setUpDetailPanel();
		registers = (IMenuWidget) detailPanel.add().widget(IMenuWidget.class);
		registers.background(BACKGROUND_GRAY, BACKGROUND_GRAY, BACKGROUND_GRAY);
	}

	private void setUpDetailPanel() {
		if (detailPanel != null)
			return;
		splitPane = panel().add().splitPane().splitPosition(SPLIT_POSITION);
		splitPane.border().color().lightgray();
		leftContentPanel = splitPane.first().scrollPane().viewPort().panel()
				.vertical();
		panel = leftContentPanel.spacing(10).add().panel().vertical();
		scrollPane = splitPane.second().scrollPane();
		scrollPane.color().rgb(BACKGROUND_GRAY, BACKGROUND_GRAY,
				BACKGROUND_GRAY);
		// scrollPane.border().color().lightgray();
		rightContentPanel = scrollPane.viewPort().panel().vertical();
		// .spacing(10);
		detailPanel = rightContentPanel.add().panel().vertical();
		onResize(-1, panel.display().height());
		ResizeListener.setup(panel.display(), this);
	}

	@Override
	public void onResize(int width, int height) {
		int offsetY = splitPane.offsetY();
		int maxFromDisplay = height - offsetY - 30;
		splitPane.height(maxFromDisplay);
	}

	@Override
	public ITreeWidget<T> root(ITree<T> tree) {
		addButtons();
		root(tree, null);
		return this;
	}

	public ITreeWidget<T> root(ITree<T> tree, List<ITree<T>> path) {
		if (this.root != null) {
			show(null);
			panel().clear();
		}
		this.root = tree;
		node = null;
		if (showRoot) {
			Node<T> n0 = new Node<T>(this, panel(), tree, 0, expand, path);
			node = n0;
		} else {
			for (ITree<T> c : tree.children()) {
				Node<T> n0 = new Node<T>(this, panel(), c, 0, expand, path);
				if (node == null)
					node = n0;
			}
		}
		if (selection != null) {
			for (Node<T> n : object2node.values()) {
				if (n.tree.object().equals(selection)) {
					node = n;
				}
			}
			// assert node != null;
		}
		show(node);
		return this;
	}

	void show(Node<T> node) {
		if (last != null) {
			if (last == node)
				return;
			last.selected(false);
		}
		last = node;
		if (node != null) {
			node.selected(true);
			selection(node.tree.object());
		}
		for (DetailView view : detailViews) {
			view.setNode(node);
		}
	}

	@Override
	public ITreeWidget<T> expand() {
		this.expand = true;
		return this;
	}

	@Override
	public IClickable<?> addHyperlink(String title) {
		addButtons();
		return widgetTitle.addHyperlink(title);
	}

	@Override
	public ITreeWidget<T> selection(T selection) {
		addButtons();
		for (ISelectionListener<T> l : selectionListeners)
			l.onChange(selection);
		assert delete != null;
		if (selection != null && root != null && root.object() != null) {
			Node<T> sNode = object2node.get(selection);
			delete.clickable(!root.object().equals(selection)
					&& (sNode == null || sNode.tree.isDeletable()));
		}
		this.selection = selection;
		updateCreatable();
		return this;
	}

	private void updateCreatable() {
		if (root == null)
			return;
		ITree<T> tree = root;
		if (selection != null && object2node.get(selection) != null) {
			tree = object2node.get(selection).tree;
		}
		String[] creatableTypes = tree.getCreatableTypes();
		List<String> ctypes = creatableTypes != null ? Arrays
				.asList(creatableTypes) : null;
		for (String c : newClickHyperlink.keySet()) {
			boolean b = ctypes == null || ctypes.contains(c);
			newClickHyperlink.get(c).clickable(b);
		}
	}

	@Override
	public T selection() {
		if (last == null)
			return null;
		return last.tree.object();
	}

	TreeWidgetImpl<T> addRefreshListener(final RefreshListener listener) {
		addButtons();
		return this;
	}

	private ILabel refresh() {
		if (refresh == null)
			refresh = widgetTitle.addHyperlink("Refresh");
		return refresh;
	}

	@Override
	public ITreeWidget<T> clickNew() {
		return clickNew(null);
	}

	@Override
	public ITreeWidget<T> addSelectionListener(ISelectionListener<T> listener) {
		selectionListeners.add(listener);
		return this;
	}

	@Override
	public ITreeWidget<T> notifyUpdate(T originalObject, T object) {
		if (selection == originalObject)
			selection = object;
		Node<T> n = object2node.remove(originalObject);
		n.update(object);
		object2node.put(object, n);
		return this;
	}

	@Override
	public ITreeWidget<T> addCreatableType(String type) {
		if (defaultCreatableType == null)
			defaultCreatableType = type;
		creatableTypes.add(type);
		return this;
	}

	void showToParent(ITree<T> tree, final ITree<T> parent) {
		last = null;
		selection(parent.object());
		boolean rememberExpand = expand;
		expand = false;
		List<ITree<T>> path = new LinkedList<ITree<T>>();
		ITree<T> r = parent;
		path.add(r);
		while (r.parent() != null) {
			r = r.parent();
			path.add(0, r);
		}
		root(tree, path);
		node.path = null;
		expand = rememberExpand;
	}

	@Override
	public ITreeWidget<T> clickNew(String type) {
		if (type == null && newClick.size() > 1)
			type = defaultCreatableType;
		IClickListener cl = newClick.get(type);
		cl.onClick();
		return this;
	}

	@Override
	public ITreeWidget<T> hideRoot() {
		showRoot = false;
		return this;
	}

	@Override
	public ITreeWidget<T> showRefresh(boolean showRefresh) {
		this.showRefresh = showRefresh;
		return this;
	}
}
