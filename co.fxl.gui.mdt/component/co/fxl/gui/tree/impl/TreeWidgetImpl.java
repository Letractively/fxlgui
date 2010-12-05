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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ISplitPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.ChainedCallback;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.ResizeListener;
import co.fxl.gui.api.template.UiCallback;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IMenuItem.INavigationListener;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;

class TreeWidgetImpl<T> implements ITreeWidget<T>, IResizeListener {

	private static final int BACKGROUND_GRAY = 247;

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
			decorator.decorate(contentPanel, node.tree.object());
		}
	}

	private IVerticalPanel panel;
	private Node<T> last;
	private IMenuWidget registers;
	private List<DetailView> detailViews = new LinkedList<DetailView>();
	IVerticalPanel detailPanel;
	private ITree<T> root;
	private WidgetTitle widgetTitle;
	private boolean expand = false;
	private Object selection;
	Map<Object, Node<T>> object2node = new HashMap<Object, Node<T>>();
	private ILabel refresh;
	private IClickListener newClick;
	private List<ISelectionListener<T>> selectionListeners = new LinkedList<ISelectionListener<T>>();
	private ILabel delete;
	private IVerticalPanel leftContentPanel;
	private IVerticalPanel rightContentPanel;
	private ISplitPane splitPane;
	IScrollPane scrollPane;
	private Node<T> node;

	TreeWidgetImpl(IContainer layout) {
		widgetTitle = new WidgetTitle(layout.panel()).space(0);
		widgetTitle.foldable(false);
		widgetTitle.addHyperlink("New").addClickListener(
				newClick = new IClickListener() {
					@Override
					public void onClick() {
						final ITree<T> lParentNode = last.tree;
						ChainedCallback<List<T>, ITree<T>> lCallback1 = new ChainedCallback<List<T>, ITree<T>>() {
							@Override
							public void onSuccess(List<T> result) {
								lParentNode.createNew(getNextCallback());
							}
						};
						UiCallback<ITree<T>> lCallback2 = new UiCallback<ITree<T>>() {
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
								node.path = null;
								expand = rememberExpand;
							}
						};
						lCallback1.setNextCallback(lCallback2);
						lParentNode.loadChildren(lCallback1);
					}
				});
		delete = widgetTitle.addHyperlink("Delete")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						final ITree<T> parent = last.tree.parent();
						ICallback<T> callback = new ICallback<T>() {
							@Override
							public void onFail(Throwable throwable) {
								throw new MethodNotImplementedException();
							}

							@Override
							public void onSuccess(T result) {
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
								root(root, path);
								node.path = null;
								expand = rememberExpand;
							}
						};
						last.tree.delete(callback);
					}
				}).mouseLeft();
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
		splitPane = panel().add().splitPane().splitPosition(300);
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
		return root(tree, null);
	}

	public ITreeWidget<T> root(ITree<T> tree, List<ITree<T>> path) {
		if (this.root != null) {
			show(null);
			panel().clear();
		}
		this.root = tree;
		node = new Node<T>(this, panel(), tree, 0, expand, path);
		if (selection != null) {
			node = object2node.get(selection);
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
		return widgetTitle.addHyperlink(title);
	}

	@Override
	public ITreeWidget<T> selection(T selection) {
		this.selection = selection;
		for (ISelectionListener<T> l : selectionListeners)
			l.onChange(selection);
		if (selection != null && root != null)
			delete.clickable(!root.object().equals(selection));
		return this;
	}

	@Override
	public T selection() {
		if (last == null)
			return null;
		return last.tree.object();
	}

	@SuppressWarnings("unchecked")
	TreeWidgetImpl addRefreshListener(final RefreshListener listener) {
		refresh().addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				listener.onRefresh();
			}
		});
		return this;
	}

	private ILabel refresh() {
		if (refresh == null)
			refresh = widgetTitle.addHyperlink("Refresh");
		return refresh;
	}

	@Override
	public ITreeWidget<T> clickNew() {
		newClick.onClick();
		return this;
	}

	@Override
	public ITreeWidget<T> addSelectionListener(ISelectionListener<T> listener) {
		selectionListeners.add(listener);
		return this;
	}

	@Override
	public ITreeWidget<T> notifyUpdate(T object) {
		object2node.get(object).update();
		return this;
	}
}
