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
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.ISplitPane;
import co.fxl.gui.api.ISplitPane.ISplitPaneResizeListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.impl.ContextMenu;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.FlipPage;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.impl.KeyAdapter;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IMenuItem.INavigationListener;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.table.util.impl.LazyScrollPaneImpl;
import co.fxl.gui.tree.api.IConstraintAdapter;
import co.fxl.gui.tree.api.ILazyTreeWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;
import co.fxl.gui.tree.api.ITypeResolver;

public class TreeWidgetImpl<T> implements ITreeWidget<T>, IResizeListener {

	// TODO Swing: native implementation: required for automated testing

	// TODO BUG: Opera & Firefox: unnecessary horizontal scrollbar for right
	// splitpane content

	// TODO FEATURE: Usability: Double click on Splitter shows optimum splitter
	// position with respect to left-side-widget (like when moving)

	// TODO Usability: Tree-Node: Klick on left/right: expand/collapse
	// TODO Usability: Tree-Node: Klick on up/down: previous/next node

	private static final String DELETE2 = "Delete";
	private static final String REFRESH2 = "Refresh";
	private static final String MOVE = "Move";
	private static final String PASTE2 = "Paste";
	private static final String COPY2 = "Copy";
	private static final String CUT2 = "Cut";
	private static final String SWITCH_TO_DETAIL_VIEW_TO_CREATE_A_NEW = "Switch to Detail View to create a new";
	private static final String ENTITY = "Entity";
	private static final String CREATE_A_NEW = "Create a new";
	private static final String NEW = "New";
	private static final String DELETE_ENTITIES = "Delete Entities";
	private static final String DELETE_ENTITY = "Delete Entity";
	static int SPLIT_POSITION = 250;
	private boolean showRefresh = true;
	private boolean showCommands = true;

	interface RefreshListener {

		void onRefresh();
	}

	class DetailView implements co.fxl.gui.tree.api.ITreeWidget.IView<T> {

		private IDecorator<T> decorator;
		private ITree<T> node;
		boolean onTop = false;
		private IVerticalPanel contentPanel;
		private IMenuItem register;
		String[] constrainType;
		String title;
		boolean isDefaultView;
		boolean deactivatedUpdate = false;
		private IConstraintAdapter<T> constraintAdapter;

		DetailView(String title, IDecorator<T> decorator, String imageResource) {
			this.decorator = decorator;
			this.title = title;
			register = registers.addNavigationItem().imageResource(
					imageResource);
			register.text(title);
			register.listener(new INavigationListener() {
				@Override
				public void onActive(boolean visible, ICallback<Void> cb) {
					onTop = visible;
					if (visible) {
						update(cb);
					} else {
						cb.onSuccess(null);
					}
				}
			});
			contentPanel = register.contentPanel();
			if (detailViews.isEmpty())
				register.active();
		}

		boolean setNode(ITree<T> node) {
			this.node = node;
			if (node == null) {
				boolean addTitle = decorator.clear(contentPanel);
				if (addTitle) {
					contentPanel.add().panel().vertical().spacing(10).add()
							.label().text("NO ENTITY SELECTED").font()
							.pixel(10).color().gray();
				}
				return false;
			}
			if (onTop) {
				update(DummyCallback.voidInstance());
				return true;
			}
			return false;
		}

		protected void update(final ICallback<Void> cb) {
			if (deactivatedUpdate) {
				cb.onSuccess(null);
				return;
			}
			if (node != null && node.object() != null) {
				IVerticalPanel flip = bottom.next().panel().vertical();
				flip.add().label().text("&#160;");
				flip.height(32);
				new WidgetTitle().styleFooter(flip);
				activeView = this;
				if (defaultViewResolver != null)
					defaultViewResolver.notifyActive(node.object(), title);
				decorator.decorate(contentPanel, flip, node,
						new CallbackTemplate<Void>(cb) {
							@Override
							public void onSuccess(Void result) {
								bottom.flip();
								cb.onSuccess(null);
							}
						}, getDetailViewWidth());
			} else {
				cb.onSuccess(null);
			}
		}

		public void enabled(boolean enabled) {
			register.enabled(enabled);
		}

		@Override
		public IView<T> constrainType(String clazz) {
			if (clazz != null) {
				this.constrainType = new String[] { clazz };
				register.enabled(false);
			}
			return this;
		}

		@Override
		public IView<T> constrainType(String[] clazz) {
			if (clazz.length > 1 || (clazz.length == 1 && clazz[0] != null)) {
				this.constrainType = clazz;
				register.enabled(false);
			}
			return this;
		}

		@Override
		public co.fxl.gui.tree.api.ITreeWidget.IViewID iD() {
			return new ViewID(title, constrainType);
		}

		@Override
		public IView<T> isDefaultView() {
			isDefaultView = true;
			return this;
		}

		public boolean enabled() {
			return register.enabled();
		}

		@Override
		public IView<T> toggleLoading(boolean toggleLoading) {
			register.toggleLoading(toggleLoading);
			return this;
		}

		@Override
		public co.fxl.gui.tree.api.ITreeWidget.IView<T> constraintAdapter(
				IConstraintAdapter<T> constraintAdapter) {
			this.constraintAdapter = constraintAdapter;
			return this;
		}

		// @Override
		// public co.fxl.gui.tree.api.ITreeWidget.IView resizeListener(
		// IResizeListener l) {
		// resizeListeners.add(l);
		// return this;
		// }
	}

	IVerticalPanel panel;
	IMenuWidget registers;
	private List<DetailView> detailViews = new LinkedList<DetailView>();
	private WidgetTitle widgetTitle;
	private IClickable<?> refresh;
	private Map<String, IClickListener> newClick = new HashMap<String, IClickListener>();
	private Map<String, IClickable<?>> newClickHyperlink = new HashMap<String, IClickable<?>>();
	private List<ISelectionListener<T>> selectionListeners = new LinkedList<ISelectionListener<T>>();
	private IClickable<?> delete;
	ISplitPane splitPane;
	private boolean hasButtons = false;
	private String defaultCreatableType = null;
	private List<String> creatableTypes = new LinkedList<String>();
	ITreeClickListener<T> treeClickListener;
	KeyAdapter<Object> treeClickAdapter;
	boolean allowCreate = true;
	private IClickListener deleteListener;
	private Map<String, String> creatableTypeIcons = new HashMap<String, String>();
	private boolean allowCutPaste = false;
	private IClickable<?> cut;
	private IClickable<?> copy;
	private IClickable<?> paste;
	private FlipPage bottom;
	CommandLink reorder;
	TreeModel<T> model;
	boolean showRoot = true;
	boolean moveActive = false;
	private IContainer container;
	int preMoveSplitPosition = -1;

	TreeWidgetImpl(IContainer layout) {
		this.container = layout;
		widgetTitle = new WidgetTitle(layout.panel(), true).space(0)
				.commandsOnTop();
		widgetTitle.foldable(false);
		widgetTitle.addToContextMenu(true);
	}

	void addButtons() {
		if (hasButtons) {
			return;
		}
		widgetTitle.hyperlinkVisible(false);
		hasButtons = true;
		if (showCommands && allowCreate) {
			if (creatableTypes.isEmpty())
				creatableTypes.add(null);
			for (final String type : creatableTypes) {
				IClickListener cl = new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						final ITree<T> parent = model.selection();
						final CallbackTemplate<ITree<T>> lCallback2 = new CallbackTemplate<ITree<T>>() {

							@Override
							public void onSuccess(ITree<T> result) {
								ITree<T> p = parent;
								if (p != null && result.parent() != null
										&& !result.parent().equals(p)) {
									p = result.parent();
								}
								if (p != null && !model.isHiddenRoot(p)
										&& lazyTree.isCollapsed(p))
									lazyTree.collapseNoRefresh(p, false);
								lazyTree.selection(result.object());
								lazyTree.refresh(true, true);
							}
						};
						if (parent != null) {
							CallbackTemplate<List<T>> lCallback1 = new CallbackTemplate<List<T>>() {
								@Override
								public void onSuccess(List<T> result) {
									if (type == null)
										parent.createNew(lCallback2);
									else
										parent.createNew(type, lCallback2);
								}
							};
							parent.loadChildren(lCallback1);
						} else {
							if (type == null)
								model.root.createNew(lCallback2);
							else
								model.root.createNew(type, lCallback2);
						}
					}
				};
				newClick.put(type, cl);
				String text = NEW + (type == null ? "" : " " + type);
				String imageResource = type == null ? Icons.NEW
						: creatableTypeIcons.get(type);
				IClickable<?> hl = widgetTitle.addHyperlink(imageResource,
						text, CREATE_A_NEW + " "
								+ (type == null ? ENTITY : type),
						SWITCH_TO_DETAIL_VIEW_TO_CREATE_A_NEW + " "
								+ (type == null ? ENTITY : type));
				newClickHyperlink.put(type, hl);
				hl.addClickListener(cl);
			}
		}
		if (showCommands) {
			if (allowCutPaste) {
				cut = widgetTitle.addHyperlink(Icons.CUT, CUT2);
				cut.addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						model.cutCopy(false);
						refreshLazyTree(false);
					}
				});
				copy = widgetTitle.addHyperlink(Icons.COPY, COPY2);
				copy.addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						model.cutCopy(true);
						refreshLazyTree(false);
					}
				});
				paste = widgetTitle.addHyperlink(Icons.PASTE, PASTE2);
				paste.addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						if (model.cutCopy().equals(model.selection())) {
							paste.clickable(false);
							model.clearCutCopy();
							previousSelection = model.selection().object();
							refreshLazyTree(true, true);
							return;
						}
						// final ITree<T> p1 = model.cutCopy().parent();
						// final ITree<T> p2 = model.selection();
						model.cutCopy().reassign(model.selection(),
								model.isCopy(),
								new CallbackTemplate<ITree<T>>() {
									@Override
									public void onSuccess(ITree<T> result) {
										// widgetTitle.reset();
										paste.clickable(false);
										model.clearCutCopy();
										lazyTree.marked(null);
										// model.refresh(p1, true);
										// model.refresh(p2, true);
										// model.selection(result);
										lazyTree.collapse(result.parent(),
												false);
										previousSelection = result.object();
										refreshLazyTree(true, true);
									}
								});
					}
				});
			}
			if (allowReorder) {
				IClickListener reorderCL = new LazyClickListener() {
					@Override
					protected void onAllowedClick() {
						preMoveSplitPosition = -1;
						previousSelection = model.selection().object();
						model.move();
						// Display.instance().title(
						// lazyTree.width() + ">"
						// + splitPane.splitPosition() + "?");
						// panel.display().invokeLater(new Runnable() {
						// @Override
						// public void run() {
						if (lazyTree.width() > splitPane.splitPosition()) {
							TreeWidgetImpl.this.preMoveSplitPosition = splitPane
									.splitPosition();
							splitPane.splitPosition(lazyTree.width()
									+ LazyScrollPaneImpl.WIDTH_SCROLL_PANEL);
						}
						// }
						// });
					}
				};
				reorder = widgetTitle.addHyperlink(Icons.MOVE, MOVE);
				reorder.addClickListener(reorderCL);
			}
			deleteListener = new LazyClickListener() {
				@Override
				public void onAllowedClick() {
					IDisplay display = panel.display();
					IDialog dl = queryDeleteEntity(display, false);
					dl.addButton().yes().addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							final ITree<T> tree = model.selection();
							// final ITree<T> parent = tree.parent();
							final ITree<T> nextSelection = model
									.nextSelection(tree);
							ICallback<T> callback = new CallbackTemplate<T>() {

								@Override
								public void onSuccess(T result) {
									model.selection(nextSelection, false);
									lazyTree.refresh(true);
									// model.refresh(parent, true);
								}
							};
							tree.delete(callback);
						}
					});
					dl.addButton().no().addClickListener(new IClickListener() {
						@Override
						public void onClick() {
						}
					});
					dl.visible(true);
				}
			};
			delete = widgetTitle.addHyperlink(co.fxl.gui.impl.Icons.CANCEL,
					DELETE2);
			delete.addClickListener(deleteListener);
			if (showRefresh && this instanceof RefreshListener)
				refresh().addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						RefreshListener refreshListener = (RefreshListener) TreeWidgetImpl.this;
						refreshListener.onRefresh();
					}
				});
		}
	}

	@Override
	public ITreeWidget<T> title(String title) {
		widgetTitle.addTitle(title).font().pixel(18);
		ContextMenu.instance(container.display()).group(title);
		return this;
	}

	IVerticalPanel panel() {
		if (panel == null) {
			panel = widgetTitle.content().panel().vertical();
			bottom = new FlipPage(widgetTitle.bottom());
		}
		return panel;
	}

	@Override
	public ITreeWidget<T> setDetailView(IDecorator<T> decorator) {
		addDetailView("Details", decorator, null);
		return this;
	}

	@Override
	public IView<T> addDetailView(String title, IDecorator<T> decorator,
			String imageResource) {
		lazySetUpRegisters();
		DetailView detailView = new DetailView(title, decorator, imageResource);
		detailViews.add(detailView);
		return detailView;
	}

	private void lazySetUpRegisters() {
		if (registers != null)
			return;
		setUpRegistersNow();
	}

	public void setUpRegistersNow() {
		IVerticalPanel panel2 = panel();
		splitPane = panel2.add().splitPane().splitPosition(SPLIT_POSITION);
		createLeftSideWidget();
		panel2.color().white();// .rgb(240, 240, 240);
		rightSideWidget = createRightSideWidget();
		registers = (IMenuWidget) rightSideWidget.add().widget(
				IMenuWidget.class);
		ResizeListenerImpl.setup(panel2.display(), this);
		onResize(-1, panel2.display().height());
		splitPane.addResizeListener(new ISplitPaneResizeListener() {
			@Override
			public void onResize(int splitPosition) {
				fireResizeSplitPane(splitPosition - 5);
			}
		});
	}

	void fireResizeSplitPane(int splitPosition) {
		if (TreeWidgetImpl.this.activeView != null) {
			DetailView view = (DetailView) TreeWidgetImpl.this.activeView;
			view.decorator.resize();
		}
		if (lazyTree != null)
			lazyTree.width(splitPosition);
	}

	private int getDetailViewWidth() {
		return rightSideWidget.width();
	}

	public IVerticalPanel createRightSideWidget() {
		// IScrollPane scrollPane =
		// splitPane.second().scrollPane().horizontal();
		// scrollPane.color().white();
		// IVerticalPanel vertical = scrollPane.viewPort().panel()
		// .vertical();
		IVerticalPanel vertical = splitPane.second().panel().vertical();
		vertical.color().white();
		return vertical;
	}

	public void createLeftSideWidget() {
		panel = splitPane.first().panel().vertical();
	}

	@Override
	public boolean onResize(int width, int height) {
		if (!splitPane.visible())
			return false;
		int offsetY = splitPane.offsetY();
		offsetY = Math.max(offsetY, 130);
		int maxFromDisplay = height - offsetY - 30;
		if (maxFromDisplay > 0) {
			splitPane.height(maxFromDisplay);
			if (lazyTree != null)
				lazyTree.height(splitPane.height());
		}
		return true;
	}

	@Override
	public ITreeWidget<T> root(ITree<T> tree) {
		return root(tree, false);
	}

	ITreeWidget<T> root(ITree<T> tree, final boolean alwaysRefresh) {
		if (previousSelection == null) {
			if (model != null && model.selection() != null) {
				previousSelection = model.selection().object();
			} else if (showRoot) {
				previousSelection = tree.object();
			} else if (tree.children().size() > 0) {
				previousSelection = tree.children().get(0).object();
			}
		}
		addButtons();
		IVerticalPanel panel2 = panel();
		if (model != null) {
			setDetailViewTree(null);
			panel2.clear();
		}
		model = new TreeModel<T>(this, tree);
		createLazyTree(tree, panel2);
		updateButtons();
		notifyChange(alwaysRefresh);
		if (runAfterVisible != null) {
			IClickListener run = newClick.get(runAfterVisible);
			runAfterVisible = null;
			run.onClick();
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	void createLazyTree(ITree<T> tree, final IVerticalPanel panel2) {
		lazyTree = (ILazyTreeWidget<T>) panel2.add().widget(
				ILazyTreeWidget.class);
		lazyTree.selectionDecorator(new ILazyTreeWidget.IDecorator() {
			@Override
			public void decorate(IContainer c, int index) {
				ITree<T> tree = lazyTree.getTreeByIndex(index);
				int indent = 0;
				ITree<T> parent = tree.parent();
				while (parent != null) {
					parent = parent.parent();
					indent++;
				}
				if (!showRoot)
					indent--;
				new TreeNode<T>(lazyTree, TreeWidgetImpl.this, c.panel()
						.vertical(), tree, indent, model.isCutCopy(tree));
				model.selection(tree, true);
			}
		});
		lazyTree.showRoot(showRoot);
		lazyTree.tree(tree);
		if (previousSelection == null && model.selection() != null)
			previousSelection = model.selection().object();
		if (previousSelection != null) {
			lazyTree.selection(previousSelection);
		}
		lazyTree.spacing(10);
		lazyTree.height(splitPane.height());
		lazyTree.selection(previousSelection);
		lazyTree.visible(true);
	}

	void refreshLazyTree(boolean r) {
		refreshLazyTree(r, false);
	}

	void refreshLazyTree(boolean r, boolean checkSelection) {
		if (previousSelection != null)
			lazyTree.selection(previousSelection);
		lazyTree.refresh(r, checkSelection);
	}

	boolean allowReorder = false;
	private IView<T> activeView;
	T previousSelection;

	// TODO potential problem in combination with click-new in MDT.DetailView
	// (if constrained)

	void setDetailViewTree(final ITree<T> tree) {
		setDetailViewTree(tree, true);
	}

	void setDetailViewTree(final ITree<T> tree, boolean refresh) {
		if (model.setDetailViewTree(tree) && !refresh)
			return;
		if (tree != null && !tree.isLoaded()) {
			tree.load(new CallbackTemplate<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result)
						expand(tree.object(), true);
					else {
						refreshTreeNode(false);
						setLoadedDetailViewTree(tree);
					}
				}
			});
		} else {
			setLoadedDetailViewTree(tree);
		}
	}

	private void setLoadedDetailViewTree(ITree<T> tree) {
		if (tree != null && model.selection() != null
				&& !model.selection().equals(tree))
			return;
		boolean showFirst = false;
		for (int i = 0; i < detailViews.size(); i++) {
			DetailView view = detailViews.get(i);
			if (tree != null) {
				String type = typeResolver.resolve(tree.object());
				boolean hide = isHide(view, type, tree.object());
				if (hide && i > 0 && view.onTop) {
					showFirst = true;
				}
			}
		}
		DetailView alreadyDecorated = null;
		for (int i = 0; i < detailViews.size(); i++) {
			DetailView view = detailViews.get(i);
			if (tree != null) {
				String type = typeResolver.resolve(tree.object());
				boolean hide = isHide(view, type, tree.object());
				boolean enabled = !hide && (i == 0 || !tree.isNew());
				view.enabled(enabled);
				if (enabled) {
					boolean updated = view.setNode(tree);
					if (updated)
						alreadyDecorated = view;
				}
			} else
				view.setNode(null);
		}
		if (detailViews.get(0).register.isActive())
			showFirst = true;
		if (showFirst || (tree != null && tree.isNew())) {
			DetailView findDefaultView = findDefaultView(tree);
			if (findDefaultView == alreadyDecorated)
				findDefaultView.deactivatedUpdate = true;
			findDefaultView.register.active();
			if (findDefaultView == alreadyDecorated)
				findDefaultView.deactivatedUpdate = false;
		}
		updateButtons();
		notifyChange();
	}

	private DetailView findDefaultView(ITree<T> tree) {
		for (DetailView view : detailViews) {
			if (view.enabled() && view.isDefaultView)
				return view;
			if (defaultViewResolver != null
					&& tree != null
					&& view != null
					&& view.title != null
					&& view.title.equals(defaultViewResolver.resolve(tree
							.object())))
				return view;
		}
		return detailViews.get(0);
	}

	@Override
	public ITreeWidget<T> refreshSelection(boolean refreshChildren) {
		for (int i = 0; i < detailViews.size(); i++) {
			DetailView view = detailViews.get(i);
			if (view.register.isActive()) {
				view.update(DummyCallback.voidInstance());
			}
		}
		return refreshTreeNode(refreshChildren);
	}

	@Override
	public ITreeWidget<T> refreshTreeNode(boolean refreshChildren) {
		model.refresh(model.selection(), refreshChildren);
		return this;
	}

	private boolean isHide(DetailView view, String type, T entity) {
		if (view.constrainType != null) {
			boolean found = false;
			for (String c : view.constrainType) {
				if (c.equals(type))
					found = true;
			}
			if (!found)
				return true;
		}
		if (view.constraintAdapter != null) {
			return !view.constraintAdapter.hasView(entity);
		}
		return false;
	}

	@Override
	public ITreeWidget<T> expand(boolean expand) {
		return this;
	}

	@Override
	public ITreeWidget<T> expand(T selection, boolean expand) {
		model.refresh(selection, expand);
		return this;
	}

	@Override
	public ITreeWidget<T> expandSelection(boolean recurse) {
		ITree<T> c = model.selection();
		expand(c);
		return this;
	}

	private void expand(ITree<T> c) {
		lazyTree.collapse(c, false);
		model.refresh(c.object(), true);
		for (ITree<T> cc : c.children()) {
			expand(cc);
		}
	}

	@Override
	public IClickable<?> addHyperlink(String title) {
		addButtons();
		return widgetTitle.addHyperlink(title);
	}

	@Override
	public ITreeWidget<T> selection(T selection) {
		if (model == null) {
			previousSelection = selection;
			return this;
		}
		addButtons();
		model.selection(selection);
		updateButtons();
		notifyChange();
		return this;
	}

	void updateButtons() {
		if (!showCommands || model == null)
			return;
		if (delete != null)
			delete.clickable(model.allowDelete());
		ITree<T> selection = model.selection();
		if (reorder != null) {
			reorder.clickable(model.allowMove() && !moveActive);
		}
		String[] creatableTypes = model.getCreatableTypes();
		List<String> ctypes = creatableTypes != null ? Arrays
				.asList(creatableTypes) : null;
		boolean clickableAtAll = selection == null || !selection.isNew();
		for (String c : newClickHyperlink.keySet()) {
			boolean b = ctypes == null || ctypes.contains(c);
			newClickHyperlink.get(c).clickable(clickableAtAll && b);
		}
		if (paste != null)
			paste.clickable(model.allowPaste());
		if (cut != null) {
			cut.clickable(model.allowCut());
		}
		if (copy != null) {
			copy.clickable(model.allowCopy());
		}
	}

	private T lastSelection;
	private String runAfterVisible = null;
	ILazyTreeWidget<T> lazyTree;
	private IDefaultViewResolver<T> defaultViewResolver;
	private IVerticalPanel rightSideWidget;
	private ITypeResolver<T> typeResolver;

	void notifyChange() {
		notifyChange(false);
	}

	void notifyChange(boolean alwaysRefresh) {
		T newSelection = model.selection() != null ? model.selection().object()
				: null;
		if (alwaysRefresh || !equals(lastSelection, newSelection)) {
			lastSelection = newSelection;
			for (ISelectionListener<T> l : selectionListeners) {
				l.onChange(newSelection);
			}
		}
	}

	private boolean equals(T lastSelection, T newSelection) {
		if (lastSelection == null)
			return newSelection == null;
		else
			return lastSelection.equals(newSelection);
	}

	@Override
	public T selection() {
		return model.selection().object();
	}

	TreeWidgetImpl<T> addRefreshListener(final RefreshListener listener) {
		addButtons();
		return this;
	}

	private IClickable<?> refresh() {
		if (refresh == null)
			refresh = widgetTitle.addHyperlink(REFRESH2);
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
		return notifyUpdate(originalObject, true);
	}

	@Override
	public ITreeWidget<T> notifyUpdate(T originalObject, boolean recurse) {
		model.refresh(originalObject, recurse);
		updateButtons();
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

	@Override
	public ITreeWidget<T> clickNew(String type) {
		if (type == null && newClick.size() > 1)
			type = defaultCreatableType;
		runAfterVisible = type;
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

	@Override
	public ITreeWidget<T> allowCutPaste(boolean allowCutPaste) {
		this.allowCutPaste = allowCutPaste;
		return this;
	}

	@Override
	public int heightRegisterPanel() {
		return registers.heightMenu();
	}

	@Override
	public ITreeWidget<T> allowReorder(boolean allowReorder) {
		this.allowReorder = allowReorder;
		return this;
	}

	@Override
	public IViewID activeDetailView() {
		if (activeView == null)
			return null;
		return activeView.iD();
	}

	@Override
	public ITreeWidget<T> activeDetailView(IViewID view) {
		if (view == null)
			return this;
		for (DetailView dv : detailViews)
			if (dv.iD().equals(view))
				dv.register.active();
		return this;
	}

	public static IDialog queryDeleteEntity(IDisplay display, boolean plural) {
		return display.showDialog()
				.message((plural ? DELETE_ENTITIES : DELETE_ENTITY) + "?")
				.warn().confirm();
	}

	@Override
	public ITreeWidget<T> navigateToParent() {
		ITree<T> t = model.selection();
		ITree<T> c = t.parent();
		model.selection(c, false);
		return this;
	}

	@Override
	public ITreeWidget<T> navigateUp() {
		return navigation(-1);
	}

	@Override
	public ITreeWidget<T> navigateDown() {
		return navigation(1);
	}

	private ITreeWidget<T> navigation(int j) {
		ITree<T> t = model.selection();
		int i = t.parent().children().indexOf(t);
		assert i >= 0 : t + " not found in " + t.parent().children();
		ITree<T> c = t.parent().children().get(i + j);
		model.selection(c, false);
		return this;
	}

	@Override
	public ITreeWidget<T> addToContextMenu(boolean addToContextMenu) {
		widgetTitle.addToContextMenu(addToContextMenu);
		return this;
	}

	@Override
	public ITreeWidget<T> repaint() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ITreeWidget<T> defaultViewResolver(
			co.fxl.gui.tree.api.ITreeWidget.IDefaultViewResolver<T> defaultViewResolver) {
		this.defaultViewResolver = defaultViewResolver;
		return this;
	}

	@Override
	public ITreeWidget<T> typeResolver(ITypeResolver<T> typeResolver) {
		this.typeResolver = typeResolver;
		return this;
	}
}
