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
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.group.api.INavigationWidget;

public class NavigationWidgetImpl implements INavigationWidget {

	private IDockPanel mainPanel;
	IHorizontalPanel navigationPanel;
	private ICardPanel history;
	private NavigationItemImpl active;
	private boolean first = true;
	int[] colorActive = new int[] { 245, 245, 245 };
	int[] colorBackground = new int[] { 199, 224, 241 };
	int[] colorInactive = new int[] { 111, 111, 111 };
	int[] colorInactiveGradient = new int[] { 63, 63, 63 };
	private IVerticalPanel panel0;
	private IVerticalPanel panel1;
	private List<INavigationListener> listeners = new LinkedList<INavigationListener>();

	protected NavigationWidgetImpl(IContainer layout) {
		mainPanel = layout.panel().dock();
		IGridPanel hPanel = mainPanel.top().panel().grid();
		hPanel.color().rgb(235, 235, 235).gradient().vertical()
				.rgb(211, 211, 211);
		navigationPanel = hPanel.cell(0, 0).panel().horizontal().add().panel()
				.horizontal();
		navigationPanel.addSpace(10);
		history = mainPanel.center().panel().card();
		panel0 = history.add().panel().vertical();
		panel1 = history.add().panel().vertical();
		history.show(panel0);
	}

	IVerticalPanel panel0() {
		history.show(panel0);
		return panel0;
	}

	IVerticalPanel panel1() {
		history.show(panel1);
		return panel1;
	}

	@Override
	public INavigationGroup addGroup() {
		if (!first) {
			navigationPanel.addSpace(5);
		}
		first = false;
		return new NavigationGroupImpl(this);
	}

	@Override
	public INavigationWidget visible(boolean visible) {
		return this;
	}

	void active(NavigationItemImpl item,
			co.fxl.gui.api.template.ICallback<Void> cb, boolean notify) {
		if (active != null && active != item) {
			active.showLabelAsInactive();
		}
		active = item;
		if (notify)
			notifyListeners(active, cb, listeners);
	}

	private void notifyListeners(final NavigationItemImpl activeItem,
			final ICallback<Void> cb, final List<INavigationListener> listeners2) {
		if (listeners2.isEmpty())
			return;
		if (listeners2.size() > 1) {
			listeners2.get(0).onBeforeNavigation(activeItem,
					new CallbackTemplate<Void>() {
						@Override
						public void onSuccess(Void result) {
							List<INavigationListener> listeners3 = listeners2
									.subList(1, listeners2.size());
							notifyListeners(activeItem, cb, listeners3);
						}
					});
		} else {
			listeners2.get(0).onBeforeNavigation(activeItem, cb);
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
		// return new NonRemovableColorTemplate() {
		//
		// @Override
		// public IColor rgb(int r, int g, int b) {
		// // colorBackground = new int[] { r, g, b };
		// return this;
		// }
		// };
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
