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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.impl.SplitLayoutConfig;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.group.api.INavigationWidget.INavigationListener;

public class NavigationDialog {

	public static void addButton(final NavigationWidgetImpl widget) {
		widget.masterPanel.addSpace(4).add().label().text("More >").hyperlink()
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						throw new MethodNotImplementedException();
					}
				}).mouseLeft();
		SplitLayoutConfig.panel = widget.hPanel.cell(1, 0).align().end()
				.valign().center().panel().horizontal();
		widget.addNavigationListener(new INavigationListener() {
			@Override
			public void onBeforeNavigation(INavigationItem activeItem,
					boolean viaClick, ICallback<Void> cb) {
				NavigationItemImpl item = (NavigationItemImpl) activeItem;
				for (NavigationGroupImpl group : widget.groups) {
					group.visible(item.group == group);
				}
				for (NavigationItemImpl groupItem : item.group.items) {
					groupItem.visible(groupItem == item);
				}
				cb.onSuccess(null);
			}
		});
	}
}
