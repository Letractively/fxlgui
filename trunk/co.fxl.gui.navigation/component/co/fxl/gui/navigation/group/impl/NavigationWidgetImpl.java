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

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.navigation.group.api.INavigationGroup;
import co.fxl.gui.navigation.group.api.INavigationWidget;

public class NavigationWidgetImpl implements INavigationWidget {

	private IDockPanel mainPanel;
	IHorizontalPanel navigationPanel;
	ICardPanel history;
	private NavigationItemImpl active;
	private boolean first = true;
	int[] colorActive = new int[] { 245, 245, 245 };
	int[] colorBackground = new int[] { 0, 51, 102 };
	int[] colorInactive = new int[] { 228, 228, 255 };

	protected NavigationWidgetImpl(IContainer layout) {
		mainPanel = layout.panel().dock();
		IGridPanel hPanel = mainPanel.top().panel().grid();
		hPanel.color().rgb(0, 51, 102);
		navigationPanel = hPanel.cell(0, 0).panel().horizontal().add().panel()
				.horizontal();
		navigationPanel.addSpace(10);
		history = mainPanel.center().panel().card();
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

	void active(NavigationItemImpl item) {
		if (active != null) {
			active.showLabelAsInactive();
		}
		active = item;
	}

	@Override
	public IColor colorActive() {
		return new NonRemovableColorTemplate() {

			@Override
			public IColor rgb(int r, int g, int b) {
				colorActive = new int[] { r, g, b };
				return this;
			}
		};
	}

	@Override
	public IColor colorBackground() {
		return new NonRemovableColorTemplate() {

			@Override
			public IColor rgb(int r, int g, int b) {
				colorBackground = new int[] { r, g, b };
				return this;
			}
		};
	}

	@Override
	public IColor colorInactive() {
		return new NonRemovableColorTemplate() {

			@Override
			public IColor rgb(int r, int g, int b) {
				colorInactive = new int[] { r, g, b };
				return this;
			}
		};
	}
}
