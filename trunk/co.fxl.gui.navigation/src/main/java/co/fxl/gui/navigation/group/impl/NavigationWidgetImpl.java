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

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.FlipPage;
import co.fxl.gui.navigation.api.ITabDecorator;
import co.fxl.gui.navigation.api.ITabWidget;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.group.api.INavigationWidget;

public class NavigationWidgetImpl implements INavigationWidget {

	// TODO Code: Look: reactivate double buffering, no flickering, add
	// temp-flip-mechanism to FlipPage

	private static final boolean DYNAMIC_RESIZE = true;
	protected IDockPanel mainPanel;
	ILinearPanel<?> navigationPanel;
	private ICardPanel history;
	NavigationItemImpl active;
	private boolean first = true;
	int[] colorActive = new int[] { 245, 245, 245 };
	int[] colorBackground = new int[] { 199, 224, 241 };
	int[] colorInactive = new int[] { 111, 111, 111 };
	int[] colorInactiveGradient = new int[] { 63, 63, 63 };
	private IVerticalPanel panel0;
	private IVerticalPanel panel1;
	private List<INavigationListener> listeners = new LinkedList<INavigationListener>();
	ILinearPanel<?> masterPanel;
	IGridPanel hPanel;
	List<NavigationGroupImpl> groups = new LinkedList<NavigationGroupImpl>();
	private FlipPage flipPage;
	private boolean panel0front;
	private NavigationGroupImpl moreGroup;
	private boolean setUpDynamicResize;

	public NavigationWidgetImpl(IContainer layout) {
		mainPanel = layout.panel().dock();
		hPanel = mainPanel.top().panel().grid();
		hPanel.color().rgb(235, 235, 235).gradient().fallback(235, 235, 235)
				.vertical().rgb(211, 211, 211);
		ILayout l = hPanel.cell(0, 0).panel();
		masterPanel = createPanel(l);
		navigationPanel = createPanel(masterPanel.add().panel());
		navigationPanel.addSpace(10);
		history = mainPanel.center().panel().card();
		panel0 = history.add().panel().vertical();
		flipPage = new FlipPage(panel0.add());
		panel1 = history.add().panel().vertical();
		history.show(panel0);
	}

	ILinearPanel<?> createPanel(ILayout l) {
		return l.horizontal();
	}

	FlipPage flipPage() {
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
		ensureSpaceBetweenGroups();
		NavigationGroupImpl group = new NavigationGroupImpl(this);
		groups.add(group);
		return group;
	}

	private void ensureSpaceBetweenGroups() {
		if (!first) {
			navigationPanel.addSpace(5);
		}
		first = false;
	}

	@Override
	public ITabWidget<INavigationGroup, INavigationItem> visible(boolean visible) {
		setUpDynamicResize();
		return this;
	}

	void setUpDynamicResize() {
		if (DYNAMIC_RESIZE && !setUpDynamicResize) {
			setUpDynamicResize = true;
			moreGroup = new NavigationGroupImpl(this).visible(false);
			final NavigationItemImpl moreItem = (NavigationItemImpl) moreGroup
					.addTab().moreTab();
			moreItem.decorator(new ITabDecorator() {
				@Override
				public void decorate(IVerticalPanel panel, ICallback<Void> cb) {
					IGridPanel gp = panel.add().panel().horizontal().add()
							.panel().grid().spacing(6);
					int r = 0;
					for (final NavigationGroupImpl g : groups) {
						ILabel lg = gp.cell(0, r).panel().vertical()
								.addSpace(3).add().label().text(g.name());
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
							ILabel li = v.add().label().text(i.name());
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
					cb.onSuccess(null);
				}
			});
			update();
			Display.instance().addResizeListener(new IResizeListener() {
				@Override
				public boolean onResize(int width, int height) {
					if (!mainPanel.visible())
						return false;
					update();
					return true;
				}
			});
		}
	}

	void update() {

		// TODO ensure active item is visible

		for (NavigationGroupImpl g : groups)
			for (NavigationItemImpl i : g.items)
				i.visible(true);
		moreGroup.visible(true);
		boolean hidden = false;
		for (int i = groups.size() - 1; i >= 0
				&& Display.instance().width() < navigationPanel.width(); i--) {
			NavigationGroupImpl g = groups.get(i);
			for (int j = g.items.size() - 1; j >= 0
					&& Display.instance().width() < navigationPanel.width(); j--) {
				NavigationItemImpl ni = g.items.get(j);
				ni.visible(false);
				hidden = true;
			}
		}
		if (!hidden) {
			moreGroup.visible(false);
		}
	}

	void active(NavigationItemImpl item, boolean viaClick,
			co.fxl.gui.api.ICallback<Void> cb, boolean notify) {
		active = item;
		if (notify) {
			if (listeners.isEmpty())
				cb.onSuccess(null);
			notifyListeners(active, viaClick, cb, listeners);
		}
		if (!item.visible()) {
			update();
		}
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
}
