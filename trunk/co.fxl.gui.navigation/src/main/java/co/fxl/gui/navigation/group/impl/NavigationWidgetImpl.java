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
package co.fxl.gui.navigation.group.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IBordered.IBorder.IBorderStyle;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.HorizontalScalingPanel;
import co.fxl.gui.impl.IServerListener;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.ResizableWidgetTemplate;
import co.fxl.gui.impl.ServerListener;
import co.fxl.gui.impl.Shell;
import co.fxl.gui.navigation.api.ITabWidget;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.group.api.INavigationWidget;
import co.fxl.gui.navigation.impl.TabDecoratorTemplate;

public class NavigationWidgetImpl extends ResizableWidgetTemplate implements
		INavigationWidget, IServerListener {

	private class Action {

		private IClickListener listener;
		private IConstraint c;

		private Action(IClickListener clickListener, IConstraint c) {
			listener = clickListener;
			this.c = c;
		}
	}

	private static final boolean DYNAMIC_RESIZE = true;
	protected static final boolean DRAW_MORE_TOP = !Env.is(Env.IE);
	private static final boolean FIX_SEPARATOR_BORDER = Env.is(Env.IE);
	private static boolean ADD_SEPARATORBORDER = !Env.is(Env.SWING);
	protected IDockPanel mainPanel;
	IHorizontalPanel navigationPanel;
	private ICardPanel history;
	NavigationItemImpl active;
	// private boolean first = true;
	int[] colorActive = new int[] { 245, 245, 245 };
	int[] colorBackground = new int[] { 199, 224, 241 };
	int[] colorInactive = new int[] { 111, 111, 111 };
	int[] colorInactiveGradient = new int[] { 63, 63, 63 };
	private IVerticalPanel panel0;
	private IVerticalPanel panel1;
	private List<INavigationListener> listeners = new LinkedList<INavigationListener>();
	IHorizontalPanel masterPanel;
	IGridPanel hPanel;
	List<NavigationGroupImpl> groups = new LinkedList<NavigationGroupImpl>();
	private IContentBuffer flipPage;
	private boolean panel0front;
	private NavigationGroupImpl moreGroup;
	private boolean setUpDynamicResize;
	private NavigationItemImpl moreItem;
	private IVerticalPanel borderTop;
	private boolean listeningOnServerCalls;
	private boolean holdUpdate;
	private INavigationGroup defaultGroup;
	private IFocusPanel focus;
	// private boolean loading;
	public static NavigationWidgetImpl instance;
	private Set<Integer> serverCallCounter = new HashSet<Integer>();
	private IVerticalPanel top;
	private IImage configureIcon;
	private IGridPanel separatorBorder;
	private IPanel<?> leftPartBorder;
	private IPanel<?> middlePartBorder;
	private IPanel<?> rightPartBorder;
	boolean showGroupLabel = true;
	private List<Action> actions = new LinkedList<Action>();
	private boolean showConfigure = true;
	boolean isLoading;

	public NavigationWidgetImpl(IContainer layout) {
		mainPanel = layout.panel().dock();
		focus = mainPanel.top().panel().focus();
		top = focus.add().panel().vertical();
		hPanel = top.add().panel().grid();
		borderTop = top.add().panel().vertical();
		addSeparatorBorder();
		hPanel.color().rgb(235, 235, 235).gradient().fallback(235, 235, 235)
				.vertical().rgb(211, 211, 211);
		masterPanel = hPanel.cell(0, 0).panel().horizontal();
		navigationPanel = masterPanel.add().panel().horizontal();
		navigationPanel.addSpace(10);
		history = mainPanel.center().panel().card();
		panel0 = history.add().panel().vertical();
		flipPage = new DoubleContentBuffer(panel0.add());
		activeBackground(flipPage);
		panel1 = history.add().panel().vertical();
		history.show(panel0);
		instance = this;
		ServerListener.instance = this;
		addResizableWidgetToDisplay(mainPanel);
	}

	void addSeparatorBorder() {
		if (!ADD_SEPARATORBORDER)
			return;
		setUpSeparatorBorder();
		boolean hasActiveItem = active != null && active.buttonPanel != null;
		int offsetX = 0;
		int width = 0;
		leftPartBorder.visible(hasActiveItem);
		middlePartBorder.visible(hasActiveItem);
		if (hasActiveItem) {
			int scrollOffset = Shell.instance().scrollOffset();
			offsetX = scrollOffset + active.buttonPanel.offsetX() + 1;
			leftPartBorder.size(offsetX, 1);
			width = active.buttonPanel.width() - 2;
			middlePartBorder.size(width, 1);
		}
		rightPartBorder.size(Shell.instance().width(mainPanel) - width
				- offsetX, 1);
	}

	void setUpSeparatorBorder() {
		if (separatorBorder != null)
			return;
		separatorBorder = borderTop.clear().add().panel().grid().spacing(0)
				.height(1);
		int c = 0;
		IGridCell indentBorder = separatorBorder.cell(c++, 0);
		leftPartBorder = indentBorder.panel().horizontal().visible(false);
		HorizontalScalingPanel.addDummyIE(leftPartBorder);
		leftPartBorder.color().gray();
		IGridCell activeBorder = separatorBorder.cell(c++, 0);
		middlePartBorder = activeBorder.panel().horizontal().visible(false);
		HorizontalScalingPanel.addDummyIE(middlePartBorder);
		activeBackground(middlePartBorder);
		rightPartBorder = separatorBorder.cell(c, 0).panel().horizontal()
				.size(Shell.instance().width(mainPanel), 1);
		HorizontalScalingPanel.addDummyIE(rightPartBorder);
		separatorBorder.column(2).expand();
		rightPartBorder.color().gray();
	}

	IContentBuffer flipPage() {
		panel0();
		return flipPage;
	}

	public void panel0() {
		if (panel0front)
			return;
		history.show(panel0);
		panel0front = true;
	}

	IVerticalPanel panel1() {
		panel0front = false;
		history.show(panel1);
		return panel1;
	}

	@Override
	public INavigationGroup addGroup() {
		// ensureSpaceBetweenGroups();
		NavigationGroupImpl group = new NavigationGroupImpl(this);
		groups.add(group);
		return group;
	}

	// private void ensureSpaceBetweenGroups() {
	// if (!first) {
	// navigationPanel.addSpace(5);
	// }
	// first = false;
	// }

	@Override
	public ITabWidget<INavigationGroup, INavigationItem> visible(
			boolean visible, ICallback<Void> cb) {
		setUpDynamicResize();
		cb.onSuccess(null);
		return this;
	}

	void setUpDynamicResize() {
		if (DYNAMIC_RESIZE && !setUpDynamicResize) {
			setUpDynamicResize = true;
			moreGroup = new NavigationGroupImpl(this, masterPanel)
					.visible(false);
			moreItem = (NavigationItemImpl) moreGroup.addTab().moreTab();
			moreItem.decorator(new TabDecoratorTemplate() {
				@Override
				public void refresh(ICallback<Void> cb) {
					if (DRAW_MORE_TOP) {
						IGridPanel p02 = panel.add().panel().grid();
						if (moreItem.popUpMovedRight()) {
							addBorder2(p02, 0, false);
							addBorder1(p02, 1);
						} else {
							addBorder1(p02, 0);
							addBorder2(p02, 1, true);
						}
					}
					IVerticalPanel p = panel.add().panel().vertical()
							.spacing(4);
					p.border().style().left().style().right().style().bottom()
							.color().gray();
					boolean nonEmpty = addLabelsToGridPanel(p);
					addActionsToPanel(p, nonEmpty);
					cb.onSuccess(null);
				}

				void addBorder2(IGridPanel p02, int p, boolean right) {
					IAbsolutePanel a1 = p02.cell(p, 0).panel().absolute()
							.size(moreItem.buttonPanel.width(), 1);
					HorizontalScalingPanel.addDummyIE(a1);
					a1.color().white();
					IBorder border = a1.border();
					IBorderStyle style = border.style();
					if (right)
						style.right();
					else
						style.left();
					border.color().gray();
				}

				void addBorder1(IGridPanel p02, int p) {
					IAbsolutePanel a0 = p02.cell(p, 0).panel().absolute()
							.height(1);
					HorizontalScalingPanel.addDummyIE(a0);
					a0.color().gray();
					p02.column(p).expand();
				}
			});
			update();
			addResizableWidgetToDisplay();
		}
	}

	@Override
	public void onResize(int w, int h) {
		updateAfterResize();
	}

	boolean update() {
		return update(false);
	}

	@Override
	public boolean update(boolean alwaysAdjust) {
		if (!DYNAMIC_RESIZE || moreGroup == null)
			return false;
		if (holdUpdate)
			return false;
		if (!alwaysAdjust
				&& Shell.instance().width(mainPanel) > masterPanel.width())
			return false;
		for (NavigationGroupImpl g : groups)
			for (NavigationItemImpl i : g.items)
				i.displayed(true);
		moreGroup.visible(true);
		boolean hidden = false;
		List<NavigationItemImpl> candidates = new LinkedList<NavigationItemImpl>();
		for (int i = groups.size() - 1; i >= 0
				&& Shell.instance().width(mainPanel) < masterPanel.width(); i--) {
			NavigationGroupImpl g = groups.get(i);
			for (int j = g.items.size() - 1; j >= 0
					&& Shell.instance().width(mainPanel) < masterPanel.width(); j--) {
				NavigationItemImpl ni = g.items.get(j);
				if (ni == active)
					continue;
				if (active != null && ni.group == active.group) {
					candidates.add(ni);
					continue;
				}
				ni.displayed(false);
				hidden = true;
			}
		}
		for (int i = 0; i < candidates.size()
				&& Shell.instance().width(mainPanel) < masterPanel.width(); i++) {
			candidates.get(i).displayed(false);
			hidden = true;
		}
		if ((!hidden && actions.isEmpty()) || !showConfigure) {
			moreGroup.visible(false);
		}
		if (moreItem.popUp != null)
			moreItem.popUp.visible(false);
		addSeparatorBorder();
		return true;
	}

	void active(NavigationItemImpl item, boolean viaClick,
			co.fxl.gui.api.ICallback<Void> cb, boolean notify) {
		active = item;
		if (notify) {
			if (listeners.isEmpty())
				cb.onSuccess(null);
			notifyListeners(active, viaClick, cb, listeners);
		}
		// if (!item.visible()) {
		// }
	}

	void activeBackground(IColored panel0) {
		applyColor(panel0.color(), colorActive);
	}

	void applyColor(IColor color, int[] rgb) {
		color.remove();
		color.rgb(rgb[0], rgb[1], rgb[2]);
	}

	private void notifyListeners(final NavigationItemImpl activeItem,
			final boolean viaClick, final ICallback<Void> cb,
			final List<INavigationListener> listeners2) {
		if (listeners2.isEmpty())
			return;
		if (listeners2.size() > 1) {
			listeners2.get(0).onBeforeNavigation(activeItem, viaClick,
					new CallbackTemplate<Void>(cb) {
						@Override
						public void onSuccess(Void result) {
							List<INavigationListener> listeners3 = listeners2
									.subList(1, listeners2.size());
							notifyListeners(activeItem, viaClick, cb,
									listeners3);
						}
					});
		} else {
			listeners2.get(0).onBeforeNavigation(activeItem, viaClick, cb);
		}
	}

	// @Override
	// public IColor colorActive() {
	// return new NonRemovableColorTemplate() {
	//
	// @Override
	// public IColor setRGB(int r, int g, int b) {
	// colorActive = new int[] { r, g, b };
	// return this;
	// }
	// };
	// }
	//
	// @Override
	// public IColor colorBackground() {
	// throw new UnsupportedOperationException();
	// }
	//
	// @Override
	// public IColor colorInactive() {
	// return new NonRemovableColorTemplate() {
	//
	// @Override
	// public IColor setRGB(int r, int g, int b) {
	// colorInactive = new int[] { r, g, b };
	// return this;
	// }
	// };
	// }

	@Override
	public INavigationWidget refresh(ICallback<Void> cb) {
		updateAfterResize();
		if (active != null)
			active.active(true, cb);
		else
			cb.onSuccess(null);
		return this;
	}

	@Override
	public NavigationItemImpl activeItem() {
		return active;
	}

	@Override
	public INavigationWidget addNavigationListener(INavigationListener l) {
		listeners.add(l);
		return this;
	}

	@Override
	public INavigationGroup defaultGroup() {
		if (defaultGroup == null)
			defaultGroup = addGroup();
		return defaultGroup;
	}

	@Override
	public int height() {
		throw new UnsupportedOperationException();
	}

	void addActionsToPanel(IVerticalPanel p, boolean nonEmpty) {
		if (!showConfigure)
			return;
		int c = 0;
		for (final Action action : actions) {
			if (action.c != null && !action.c.satisfied(active))
				continue;
			c++;
		}
		if (nonEmpty && c > 0)
			p.add().line();
		IVerticalPanel actionPanel = p.add().panel().vertical().spacing(6);
		for (final Action action : actions) {
			if (action.c != null && !action.c.satisfied(active))
				continue;
			ILabel li = actionPanel.add().label().text(action.c.label())
					.hyperlink();
			li.font().pixel(14).weight().bold();
			li.addClickListener(new LazyClickListener() {
				@Override
				protected void onAllowedClick() {
					IPopUp pp = moreItem.popUp;
					pp.visible(false);
					action.listener.onClick();
				}
			});
		}
	}

	boolean addLabelsToGridPanel(IVerticalPanel p) {
		if (!hasInvisibleItems())
			return false;
		IGridPanel gp = p.add().panel().horizontal().add().panel().grid()
				.spacing(6);
		int r = 0;
		boolean added = false;
		for (final NavigationGroupImpl g : groups) {
			boolean show = false;
			for (final NavigationItemImpl i : g.items) {
				if (!i.displayed() && i.visible())
					show = true;
			}
			if (!show)
				continue;
			final ILabel lg = gp.cell(0, r).valign().begin().panel().vertical()
					.addSpace(1).add().label()
					.text(!g.showGroupLabel ? "" : g.name());
			lg.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					lg.font().underline(true);
				}

				@Override
				public void onMouseOut() {
					lg.font().underline(false);
				}
			});
			lg.font().weight().bold().pixel(11);
			lg.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					moreItem.popUp.visible(false);
					g.items.get(0).active(true, DummyCallback.voidInstance());
				}
			});
			IVerticalPanel v = gp.cell(1, r).panel().vertical();
			for (final NavigationItemImpl i : g.items) {
				if (i.displayed() || !i.visible())
					continue;
				ILabel li = v.add().label().text(i.name()).hyperlink();
				li.font().pixel(14).weight().bold();
				li.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						moreItem.popUp.visible(false);
						i.active(true, DummyCallback.voidInstance());
					}
				});
				v.addSpace(2);
				added = true;
			}
			r++;
		}
		return added;
	}

	private boolean hasInvisibleItems() {
		for (final NavigationGroupImpl g : groups) {
			for (final NavigationItemImpl i : g.items) {
				if (!i.displayed() && i.visible())
					return true;
			}
		}
		return false;
	}

	@Override
	public INavigationWidget showGroupLabel(boolean showGroupLabel) {
		this.showGroupLabel = showGroupLabel;
		for (NavigationGroupImpl g : groups)
			g.updateVisibilityLabel();
		return this;
	}

	@Override
	public void notifyServerCallStart(int id) {
		if (listeningOnServerCalls) {
			serverCallCounter.add(id);
			if (serverCallCounter.size() == 1) {
				flipPage().back();
			}
		}
	}

	@Override
	public void notifyServerCallReturn(int id) {
		if (listeningOnServerCalls) {
			serverCallCounter.remove(id);
			if (serverCallCounter.isEmpty()) {
				flipPage().preview();
			}
		}
	}

	@Override
	public INavigationWidget holdUpdate(boolean b) {
		holdUpdate = b;
		return this;
	}

	void listeningOnServerCalls(boolean b) {
		serverCallCounter.clear();
		listeningOnServerCalls = b;
	}

	public void updateAfterResize() {
		if (FIX_SEPARATOR_BORDER) {
			update(true);
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					addSeparatorBorder();
				}
			});
		} else if (!update(true)) {
			addSeparatorBorder();
		}
	}

	@Override
	public INavigationWidget clearCache() {
		for (NavigationGroupImpl g : groups)
			g.clearCache();
		return this;
	}

	@Override
	public boolean active(String preset, ICallback<Void> cb) {
		for (NavigationGroupImpl g : groups) {
			if (g.active(preset, cb)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public INavigationItem findByName(String name) {
		for (NavigationGroupImpl g : groups) {
			if (g.findByName(name) != null) {
				return g.findByName(name);
			}
		}
		return null;
	}

	@Override
	public INavigationWidget configureListener(IClickListener l) {
		configureIcon = navigationPanel.add().image().resource("configure.png")
				.clickable(true);
		configureIcon.margin().left(8);
		focus.addMouseOverListener(new IMouseOverListener() {

			@Override
			public void onMouseOver() {
				configureIcon.resource("configure.png").clickable(true);
			}

			@Override
			public void onMouseOut() {
				configureIcon.resource("empty_16x16.png").clickable(false);
			}
		});
		configureIcon.addClickListener(l);
		return this;
	}

	@Override
	public INavigationWidget showConfigure(boolean b) {
		showConfigure = b;
		if (configureIcon != null)
			configureIcon.visible(b);
		return this;
	}

	@Override
	public INavigationWidget active(ICallback<Void> c) {
		active = null;
		for (NavigationGroupImpl g : groups)
			if (g.visible())
				for (NavigationItemImpl i : g.items)
					if (i.visible()) {
						i.active(true, c);
						return this;
					}
		c.onSuccess(null);
		return this;
	}

	@Override
	public INavigationWidget clearConfigureActions() {
		actions.clear();
		return this;
	}

	@Override
	public INavigationWidget addConfigureAction(IClickListener clickListener,
			IConstraint c) {
		Action action = new Action(clickListener, c);
		actions.add(action);
		return this;
	}

	void loading(boolean b) {
		isLoading = b;
	}

	// void loading(boolean b) {
	// loading = b;
	// addSeparatorBorder();
	// }
}
