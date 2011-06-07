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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ISplitPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.KeyAdapter;
import co.fxl.gui.api.template.LazyClickListener;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.api.template.WidgetTitle.CommandLink;
import co.fxl.gui.mdt.impl.Icons;
import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IMenuItem.INavigationListener;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;

public class ModelTreeWidget<T> implements ITreeWidget<T>, IResizeListener {

	// private static final String LOCK = "Lock";
	private static final int SPLIT_POSITION = 250;
	private static final boolean LAZY_LOAD = false;
	private boolean showRefresh = true;
	private boolean showCommands = true;

	interface RefreshListener {

		void onRefresh();
	}

	public class ViewID implements IViewID {

		private String title;
		private Class<?>[] constrainType;

		public ViewID(DetailView detailView) {
			title = detailView.title;
			constrainType = detailView.constrainType;
		}

		@Override
		public int hashCode() {
			return title.hashCode();
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object o) {
			ViewID iD = (ViewID) o;
			if (!title.equals(iD.title))
				return false;
			return equals(constrainType, iD.constrainType);
		}

		private boolean equals(Class<?>[] c1, Class<?>[] c2) {
			if (c1 == null || c2 == null)
				return c1 == c2;
			if (c1.length != c2.length)
				return false;
			for (int i = 0; i < c1.length; i++)
				if (!c1[i].equals(c2[i]))
					return false;
			return true;
		}
	}

	private class DetailView implements co.fxl.gui.tree.api.ITreeWidget.IView {

		private IDecorator<T> decorator;
		private ITree<T> node;
		boolean onTop = false;
		private IVerticalPanel contentPanel;
		private IMenuItem register;
		private Class<?>[] constrainType;
		private String title;
		boolean isDefaultView;

		DetailView(String title, IDecorator<T> decorator) {
			this.decorator = decorator;
			this.title = title;
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
			contentPanel = register.contentPanel();
			if (detailViews.isEmpty())
				register.active();
		}

		void setNode(ITree<T> node) {
			this.node = node;
			if (node == null) {
				boolean addTitle = decorator.clear(contentPanel);
				if (addTitle)
					contentPanel.add().panel().vertical().spacing(10).add()
							.label().text("NO ENTITY SELECTED").font()
							.pixel(10).color().gray();
				return;
			}
			if (onTop) {
				update();
			}
		}

		protected void update() {
			if (node != null && node.object() != null) {
				bottom.clear();
				decorator.decorate(contentPanel, bottom, node);
				activeView = this;
			}
		}

		public void enabled(boolean enabled) {
			register.enabled(enabled);
		}

		@Override
		public IView constrainType(Class<?> clazz) {
			if (clazz != null)
				this.constrainType = new Class<?>[] { clazz };
			return this;
		}

		@Override
		public IView constrainType(Class<?>[] clazz) {
			if (clazz.length > 1 || (clazz.length == 1 && clazz[0] != null))
				this.constrainType = clazz;
			return this;
		}

		@Override
		public co.fxl.gui.tree.api.ITreeWidget.IViewID iD() {
			return new ViewID(this);
		}

		@Override
		public co.fxl.gui.tree.api.ITreeWidget.IView isDefaultView() {
			isDefaultView = true;
			return this;
		}

		public boolean enabled() {
			return register.enabled();
		}
	}

	private IVerticalPanel panel;
	private IMenuWidget registers;
	private List<DetailView> detailViews = new LinkedList<DetailView>();
	private WidgetTitle widgetTitle;
	private IClickable<?> refresh;
	private Map<String, IClickListener> newClick = new HashMap<String, IClickListener>();
	private Map<String, IClickable<?>> newClickHyperlink = new HashMap<String, IClickable<?>>();
	private List<ISelectionListener<T>> selectionListeners = new LinkedList<ISelectionListener<T>>();
	private IClickable<?> delete;
	private IVerticalPanel leftContentPanel;
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
	IScrollPane leftScrollPane;
	private IVerticalPanel bottom;
	CommandLink reorder;
	TreeModel<T> model;
	boolean showRoot = true;
	private LazyScrollListener<T> scrollListener = new LazyScrollListener<T>(
			this);
	boolean moveActive = false;

	ModelTreeWidget(IContainer layout) {
		widgetTitle = new WidgetTitle(layout.panel(), true).space(0)
				.commandsOnTop();
		widgetTitle.foldable(false);
		// widgetTitle.holdOnClick();
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
						final ITree<T> parent = model.selection();
						final CallbackTemplate<ITree<T>> lCallback2 = new CallbackTemplate<ITree<T>>() {

							public void onFail(Throwable throwable) {
								// widgetTitle.reset();
								super.onFail(throwable);
							}

							@Override
							public void onSuccess(ITree<T> result) {
								// widgetTitle.reset();
								if (result.parent() != null) {
									model.refresh(result.parent(), true);
								} else
									model.refresh();
								model.selection(result);
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
				IClickable<?> hl = widgetTitle
						.addHyperlink(type == null ? Icons.NEW
								: creatableTypeIcons.get(type), "New"
								+ (type == null ? "" : " " + type));
				newClickHyperlink.put(type, hl);
				hl.addClickListener(cl);
			}
		}
		if (showCommands) {
			if (allowCutPaste) {
				cut = widgetTitle.addHyperlink(Icons.CUT, "Cut");
				cut.addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						// widgetTitle.reset();
						model.cutCopy(false);
					}
				});
				copy = widgetTitle.addHyperlink(Icons.COPY, "Copy");
				copy.addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						// widgetTitle.reset();
						model.cutCopy(true);
					}
				});
				paste = widgetTitle.addHyperlink(Icons.PASTE, "Paste");
				paste.addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						final ITree<T> p1 = model.cutCopy().parent();
						final ITree<T> p2 = model.selection();
						model.cutCopy().reassign(model.selection(),
								model.isCopy(),
								new CallbackTemplate<ITree<T>>() {
									@Override
									public void onSuccess(ITree<T> result) {
										// widgetTitle.reset();
										paste.clickable(false);
										model.refresh(p1, true);
										model.refresh(p2, true);
										model.selection(result);
									}
								});
					}
				});
			}
			if (allowReorder) {
				IClickListener reorderCL = new LazyClickListener() {

					@Override
					protected void onAllowedClick() {
						model.move();
					}
				};
				reorder = widgetTitle.addHyperlink(Icons.MOVE, "Move");
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
							final ITree<T> parent = tree.parent();
							final ITree<T> nextSelection = model
									.nextSelection(tree);
							ICallback<T> callback = new CallbackTemplate<T>() {

								@Override
								public void onSuccess(T result) {
									model.selection(nextSelection, false);
									model.refresh(parent, true);
								}
							};
							tree.delete(callback);
						}
					});
					dl.addButton().no().addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							// widgetTitle.reset();
						}
					});
					dl.visible(true);
				}
			};
			delete = widgetTitle.addHyperlink(
					co.fxl.gui.api.template.Icons.CANCEL, "Delete");
			delete.addClickListener(deleteListener);
			if (showRefresh && this instanceof RefreshListener)
				refresh().addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						((RefreshListener) ModelTreeWidget.this).onRefresh();
						// widgetTitle.reset();
					}
				});
		}
	}

	@Override
	public ITreeWidget<T> title(String title) {
		widgetTitle.addTitle(title).font().pixel(18);
		return this;
	}

	IVerticalPanel panel() {
		if (panel == null) {
			panel = widgetTitle.content().panel().vertical();
			bottom = widgetTitle.bottom().panel().vertical();
			bottom.add().label().text("&#160;");
			bottom.height(32);
			WidgetTitle.decorateGradient(bottom);
		}
		return panel;
	}

	@Override
	public ITreeWidget<T> setDetailView(IDecorator<T> decorator) {
		addDetailView("Details", decorator);
		return this;
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
		leftScrollPane = splitPane.first().scrollPane();
		leftContentPanel = leftScrollPane.viewPort().panel().vertical();
		panel = leftContentPanel.spacing(10).add().panel().vertical();
		IScrollPane scrollPane = splitPane.second().scrollPane();
		registers = (IMenuWidget) scrollPane.viewPort().widget(
				IMenuWidget.class);
		ModelResizeListener.setup(panel.display(), this);
		onResize(-1, panel.display().height());
		if (LAZY_LOAD)
			leftScrollPane.addScrollListener(scrollListener);
	}

	@Override
	public void onResize(int width, int height) {
		int offsetY = splitPane.offsetY();
		offsetY = Math.max(offsetY, 130);
		int maxFromDisplay = height - offsetY - 28;
		if (maxFromDisplay > 0)
			splitPane.height(maxFromDisplay);
	}

	@Override
	public ITreeWidget<T> root(ITree<T> tree) {
		if (previousSelection == null) {
			if (model != null && model.selection() != null) {
				previousSelection = model.selection().object();
			} else if (showRoot) {
				previousSelection = tree.object();
			} else if (tree.children().size() > 0) {
				previousSelection = tree.children().get(0).object();
			}
		}
		scrollListener.active = false;
		addButtons();
		IVerticalPanel panel2 = panel();
		if (model != null) {
			setDetailViewTree(null);
			panel2.clear();
		}
		model = new TreeModel<T>(this, tree);
		Runnable finish = new Runnable() {
			@Override
			public void run() {
				setDetailViewTree(model.selection());
			}
		};
		topLevelNodes = new LinkedList<ModelTreeNode<T>>();
		scrollListener.reset();
		if (showRoot) {
			newNode(this, panel2, tree, 0, finish, true, true);
		} else {
			Iterator<ITree<T>> it = tree.children().iterator();
			if (LAZY_LOAD) {
				while (it.hasNext()) {
					newNode(this, panel2, it.next(), 0, null, true, false);
				}
				scrollListener.active = true;
				scrollListener.onScroll(leftScrollPane.scrollOffset());
			} else {
				drawNode(it, this, panel2, 0, finish, !LAZY_LOAD);
			}
		}
		return this;
	}

	void scrollIntoView(ModelTreeNode<T> node) {
		leftScrollPane.scrollIntoView(node.image);
	}

	private int painted = 0;
	boolean allowReorder = false;
	private IView activeView;
	T previousSelection;
	List<ModelTreeNode<T>> topLevelNodes;
	private static int MAX_PAINTS = 500;

	void newNode(ModelTreeWidget<T> widget, IVerticalPanel panel,
			ITree<T> root, int depth, Runnable finish, boolean topLevel,
			boolean draw) {
		ModelTreeNode<T> node = new ModelTreeNode<T>(widget, panel, root,
				depth, draw);
		if (topLevel)
			topLevelNodes.add(node);
		painted++;
		if (painted < MAX_PAINTS) {
			if (finish != null)
				finish.run();
		} else {
			painted = 0;
			if (finish != null)
				panel.display().invokeLater(finish);
		}
	}

	private void drawNode(final Iterator<ITree<T>> it,
			final ModelTreeWidget<T> treeWidgetImpl,
			final IVerticalPanel panel2, final int i, final Runnable finish,
			final boolean draw) {
		if (!it.hasNext()) {
			finish.run();
			return;
		}
		ITree<T> c = it.next();
		newNode(this, panel(), c, 0, new Runnable() {
			@Override
			public void run() {
				drawNode(it, treeWidgetImpl, panel2, i, finish, draw);
			}
		}, true, draw);
	}

	void setDetailViewTree(final ITree<T> tree) {
		if (tree != null && !tree.isLoaded()) {
			tree.load(new CallbackTemplate<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result)
						expand(tree.object(), true);
					else
						setLoadedDetailViewTree(tree);
				}
			});
		} else {
			setLoadedDetailViewTree(tree);
		}
	}

	private void setLoadedDetailViewTree(ITree<T> tree) {
		boolean showFirst = false;
		for (int i = 0; i < detailViews.size(); i++) {
			DetailView view = detailViews.get(i);
			if (tree != null) {
				Class<? extends Object> type = tree.object().getClass();
				boolean hide = isHide(view, type);
				if (hide && i > 0 && view.onTop) {
					showFirst = true;
				}
			}
		}
		for (int i = 0; i < detailViews.size(); i++) {
			DetailView view = detailViews.get(i);
			if (tree != null) {
				Class<? extends Object> type = tree.object().getClass();
				boolean hide = isHide(view, type);
				boolean enabled = !hide && (i == 0 || !tree.isNew());
				view.enabled(enabled);
				if (enabled)
					view.setNode(tree);
			} else
				view.setNode(null);
		}
		if (detailViews.get(0).register.isActive())
			showFirst = true;
		if (showFirst || (tree != null && tree.isNew())) {
			findDefaultView(tree).register.active();
		}
		updateButtons();
		notifyChange();
	}

	private DetailView findDefaultView(ITree<T> tree) {
		for (DetailView view : detailViews) {
			if (view.enabled() && view.isDefaultView)
				return view;
		}
		return detailViews.get(0);
	}

	@Override
	public ITreeWidget<T> refreshSelection(boolean refreshChildren) {
		for (int i = 0; i < detailViews.size(); i++) {
			DetailView view = detailViews.get(i);
			if (view.register.isActive()) {
				view.update();
			}
		}
		model.refresh(model.selection(), refreshChildren);
		return this;
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
		return this;
	}

	@Override
	public ITreeWidget<T> expand(T selection, boolean expand) {
		model.refresh(selection, expand);
		return this;
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
			// if (model.allowMove()) {
			// ModelTreeNode<T> node =
			// model.node(selection);
			// if (node != null)
			// reorder.label(moveActive ? LOCK : "Move");
			// }
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

	void notifyChange() {
		T newSelection = model.selection() != null ? model.selection().object()
				: null;
		if (!equals(lastSelection, newSelection)) {
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

	ModelTreeWidget<T> addRefreshListener(final RefreshListener listener) {
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
				.message("Delete " + (plural ? "Entities" : "Entity") + "?")
				.warn().confirm();
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
		model.selection(c);
		return this;
	}
}
