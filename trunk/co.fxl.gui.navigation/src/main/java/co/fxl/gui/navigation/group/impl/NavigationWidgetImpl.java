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

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.FlipPage;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.group.api.INavigationWidget;

class NavigationWidgetImpl implements INavigationWidget {

	// TODO Code: Look: reactivate double buffering, no flickering, add
	// temp-flip-mechanism to FlipPage

	private IDockPanel mainPanel;
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

	NavigationWidgetImpl(IContainer layout) {
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
	public INavigationWidget visible(boolean visible) {
		return this;
	}

	void active(NavigationItemImpl item, boolean viaClick,
			co.fxl.gui.api.ICallback<Void> cb, boolean notify) {
		active = item;
		if (notify) {
			if (listeners.isEmpty())
				cb.onSuccess(null);
			notifyListeners(active, viaClick, cb, listeners);
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

	@Override
	public IColor colorActive() {
		return new NonRemovableColorTemplate() {

			@Override
			public IColor setRGB(int r, int g, int b) {
				colorActive = new int[] { r, g, b };
				return this;
			}
		};
	}

	@Override
	public IColor colorBackground() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColor colorInactive() {
		return new NonRemovableColorTemplate() {

			@Override
			public IColor setRGB(int r, int g, int b) {
				colorInactive = new int[] { r, g, b };
				return this;
			}
		};
	}

	@Override
	public INavigationWidget refresh() {
		if (active != null)
			active.active();
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
}
