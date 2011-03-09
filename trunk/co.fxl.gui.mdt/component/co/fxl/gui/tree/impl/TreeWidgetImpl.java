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
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog.IQuestionDialog;
import co.fxl.gui.api.IDialog.IQuestionDialog.IQuestionDialogListener;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.ISplitPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.KeyAdapter;
import co.fxl.gui.api.template.LazyClickListener;
import co.fxl.gui.api.template.ResizeListener;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IMenuItem.INavigationListener;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;

class TreeWidgetImpl<T> implements ITreeWidget<T>, IResizeListener {

	private static final int SPLIT_POSITION = 250;
	private static final int BACKGROUND_GRAY = 247;
	private boolean showRefresh = true;
	private boolean showCommands = true;

	interface RefreshListener {

		void onRefresh();
	}

	private class DetailView implements co.fxl.gui.tree.api.ITreeWidget.IView {

		private IDecorator<T> decorator;
		private Node<T> node;
		boolean onTop = false;
		private IVerticalPanel contentPanel;
		private IMenuItem register;
		private Class<?>[] constrainType;

		DetailView(String title, IDecorator<T> decorator) {
			this.decorator = decorator;
			register = registers.addNavigationItem();
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
			contentPanel.color().rgb(BACKGROUND_GRAY, BACKGROUND_GRAY,
					BACKGROUND_GRAY);
			if (detailViews.isEmpty())
				register.active();
		}

		// DetailView(IDecorator<Object> decorator) {
		// this.decorator = decorator;
		// contentPanel = detailPanel;
		// onTop = true;
		// }

		void setNode(Node<T> node) {
			this.node = node;
			if (node == null) {
				decorator.clear(contentPanel);
				return;
			}
			if (onTop) {
				update();
			}
		}

		protected void update() {
			if (node == null)
				return;
			if (node.tree.object() != null) {
				if (node.tree.object() == root.object() && !showRoot) {
					return;
				}
				decorator.decorate(contentPanel, node.tree);
			}
		}

		public void enabled(boolean enabled) {
			register.enabled(enabled);
		}

		@Override
		public void constrainType(Class<?> clazz) {
			if (clazz != null)
				this.constrainType = new Class<?>[] { clazz };
		}

		@Override
		public void constrainType(Class<?>[] clazz) {
			if (clazz.length > 1 || (clazz.length == 1 && clazz[0] != null))
				this.constrainType = clazz;
		}
	}

	private IVerticalPanel panel;
	Node<T> last;
	private IMenuWidget registers;
	private List<DetailView> detailViews = new LinkedList<DetailView>();
	// IVerticalPanel detailPanel;
	ITree<T> root;
	private WidgetTitle widgetTitle;
	private boolean expand = false;
	T selection;
	Map<T, Node<T>> object2node = new HashMap<T, Node<T>>();
	private IClickable<?> refresh;
	private Map<String, IClickListener> newClick = new HashMap<String, IClickListener>();
	private Map<String, IClickable<?>> newClickHyperlink = new HashMap<String, IClickable<?>>();
	private List<ISelectionListener<T>> selectionListeners = new LinkedList<ISelectionListener<T>>();
	private IClickable<?> delete;
	private IVerticalPanel leftContentPanel;
	// private IVerticalPanel rightContentPanel;
	private ISplitPane splitPane;
	// IScrollPane scrollPane;
	Node<T> node;
	private boolean hasButtons = false;
	private String defaultCreatableType = null;
	private List<String> creatableTypes = new LinkedList<String>();
	private boolean showRoot = true;
	ITreeClickListener<T> treeClickListener;
	KeyAdapter<Object> treeClickAdapter;
	boolean allowCreate = true;
	private IClickListener deleteListener;
	private Map<String, String> creatableTypeIcons = new HashMap<String, String>();

	TreeWidgetImpl(IContainer layout) {
		widgetTitle = new WidgetTitle(layout.panel()).space(0);
		widgetTitle.foldable(false);
		widgetTitle.holdOnClick();
	}

	void addButtons() {
		if (hasButtons) {
			return;
		}
		hasButtons = true;
		if (showCommands && allowCreate) {
			if (creatableTypes.isEmpty())
				creatableTypes.add(null);
			for (final String type : creatableTypes) {
				IClickListener cl = new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						final ITree<T> lParentNode = last != null ? last.tree
								: root;
						ChainedCallback<List<T>, ITree<T>> lCallback1 = new ChainedCallback<List<T>, ITree<T>>() {
							@Override
							public void onSuccess(List<T> result) {
								if (type == null)
									lParentNode.createNew(getNextCallback());
								else
									lParentNode.createNew(type,
											getNextCallback());
							}
						};
						CallbackTemplate<ITree<T>> lCallback2 = new CallbackTemplate<ITree<T>>() {

							public void onFail(Throwable throwable) {
								widgetTitle.reset();
								super.onFail(throwable);
							}

							@Override
							public void onSuccess(ITree<T> result) {
								widgetTitle.reset();
								selection(result.object());
								boolean rememberExpand = expand;
								expand = true;
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
						assert lParentNode != null : "Parent node cannot be resolved, cannot load children";
						lParentNode.loadChildren(lCallback1);
					}
				};
				newClick.put(type, cl);
				IClickable<?> hl = widgetTitle.addHyperlink(type == null ? null
						: creatableTypeIcons.get(type), "New"
						+ (type == null ? "" : " " + type));
				newClickHyperlink.put(type, hl);
				hl.addClickListener(cl);
			}
		}
		if (showCommands) {
			deleteListener = new IClickListener() {
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
							widgetTitle.reset();
						}

						@Override
						public void onCancel() {
							throw new MethodNotImplementedException();
						}
					});
				}
			};
			delete = widgetTitle.addHyperlink("remove.png", "Delete");
			delete.addClickListener(deleteListener);
			if (showRefresh && this instanceof RefreshListener)
				refresh().addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						((RefreshListener) TreeWidgetImpl.this).onRefresh();
						widgetTitle.reset();
					}
				});
		}
	}

	void delete() {
		final ITree<T> tree = last.tree;
		final ITree<T> parent = tree.parent();
		ICallback<T> callback = new CallbackTemplate<T>() {

			@Override
			public void onSuccess(T result) {
				showToParent(root, parent);
				// widgetTitle.reset();
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
		addDetailView("Details", decorator);
		return this;
		// setUpDetailPanel();
		// DetailView detailView = new DetailView(decorator);
		// detailViews.add(detailView);
		// return this;
	}

	@Override
	public IView addDetailView(String title, IDecorator<T> decorator) {
		setUpRegisters();
		DetailView detailView = new DetailView(title, decorator);
		detailViews.add(detailView);
		return detailView;
	}

	private void setUpRegisters() {
		if (registers != null)
			return;
		splitPane = panel().add().splitPane().splitPosition(SPLIT_POSITION);
		splitPane.border().color().lightgray();
		leftContentPanel = splitPane.first().scrollPane().viewPort().panel()
				.vertical();
		panel = leftContentPanel.spacing(10).add().panel().vertical();
		registers = (IMenuWidget) splitPane.second().widget(IMenuWidget.class);
		onResize(-1, panel.display().height());
		ResizeListener.setup(panel.display(), this);
	}

	@Override
	public void onResize(int width, int height) {
		int offsetY = splitPane.offsetY();
		int maxFromDisplay = height - offsetY - 30;
		if (maxFromDisplay > 0)
			splitPane.height(maxFromDisplay);
	}

	@Override
	public ITreeWidget<T> root(ITree<T> tree) {
		addButtons();
		root(tree, null);
		return this;
	}

	public ITreeWidget<T> root(ITree<T> tree, List<ITree<T>> path) {
		assert tree != null : "Tree cannot be null";
		if (this.root != null) {
			show(null);
			panel().clear();
		}
		if (path != null) {
			selection = path.get(path.size() - 1).object();
		}
		this.root = tree;
		node = null;
		object2node.clear();
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
			Node<T> n = getObject2node(selection);
			if (n != null)
				node = n;
			else if (!selection.equals(root.object()) || showRoot) {
				node = null;
				// new
				// MethodNotImplementedException("Selection in tree widget '"
				// + selection + "' (" + selection.getClass()
				// + ") not found in expanded tree").printStackTrace();
			} else
				node = null;
			// for (Node<T> n : object2node.values()) {
			// T object = n.tree.object();
			// if (object.equals(selection)) {
			// node = n;
			// }
			// }
			// assert node != null;
		}
		show(node);
		return this;
	}

	void show(final Node<T> node) {
		if (last != null) {
			// if (last == node)
			// return;
			last.selected(false);
		}
		showLoading(node, true, null);
	}

	void showLoading(final Node<T> node, final boolean callSelection,
			final ICallback<Void> cb) {
		if (node != null && !node.tree.isLoaded()) {
			node.tree.load(new CallbackTemplate<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					node.update(node.tree.object());
					showAfterLoad(node, callSelection);
					last = node;
					if (result)
						node.expand();
					if (cb != null)
						cb.onSuccess(null);
				}
			});
		} else {
			showAfterLoad(node, callSelection);
			last = node;
			if (cb != null)
				cb.onSuccess(null);
		}
	}

	void showAfterLoad(Node<T> node, boolean callSelection) {
		if (callSelection) {
			if (node != null && node.tree != null) {
				node.selected(true);
				selection(node.tree.object());
			} else
				selection(null);
		}
		boolean showFirst = false;
		for (int i = 0; i < detailViews.size(); i++) {
			DetailView view = detailViews.get(i);
			if (node != null) {
				Class<? extends Object> type = node.tree.object().getClass();
				boolean hide = isHide(view, type);
				if (hide && i > 0 && view.onTop) {
					showFirst = true;
				}
			}
		}
		if (showFirst || (node != null && node.tree.isNew()))
			detailViews.get(0).register.active();
		for (int i = 0; i < detailViews.size(); i++) {
			DetailView view = detailViews.get(i);
			if (node != null) {
				Class<? extends Object> type = node.tree.object().getClass();
				boolean hide = isHide(view, type);
				view.enabled(!hide && (i == 0 || !node.tree.isNew()));
			}
			view.setNode(node);
		}
	}

	private boolean isHide(DetailView view, Class<? extends Object> type) {
		if (view.constrainType == null)
			return false;
		for (Class<?> c : view.constrainType) {
			if (c.equals(type))
				return false;
		}
		return true;
	}

	@Override
	public ITreeWidget<T> expand(boolean expand) {
		this.expand = expand;
		return this;
	}

	@Override
	public ITreeWidget<T> expand(T selection, boolean expand) {
		Node<T> sNode = getObject2node(selection);
		if (expand)
			sNode.expand();
		else
			sNode.clear();
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
		boolean update = false;
		if (this.selection != null) {
			Node<T> sNode = getObject2node(this.selection);
			// assert sNode != null : this.selection + " not found in "
			// + object2node.values().toString();
			if (sNode != null)
				sNode.selected(false);
		}
		if (selection != null && root != null && root.object() != null) {
			Node<T> sNode = getObject2node(selection);
			if (sNode != null) {
				last = sNode;
				sNode.selected(true);
				update = true;
			}
			if (showCommands)
				delete.clickable(!root.object().equals(selection)
						&& (sNode != null && sNode.tree.isDeletable()));
		} else {
			if (showCommands)
				delete.clickable(false);
		}
		this.selection = selection;
		if (showCommands)
			updateCreatable();
		if (update)
			showLoading(last, false, new CallbackTemplate<Void>() {
				@Override
				public void onSuccess(Void result) {
					notifyChange();
				}
			});
		else
			notifyChange();
		return this;
	}

	void notifyChange() {
		for (ISelectionListener<T> l : selectionListeners)
			l.onChange(this.selection);
	}

	private void updateCreatable() {
		if (root == null) {
			disableAllNew();
			return;
		}
		ITree<T> tree = root;
		if (selection != null && getObject2node(selection) != null) {
			tree = getObject2node(selection).tree;
			if (tree.isNew()) {
				disableAllNew();
				return;
			}
		}
		String[] creatableTypes = tree.getCreatableTypes();
		List<String> ctypes = creatableTypes != null ? Arrays
				.asList(creatableTypes) : null;
		for (String c : newClickHyperlink.keySet()) {
			boolean b = ctypes == null || ctypes.contains(c);
			newClickHyperlink.get(c).clickable(b);
		}
	}

	private void disableAllNew() {
		for (IClickable<?> c : newClickHyperlink.values()) {
			c.clickable(false);
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

	private IClickable<?> refresh() {
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
	public ITreeWidget<T> notifyUpdate(T originalObject) {
		return notifyUpdate(originalObject, false);
	}

	@Override
	public ITreeWidget<T> notifyUpdate(T originalObject, boolean recurse) {
		assert originalObject != null : "Illegal argument for Tree.notifyUpdate";
		Node<T> n = getObject2node(originalObject);
		assert n != null : "Expanded tree node cannot be updated for object "
				+ originalObject;
		n.update(originalObject);
		if (selection == originalObject) {
			selection(originalObject);
			show(n);
		}
		updateCreatable();
		if (recurse && n.expandLoadedNode) {
			for (ITree<T> t : n.tree.children()) {
				notifyUpdate(t.object(), recurse);
			}
		}
		return this;
	}

	@Override
	public ITreeWidget<T> addCreatableType(String type) {
		if (defaultCreatableType == null)
			defaultCreatableType = type;
		creatableTypes.add(type);
		return this;
	}

	@Override
	public ITreeWidget<T> addCreatableType(String type, String image) {
		if (defaultCreatableType == null)
			defaultCreatableType = type;
		creatableTypes.add(type);
		creatableTypeIcons.put(type, image);
		return this;
	}

	void showToParent(ITree<T> tree, final ITree<T> parent) {
		last = null;
		T set = null;
		if (showRoot
				|| (parent.object() != null && root != null && !parent.object()
						.equals(root.object()))) {
			set = parent.object();
		}
		selection(set);
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
		if (node != null)
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

	@Override
	public IKey<?> addTreeClickListener(ITreeClickListener<T> listener) {
		treeClickListener = listener;
		treeClickAdapter = new KeyAdapter<Object>();
		return treeClickAdapter;
	}

	@Override
	public ITreeWidget<T> allowCreate(boolean allowCreate) {
		this.allowCreate = allowCreate;
		return this;
	}

	@Override
	public ITreeWidget<T> showCommands(boolean showCommands) {
		this.showCommands = showCommands;
		return this;
	}

	Node<T> getObject2node(T selection) {
		for (T t : object2node.keySet()) {
			Node<T> node = object2node.get(t);
			assert node != null : selection + " not found in "
					+ object2node.values();
			ITree<T> tree = node.tree;
			T object = tree.object();
			if (object.equals(selection))
				return node;
		}
		return null;
	}
}
