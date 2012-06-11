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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.DisplayResizeAdapter;
import co.fxl.gui.navigation.api.ITabWidget;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.group.api.INavigationWidget;
import co.fxl.gui.navigation.impl.TabDecoratorTemplate;

public class NavigationWidgetImpl implements INavigationWidget {

	private static final boolean DYNAMIC_RESIZE = true;
	protected static final boolean DRAW_MORE_TOP = Constants.get(
			"NavigationWidgetImpl.DRAW_MORE_TOP", true);
	public static boolean ADD_SEPARATORBORDER = true;
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
	private FlipPageContentBuffer flipPage;
	private boolean panel0front;
	private NavigationGroupImpl moreGroup;
	private boolean setUpDynamicResize;
	private NavigationItemImpl moreItem;
	private IVerticalPanel borderTop;
	boolean listeningOnServerCalls;
	public static NavigationWidgetImpl instance;

	public NavigationWidgetImpl(IContainer layout) {
		mainPanel = layout.panel().dock();
		IVerticalPanel top = mainPanel.top().panel().vertical();
		hPanel = top.add().panel().grid();
		borderTop = top.add().panel().vertical();
		addSeparatorBorder();
		hPanel.color().rgb(235, 235, 235).gradient().fallback(235, 235, 235)
				.vertical().rgb(211, 211, 211);
		ILayout l = hPanel.cell(0, 0).panel();
		masterPanel = createPanel(l);
		navigationPanel = createPanel(masterPanel.add().panel());
		navigationPanel.addSpace(10);
		history = mainPanel.center().panel().card();
		panel0 = history.add().panel().vertical();
		flipPage = new FlipPageContentBuffer(panel0.add());
		activeBackground(flipPage);
		panel1 = history.add().panel().vertical();
		history.show(panel0);
		instance = this;
	}

	void addSeparatorBorder() {
		if (!ADD_SEPARATORBORDER)
			return;
		IGridPanel separatorBorder = borderTop.clear().add().panel().grid()
				.spacing(0).height(1);
		int c = 0;
		boolean b = active != null && active.buttonPanel != null;
		if (b) {
			IGridCell indentBorder = separatorBorder.cell(c++, 0);
			int offsetX = active.buttonPanel.offsetX() + 1;
			IPanel<?> leftPartBorder = indentBorder.panel().absolute()
					.height(1).width(offsetX);
			leftPartBorder.color().gray();
			IGridCell activeBorder = separatorBorder.cell(c++, 0);
			int width = active.buttonPanel.width() - 2;
			IPanel<?> middlePartBorder = activeBorder.panel().absolute()
					.height(1).width(width);
			activeBackground(middlePartBorder);
		}
		IPanel<?> rightPartBorder = separatorBorder.cell(c, 0).panel()
				.absolute().height(1);
		if (b)
			separatorBorder.column(2).expand();
		rightPartBorder.color().gray();
	}

	IHorizontalPanel createPanel(ILayout l) {
		return l.horizontal();
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
	public ITabWidget<INavigationGroup, INavigationItem> visible(boolean visible) {
		setUpDynamicResize();
		return this;
	}

	void setUpDynamicResize() {
		if (DYNAMIC_RESIZE && !setUpDynamicResize) {
			setUpDynamicResize = true;
			moreGroup = new NavigationGroupImpl(this).visible(false);
			moreItem = (NavigationItemImpl) moreGroup.addTab().moreTab();
			moreItem.decorator(new TabDecoratorTemplate() {
				@Override
				public void refresh(ICallback<Void> cb) {
					if (DRAW_MORE_TOP) {
						IGridPanel p02 = panel.add().panel().grid();
						IAbsolutePanel a0 = p02.cell(0, 0).panel().absolute()
								.height(1);
						a0.color().gray();
						p02.column(0).expand();
						IAbsolutePanel a1 = p02.cell(1, 0).panel().absolute()
								.height(1).width(moreItem.buttonPanel.width());
						a1.color().white();
						a1.border().style().right().color().gray();
					}
					IVerticalPanel p = panel.add().panel().vertical()
							.spacing(4);
					p.border().style().left().style().right().style().bottom()
							.color().gray();
					IGridPanel gp = p.add().panel().horizontal().add().panel()
							.grid().spacing(6);
					addLabelsToGridPanel(gp);
					cb.onSuccess(null);
				}
			});
			update();
			DisplayResizeAdapter.addResizeListener(new IResizeListener() {
				@Override
				public boolean onResize(int width, int height) {
					if (!update()) {
						addSeparatorBorder();
					}
					return true;
				}
			}).linkLifecycle(mainPanel);
		}
	}

	boolean update() {
		if (!DYNAMIC_RESIZE || moreGroup == null)
			return false;
		for (NavigationGroupImpl g : groups)
			for (NavigationItemImpl i : g.items)
				i.displayed(true);
		moreGroup.visible(true);
		boolean hidden = false;
		List<NavigationItemImpl> candidates = new LinkedList<NavigationItemImpl>();
		for (int i = groups.size() - 1; i >= 0
				&& Display.instance().width() < navigationPanel.width(); i--) {
			NavigationGroupImpl g = groups.get(i);
			for (int j = g.items.size() - 1; j >= 0
					&& Display.instance().width() < navigationPanel.width(); j--) {
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
				&& Display.instance().width() < navigationPanel.width(); i++) {
			candidates.get(i).displayed(false);
			hidden = true;
		}
		if (!hidden) {
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
					new CallbackTemplate<Void>() {
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
	public INavigationWidget refresh() {
		if (active != null)
			active.active(true);
		return this;
	}

	@Override
	public INavigationItem activeItem() {
		return active;
	}

	@Override
	public INavigationWidget addNavigationListener(INavigationListener l) {
		listeners.add(l);
		return this;
	}

	@Override
	public INavigationGroup defaultGroup() {
		return addGroup();
	}

	@Override
	public int height() {
		throw new UnsupportedOperationException();
	}

	void addLabelsToGridPanel(IGridPanel gp) {
		int r = 0;
		for (final NavigationGroupImpl g : groups) {
			boolean show = false;
			for (final NavigationItemImpl i : g.items) {
				if (!i.displayed() && i.visible())
					show = true;
			}
			if (!show)
				continue;
			final ILabel lg = gp.cell(0, r).valign().begin().panel().vertical()
					.addSpace(1).add().label().text(g.name());
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
					g.items.get(0).active(true);
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
						i.active(true);
					}
				});
				v.addSpace(2);
			}
			r++;
		}
	}

	public void notifyServerCallStart() {
		if (listeningOnServerCalls)
			flipPage().back();
	}

	public void notifyServerCallReturn() {
		if (listeningOnServerCalls)
			flipPage().preview();
	}
}
